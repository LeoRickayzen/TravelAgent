package org.jboss.quickstarts.wfk.travelagent;

import java.io.Serializable;
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

import org.jboss.quickstarts.wfk.booking.FlightBooking;
import org.jboss.quickstarts.wfk.customer.Customer;

@Entity
@NamedQueries({
	@NamedQuery(name = TABooking.FIND_ALL, query = "SELECT b FROM TABooking b ORDER BY b.id DESC"),
	@NamedQuery(name = TABooking.FIND_BY_NUMBER, query = "SELECT b FROM TABooking b WHERE b.id = :number")
})
@XmlRootElement
@Table(name = "TABooking", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class TABooking implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "TravelAgentBooking.findAll";
	public static final String FIND_BY_NUMBER = "TravelAgentBooking.findByNumber";
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "customer")
	private Customer customer;
	
	@Column(name = "taxiId")
	private Long taxiId;
	
	@Column(name = "taxiBookingId")
	private Long taxiBookingId;

	@Column(name = "hotelId")
	private Long hotelId;
	
	@Column(name = "hotelBookingId")
	private Long hotelBookingId;
	
	@Column(name = "flightId")
	private Long flightId;
	
	@Column(name = "flightBookingId")
	private Long flightBookingId;
	
	@Column(name = "date")
	private Date time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(Long taxiId) {
		this.taxiId = taxiId;
	}

	public Long getTaxiBookingId() {
		return taxiBookingId;
	}

	public void setTaxiBookingId(Long taxiBookingId) {
		this.taxiBookingId = taxiBookingId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	
	public Long getHotelBookingId() {
		return hotelBookingId;
	}

	public void setHotelBookingId(Long hotelBookingId) {
		this.hotelBookingId = hotelBookingId;
	}

	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}

	public Long getFlightBookingId() {
		return flightBookingId;
	}

	public void setFlightBookingId(Long flightBookingId) {
		this.flightBookingId = flightBookingId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
