package com.jnetsend.service;

public class SendMessageException extends Exception{
	public SendMessageException() {
		super("Cannot send message.");
	}

	public SendMessageException(String errMsg) {
		super("Cannot send message. Message: " + errMsg);
	}
}