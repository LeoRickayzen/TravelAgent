package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

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

	private ResteasyClient client;
	
	List<Booking> findAll(){
		return crud.findAllBookings();
	}
	
	Booking createBooking(Booking booking){
		crud.createBooking(booking);
		return booking;
	}
}
