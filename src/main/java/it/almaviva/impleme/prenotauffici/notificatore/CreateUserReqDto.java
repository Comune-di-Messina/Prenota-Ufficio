package it.almaviva.impleme.prenotauffici.notificatore;

import java.util.List;
import lombok.Data;

public @Data class CreateUserReqDto{
	private List<ChannelPreferencesItem> channelPreferences;
	private String userId;
	private List<ServicesChannelItem> servicesChannel;
}