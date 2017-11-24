package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightRepository;
import org.jboss.quickstarts.wfk.flight.FlightValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

public class BookingService {
	@Inject
	private @Named("logger") Logger log;

	@Inject
	private BookingValidator validator;

	@Inject
	private BookingRepository crud;
	
	List<FlightBooking> findAll(){
		return crud.findAllBookings();
	}
	
	public void createBooking(FlightBooking booking) throws InvalidCredentialsException, ConstraintViolationException{
		validator.validateBooking(booking);
		crud.createBooking(booking);
	}
	
	public FlightBooking deleteBooking(FlightBooking booking) {
		
		log.info("delete() - Deleting " + booking.toString());
		
		FlightBooking deletedBooking = null;
		
		if(booking.getBookingNumber() != null){
			deletedBooking = crud.deleteBooking(booking);
		}else{
			log.info("delete() - No ID was found so can't Delete.");
		}
		
		return deletedBooking;
	}
	
	public List<FlightBooking> findByCustomerId(Long id){
		return crud.findByCustomerId(id);
	}
	
	public FlightBooking findById(long id){
		return crud.findByNumber(id);
	}
}