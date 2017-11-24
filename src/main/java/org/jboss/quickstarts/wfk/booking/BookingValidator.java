package org.jboss.quickstarts.wfk.booking;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.contact.Contact;
import org.jboss.quickstarts.wfk.customer.CustomerRepository;
import org.jboss.quickstarts.wfk.flight.FlightRepository;

public class BookingValidator {
	
	@Inject
    private Validator validator;

    @Inject
    private FlightRepository crud;
    
    @Inject
    private CustomerRepository ccrud;
    
	
    void validateBooking(FlightBooking booking) throws InvalidCredentialsException, ConstraintViolationException, ValidationException {
    	
    	Set<ConstraintViolation<FlightBooking>> violations = validator.validate(booking);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    	
    	flightExists(booking.getFlightBooked().getId());
    	customerExists(booking.getCustomer().getId());
    }
    
	void flightExists(Long id) throws InvalidCredentialsException{
		if(crud.findFlightById(id) == null){
			throw new InvalidCredentialsException("invalid flight number");
		}
	}
	
	public void customerExists(Long customerID) throws InvalidCredentialsException{
		if(ccrud.findById(customerID) == null){
			throw new InvalidCredentialsException("invalid customer id");
		}
	}
}
