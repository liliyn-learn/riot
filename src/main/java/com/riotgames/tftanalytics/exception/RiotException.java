package com.riotgames.tftanalytics.exception;

public class RiotException extends Exception {
	private static final long serialVersionUID = 1L;
	private String mess;
	
	public RiotException(String message) {
		this.mess = message;
	}

	@Override
	public String toString() {
		return "Error " + mess;
	}
}
