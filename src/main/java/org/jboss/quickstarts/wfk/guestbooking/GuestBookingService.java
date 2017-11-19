package org.jboss.quickstarts.wfk.guestbooking;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.booking.BookingValidator;
import org.jboss.quickstarts.wfk.booking.InvalidCredentialsException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRestService;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightService;

public class GuestBookingService {
	
	@Inject
	CustomerService userService;
	@Inject
	BookingService bookingService;
	@Inject
	FlightService flightService;
	@Inject
	BookingValidator bookingValidator;
	@Inject
	CustomerRestService userRest;
	
	void createBooking(GuestBooking booking) throws ConstraintViolationException, ValidationException, Exception{
		Customer user = userService.create(booking.getCustomer());
		Flight flight = flightService.findById(booking.getBooking().getFlightBooked().getId());
		booking.getBooking().setCustomer(user);
		booking.getBooking().setFlightBooked(flight);
		bookingService.createBooking(booking.getBooking());
	}
}
