package it.almaviva.impleme.prenotauffici.notificatore;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class ChannelPreferencesItem{
	private String destination;
	private String channelId;
	private String subChannelId;
}