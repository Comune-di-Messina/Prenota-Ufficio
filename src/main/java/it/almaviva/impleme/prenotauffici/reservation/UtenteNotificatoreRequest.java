package it.almaviva.impleme.prenotauffici.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UtenteNotificatoreRequest {
	
	@JsonProperty("userId")
	private String userId;
	
	@JsonProperty("userMail")
	private String userMail;

	public UtenteNotificatoreRequest(String userId, String userMail) {
		super();
		this.userId = userId;
		this.userMail = userMail;
	}
	
	

}
