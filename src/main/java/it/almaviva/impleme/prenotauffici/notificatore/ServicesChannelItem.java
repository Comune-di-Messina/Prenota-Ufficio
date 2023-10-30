package it.almaviva.impleme.prenotauffici.notificatore;

import java.util.List;
import lombok.Data;

public @Data class ServicesChannelItem{
	private List<String> channels;
	private String serviceId;
}