package org.jboss.quickstarts.wfk.guestbooking;

import java.io.Serializable;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.customer.Customer;

public class GuestBooking implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Customer customer;
	
	private Booking booking;
	
	public Customer getCustomer(){
		return customer;
	}
	
	public void setCustomer(Customer customer){
		this.customer = customer;
	}
	
	public Booking getBooking(){
		return booking;
	}
	
	public void setBooking(Booking booking){
		this.booking = booking;
	}
}
