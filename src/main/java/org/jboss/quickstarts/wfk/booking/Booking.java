package org.jboss.quickstarts.wfk.booking;

import java.util.Date;

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

import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.user.User;

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
	
	//@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "customer_id", nullable = false)
	private Long customerID;
	
	//@ManyToOne(targetEntity = Flight.class)
	@JoinColumn(name = "flight_id", nullable = false)
	private String flightID;
	
	@Column(name = "date")
	private Date time;

	public long getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(long bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public String getFlightID() {
		return flightID;
	}

	public void setFlightID(String flightID) {
		this.flightID = flightID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
