package org.jboss.quickstarts.wfk.flight;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

public class FlightValidator {
	
	@Inject
    private Validator validator;
	
	@Inject
	private FlightRepository crud;
	
	void validateFlight(Flight flight) throws InvalidRouteException, FlightNumberExistsException, ConstraintViolationException, ValidationException {
		
		Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
		
		if(departIsDifferent(flight)){
			throw new InvalidRouteException("departure and arrival destination must be different");
		}
		if(flightNumberExists(flight)){
			throw new FlightNumberExistsException("Please enter a unique flight number");
		}
	}
	
	boolean departIsDifferent(Flight flight){
		return flight.getArrival().equals(flight.getDeparture());
	}
	
	boolean flightNumberExists(Flight flight){
		return crud.findByNumber(flight.getFlightNumber()) != null;
	}
}