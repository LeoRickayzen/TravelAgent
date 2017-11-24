package org.jboss.quickstarts.wfk.flight;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.booking.FlightBooking;
import org.jboss.quickstarts.wfk.travelagent.Booking;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@NamedQueries({
	@NamedQuery(name = Flight.FIND_ALL, query = "SELECT f FROM Flight f ORDER BY f.flightNumber DESC"),
	@NamedQuery(name = Flight.FIND_BY_NUMBER, query = "SELECT f FROM Flight f WHERE f.flightNumber = :number")
})
@XmlRootElement
@Table(name="flight", uniqueConstraints = @UniqueConstraint(columnNames = "flightNumber"))
public class Flight implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "Flight.findAll";
	public static final String FIND_BY_NUMBER = "Flight.findByNumber";
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@NotNull
	@Pattern(regexp = "^([a-z]|\\d){5}$")
	@Column(name = "flightNumber")
	private String flightNumber;
	
	@NotNull
	@Pattern(regexp = "^[A-Z]{3}$")
	@Column(name = "departure")
	private String departure;
	
	@NotNull
	@Pattern(regexp = "^[A-Z]{3}$")
	@Column(name = "arrival")
	private String arrival;
	
	@JsonIgnore
	@OneToMany(mappedBy="flightBooked", cascade = CascadeType.ALL)
	private Set<FlightBooking> bookings;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	
	public void setBookings(Set<FlightBooking> bookings){
    	this.bookings = bookings;
    }
    
    public Set<FlightBooking> getBookings(){
    	return bookings;
    }
}