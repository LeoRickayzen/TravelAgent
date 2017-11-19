package org.jboss.quickstarts.wfk.guestbooking;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.booking.BookingValidator;
import org.jboss.quickstarts.wfk.booking.InvalidCredentialsException;
import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightService;
import org.jboss.quickstarts.wfk.user.User;
import org.jboss.quickstarts.wfk.user.UserRestService;
import org.jboss.quickstarts.wfk.user.UserService;

public class GuestBookingService {
	
	@Inject
	UserService userService;
	@Inject
	BookingService bookingService;
	@Inject
	FlightService flightService;
	@Inject
	BookingValidator bookingValidator;
	@Inject
	UserRestService userRest;
	
	void createBooking(GuestBooking booking) throws ConstraintViolationException, ValidationException, Exception{
		User user = userService.create(booking.getCustomer());
		Flight flight = flightService.findById(booking.getBooking().getFlightBooked().getId());
		booking.getBooking().setCustomer(user);
		booking.getBooking().setFlightBooked(flight);
		bookingService.createBooking(booking.getBooking());
	}
}
