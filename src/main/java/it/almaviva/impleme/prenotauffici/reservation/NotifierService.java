package it.almaviva.impleme.prenotauffici.reservation;

import it.almaviva.impleme.prenotauffici.notificatore.*;
import it.almaviva.impleme.prenotauffici.reservation.exception.ReservationNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class NotifierService {

    public static final String PRENOTA_UFFICIO = "prenota_ufficio";
    public static final String SMTP = "smtp";
    public static final String FIREBASE = "firebase";
    @Value(value="${notificatore.qrcode:#{null}}")
    String qrcodeGeneratorUrl;

    @Value(value="${esb.notificatore}")
    String esbNotificatoreUrl;


    @Autowired
    ReservationRepository repository;

    public byte[] generateQrcode(ReservationEntity reservation){
        if(null != qrcodeGeneratorUrl) {
            RestTemplate rest = new RestTemplate();

            String url = String.format("%s/qrcode?text=%s", qrcodeGeneratorUrl, reservation.getId());

            return rest.getForObject(url, byte[].class);
        }
        return null;
    }

    @Transactional
    public void sendUserNotificatore(String fiscalCode, String email) {
        RestTemplate rest = new RestTemplate();


        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<CreateUserReqDto> res = rest.exchange(esbNotificatoreUrl + "/api/v2/user/{user}", HttpMethod.GET, request, CreateUserReqDto.class, fiscalCode);
            if(res.getStatusCodeValue() == 200) {
                    final CreateUserReqDto body = res.getBody();
                final Optional<ServicesChannelItem> prenotaUfficio = body.getServicesChannel().stream().filter(item -> item.getServiceId().equals(PRENOTA_UFFICIO)).findAny();

                final Optional<ChannelPreferencesItem> smtp = body.getChannelPreferences().stream().filter(item -> item.getChannelId().equals(SMTP)).findAny();
                if(smtp.isPresent()){
                    smtp.get().setDestination(email);
                }else{
                    ChannelPreferencesItem cpi = new ChannelPreferencesItem();
                    cpi.setChannelId(SMTP);
                    cpi.setDestination(email);
                    body.getChannelPreferences().add(cpi);
                }


                if (prenotaUfficio.isEmpty()) {
                    log.debug("add prenota_ufficio service channel");

                    ServicesChannelItem sci = new ServicesChannelItem();
                    sci.setServiceId(PRENOTA_UFFICIO);
                    sci.setChannels(Arrays.asList(SMTP));

                    body.getServicesChannel().add(sci);
                }else{
                    final Optional<String> smtpSc = prenotaUfficio.get().getChannels().stream().filter(item -> item.equals(SMTP)).findAny();
                    if(smtpSc.isEmpty()){
                        prenotaUfficio.get().getChannels().add(SMTP);
                    }
                }

                HttpEntity requestUpdate = new HttpEntity(body);
                body.setUserId(null);
                rest.exchange(esbNotificatoreUrl + "/api/v2/user/{user}", HttpMethod.PUT, requestUpdate, CreateUserReqDto.class, fiscalCode);

            }
        }catch (HttpStatusCodeException e ){
            if (e.getStatusCode().value() == 404){

                ServicesChannelItem sci = new ServicesChannelItem();
                sci.setServiceId(PRENOTA_UFFICIO);
                sci.setChannels(Arrays.asList(SMTP));

                ChannelPreferencesItem cpiSmtp = new ChannelPreferencesItem();
                cpiSmtp.setChannelId(SMTP);
                cpiSmtp.setDestination(email);

                CreateUserReqDto cur = new CreateUserReqDto();
                cur.setUserId(fiscalCode);
                cur.setServicesChannel(Arrays.asList(sci));
                cur.setChannelPreferences(Arrays.asList(cpiSmtp));

                final Object o = rest.postForObject(esbNotificatoreUrl + "/api/v2/user", cur, Object.class);
                log.info("new user");
            }
        }




    }

    @Transactional
    public void inviaNotifica(Long idReservation) {
        final ReservationEntity reservationEntity = repository.findById(idReservation).orElseThrow(() -> new ReservationNotFound(idReservation));


        RestTemplate rest = new RestTemplate();

        String templateId = "";
        String titoloSmtp = "";
        String titoloFirebase = "";
        String messaggioFirebase = "";
        switch (reservationEntity.getStatus()){
            case CONFIRMED:
                templateId = "richiesta.html";
                titoloSmtp = "Prenotazione Ufficio - Nuova Prenotazione";
                titoloFirebase = "Prenotazione confermata";
                messaggioFirebase ="La prenotazione ${ufficio} - ${servizio}, per le ore ${ora} del giorno ${giorno} è confermata";
                break;
            case CANCELLATA:
                templateId = "cancellazione.html";
                titoloSmtp = "Prenotazione Ufficio - Cancellazione Prenotazione";
                titoloFirebase = "Prenotazione cancellata";
                messaggioFirebase ="La prenotazione ${ufficio} - ${servizio}, per le ore ${ora} del giorno ${giorno} è cancellata";
                break;
            case REVOCATA:
                templateId = "revoca.html";
                titoloSmtp = "Prenotazione Ufficio - Revoca Prenotazione";
                titoloFirebase = "Prenotazione revocata";
                messaggioFirebase ="La prenotazione ${ufficio} - ${servizio}, per le ore ${ora} del giorno ${giorno} è revocata";
                break;
        }

        ContentParams cp = new ContentParams();

        final UserEntity user = reservationEntity.getUser();
        cp.setNome(user.getName());
        cp.setCognome(user.getSurname());
        cp.setNote(reservationEntity.getNotes());
        cp.setUfficio(reservationEntity.getOffice().getName());
        cp.setServizio(reservationEntity.getPublicService().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy", Locale.ITALIAN);
        cp.setGiorno(reservationEntity.getDate().format(formatter));
        cp.setOra(reservationEntity.getStartTime().toString());

        MessagesItem mi = new MessagesItem();
        mi.setChannelId(SMTP);
        mi.setTitle(titoloSmtp);
        mi.setTemplateId(templateId);
        mi.setContentParams(cp);

        MessagesItem miFirebase = new MessagesItem();
        miFirebase.setChannelId(FIREBASE);
        miFirebase.setSubChannelId("impleme");
        miFirebase.setTitle(titoloFirebase);
        miFirebase.setBody(messaggioFirebase);
        miFirebase.setContentParams(cp);

        SendMessangeDto sm = new SendMessangeDto();
        sm.setServiceId(PRENOTA_UFFICIO);
        sm.setUserId(reservationEntity.getUser().getFiscalCode());
        sm.setMessages(Arrays.asList(mi, miFirebase));

        rest.postForObject(esbNotificatoreUrl+"/api/v2/message", sm, Void.class);

    }
}