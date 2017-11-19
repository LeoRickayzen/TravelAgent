package org.jboss.quickstarts.wfk.booking;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.flight.Flight;

@Entity
@NamedQueries({
	@NamedQuery(name = Booking.FIND_ALL, query = "SELECT b FROM Booking b ORDER BY b.bookingNumber DESC")
})
@XmlRootElement
@Table(name="booking", uniqueConstraints = @UniqueConstraint(columnNames = "bookingNumber"))
public class Booking {
	
	public static final String FIND_ALL = "Booking.findAll";
	
	@Id
	@Column(name = "bookingNumber")
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long bookingNumber;
	
	@ManyToOne
	@JoinColumn(name = "customer")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "flightBooked")
	private Flight flightBooked;
	
	@Column(name = "date")
	private Date time;

	public long getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(long bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Flight getFlightBooked() {
		return flightBooked;
	}

	public void setFlightBooked(Flight flight) {
		this.flightBooked = flight;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}	
}