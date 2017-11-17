package org.jboss.quickstarts.wfk.booking;

import javax.inject.Inject;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.flight.FlightRepository;
import org.jboss.quickstarts.wfk.user.UserRepository;

public class BookingValidator {
	
	@Inject
    private Validator validator;

    @Inject
    private FlightRepository crud;
    
    private UserRepository ccrud;
	
    void validateBooking(Booking booking){
    	flightExists(booking.getFlight().getFlightNumber());
    	customerExists(booking.getCustomer().getId());
    }
    
	void flightExists(String flightNumber) throws InvalidCredentialsException{
		if(crud.findByNumber(flightNumber) == null){
			throw new InvalidCredentialsException("invalid flight number");
		}
	}
	
	public void customerExists(Long customerID) throws InvalidCredentialsException{
		if(!(ccrud.findById(customerID) == null)){
			throw new InvalidCredentialsException("invalid customer id");
		}
	}
}
