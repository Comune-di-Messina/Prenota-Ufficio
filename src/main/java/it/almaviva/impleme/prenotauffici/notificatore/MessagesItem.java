package it.almaviva.impleme.prenotauffici.notificatore;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class MessagesItem{
	private String destination;
	private String title;
	private String body;
	private ContentParams contentParams;
	private String channelId;
	private String subChannelId;
	private String templateId;
}