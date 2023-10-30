package it.almaviva.impleme.prenotauffici.reservation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.almaviva.impleme.prenotauffici.reservation.exception.UserUnauthorized;
import it.almaviva.impleme.prenotauffici.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationCreationDto;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationDto;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationUpdateDto;
import it.almaviva.impleme.prenotauffici.reservation.exception.UserNotFound;

@RestController
@RequestMapping("v1/reservation")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    ReservationMapper mapper;

    @Autowired
    ReservationService service;

    @Value("${jwk.url}")
    private String urlJwkProvider;

    @GetMapping
    public List<ReservationDto> list(@RequestParam Optional<String> userId,
            @RequestParam Optional<String> municipalityId, @RequestParam Optional<UUID> officeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
        try {
            return mapper.map(service.getAll(userId, municipalityId, officeId, startDate, endDate));
        } catch (UserNotFound e) {
            return List.of();
        }
    }

    @GetMapping("{id}")
    public ReservationDto get(@RequestHeader("X-Auth-Token") String xAuthToken,
                              @PathVariable Long id) {
        final ReservationDto reservationDto = mapper.map(service.getById(id));

        JWTUtil.verify(xAuthToken, urlJwkProvider);

        final String fiscalNumber = JWTUtil.getClaim(xAuthToken, "fiscalNumber").asString();
        List<String> jwtRolesList = JWTUtil.getClaimAsList(xAuthToken, "groups");

        final String cittadino = jwtRolesList.stream().filter(s -> s.equals("CITTADINO")).findAny().orElse(null);

        if(cittadino != null && (fiscalNumber == null || !reservationDto.getUserId().equals(fiscalNumber.substring(6)))){
                throw new UserUnauthorized();
        }


        return reservationDto;
    }

    @GetMapping(value = "{id}/qrcode", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] getQrCode(@PathVariable Long id) {
        return service.getById(id).getQrcode();
    }

    @PatchMapping("{id}")
    public ReservationDto update(@RequestHeader("X-Auth-Token") String xAuthToken,
                                 @PathVariable Long id, @RequestBody ReservationUpdateDto dto) {



        JWTUtil.verify(xAuthToken, urlJwkProvider);
        final ReservationDto reservationDto = mapper.map(service.getById(id));
        final String fiscalNumber = JWTUtil.getClaim(xAuthToken, "fiscalNumber").asString();
        List<String> jwtRolesList = JWTUtil.getClaimAsList(xAuthToken, "groups");

        final String cittadino = jwtRolesList.stream().filter(s -> s.equals("CITTADINO")).findAny().orElse(null);
        if(cittadino != null && (fiscalNumber == null || !reservationDto.getUserId().equals(fiscalNumber.substring(6)))){
            throw new UserUnauthorized();
        }


        return mapper.map(service.update(id, dto));
    }

    @PostMapping
    public ReservationDto create(@RequestBody @Valid ReservationCreationDto dto) {
        return mapper.map(service.create(dto));
    }
}
