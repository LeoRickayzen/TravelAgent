package org.jboss.quickstarts.wfk.flight;

import javax.validation.ValidationException;

public class FlightNumberExistsException extends ValidationException {
	public FlightNumberExistsException(String message) {
        super(message);
    }

    public FlightNumberExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlightNumberExistsException(Throwable cause) {
        super(cause);
    }
}
