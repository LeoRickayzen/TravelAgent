package org.jboss.quickstarts.wfk.flight;

import javax.validation.ValidationException;

public class InvalidDestinationException extends ValidationException {
	public InvalidDestinationException(Throwable cause) {
        super(cause);
    }

    public InvalidDestinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDestinationException(String message) {
        super(message);
    }
}