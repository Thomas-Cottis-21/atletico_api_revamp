package com.atletico.atletico_revamp.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
public class Metadata {
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Date timestamp = new Date();
	private String uri;

	public Metadata(String uri) {
		this.uri = uri;
	}
}
