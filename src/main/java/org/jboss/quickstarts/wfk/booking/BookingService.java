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

/**
 * methods provide interaction with booking entities with validation
 * 
 * @author Leo Rickayzen
 *
 */
public class BookingService {
	@Inject
	private @Named("logger") Logger log;

	@Inject
	private BookingValidator validator;

	@Inject
	private BookingRepository crud;
	
	/**
	 * get all the flight bookings
	 * 
	 * @return list of all flight bookings
	 */
	List<FlightBooking> findAll(){
		return crud.findAllBookings();
	}
	
	/**
	 * persist booking in database
	 * 
	 * @param booking to be persisted
	 * @throws InvalidCredentialsException thrown when referenced entities do not exist
	 * @throws ConstraintViolationException thrown when constraints defined in {@link FlightBooking.java} are violated 
	 */
	public void createBooking(FlightBooking booking) throws InvalidCredentialsException, ConstraintViolationException{
		validator.validateBooking(booking);
		crud.createBooking(booking);
	}
	
	/**
	 * delete a booking from the database
	 * 
	 * @param booking to be deleted
	 * @return deleted booking
	 */
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
	
	/**
	 * find a booking given the bookers id
	 * 
	 * @param id of customer
	 * @return the customer with the corresponding id
	 */
	public List<FlightBooking> findByCustomerId(Long id){
		return crud.findByCustomerId(id);
	}
	
	/**
	 * find a booking given it's id
	 * 
	 * @param id of the booking to find
	 * @return booking with the corresponding id
	 */
	public FlightBooking findById(long id){
		return crud.findByNumber(id);
	}
}