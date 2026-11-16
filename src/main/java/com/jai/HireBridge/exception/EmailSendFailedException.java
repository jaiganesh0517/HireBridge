package com.jai.HireBridge.exception;

public class EmailSendFailedException extends RuntimeException {

	private final int attempts;
	
	public EmailSendFailedException(int attempts, Throwable cause) {
		super(cause.getMessage(),cause);
		this.attempts = attempts;
	}
	public int getAttempts() {
		return attempts;
	}
}
