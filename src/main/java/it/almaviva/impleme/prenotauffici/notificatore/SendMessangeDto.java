package it.almaviva.impleme.prenotauffici.notificatore;

import java.util.List;
import lombok.Data;

public @Data class SendMessangeDto{
	private List<MessagesItem> messages;
	private String serviceId;
	private String userId;
}