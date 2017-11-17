package org.jboss.quickstarts.wfk.guestbooking;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.booking.BookingValidator;
import org.jboss.quickstarts.wfk.booking.InvalidCredentialsException;
import org.jboss.quickstarts.wfk.user.User;
import org.jboss.quickstarts.wfk.user.UserService;

public class GuestBookingService {
	
	UserService userService;
	BookingService bookingService;
	BookingValidator bookingValidator;
	
	void createBooking(Booking booking) throws ConstraintViolationException, ValidationException, Exception{
		User user = booking.getCustomer();
		try{
			bookingValidator.customerExists(user.getId());
		}catch(InvalidCredentialsException ice){
			userService.create(user);
		}
		bookingService.createBooking(booking);
	}
}
