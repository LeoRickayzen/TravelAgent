package org.jboss.quickstarts.wfk.flight;

import javax.validation.ValidationException;

public class InvalidRouteException extends ValidationException {
	public InvalidRouteException(String message) {
        super(message);
    }

    public InvalidRouteException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRouteException(Throwable cause) {
        super(cause);
    }
}
