package it.almaviva.impleme.prenotauffici.notificatore;

import lombok.Data;

public @Data class ContentParams{
	private String cognome;
	private String ufficio;
	private String nome;
	private String servizio;
	private String giorno;
	private String ora;
	private String note;
}