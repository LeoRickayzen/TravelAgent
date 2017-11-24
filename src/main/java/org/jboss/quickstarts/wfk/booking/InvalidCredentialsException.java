package org.jboss.quickstarts.wfk.booking;

import javax.validation.ValidationException;

public class InvalidCredentialsException extends ValidationException {
	
	private static final long serialVersionUID = 1L;

	public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}
