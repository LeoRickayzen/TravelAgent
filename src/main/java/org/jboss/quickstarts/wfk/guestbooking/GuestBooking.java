package org.jboss.quickstarts.wfk.guestbooking;

import java.io.Serializable;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.user.User;

public class GuestBooking implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private User customer;
	
	private Booking booking;
	
	public User getCustomer(){
		return customer;
	}
	
	public void setCustomer(User customer){
		this.customer = customer;
	}
	
	public Booking getBooking(){
		return booking;
	}
	
	public void setBooking(Booking booking){
		this.booking = booking;
	}
}
