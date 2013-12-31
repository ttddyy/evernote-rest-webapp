package net.ttddyy.evernote.rest;

/**
 * @author Tadaya Tsuyukubo
 */
public class EvernoteRestException extends RuntimeException {

	public EvernoteRestException() {
	}

	public EvernoteRestException(String message) {
		super(message);
	}

	public EvernoteRestException(String message, Throwable cause) {
		super(message, cause);
	}

}
