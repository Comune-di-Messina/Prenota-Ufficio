package it.almaviva.impleme.prenotauffici.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UtenteNotificatoreResponse {
	
	@JsonProperty("code")
    private String code;
	
	@JsonProperty("userId")
    private String userId;
	
	@JsonProperty("descrizione")
    private String descrizione;
	
	@JsonProperty("success")
    private String success;

}
