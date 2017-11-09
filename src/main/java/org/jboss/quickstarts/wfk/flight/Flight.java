package org.jboss.quickstarts.wfk.flight;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
	@NamedQuery(name = Flight.FIND_ALL, query = "SELECT f FROM Flight f ORDER BY f.flightNumber DESC")
})
@XmlRootElement
@Table(name="flight", uniqueConstraints = @UniqueConstraint(columnNames = "flightNumber"))
public class Flight implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "Flight.findAll";
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	
	@NotNull
	@Pattern(regexp = "^([a-z]|\\d){5}$")
	@Column(name = "flightNumber")
	private String flightNumber;
	
	@NotNull
	@Column(name = "departure")
	private String departure;
	
	@NotNull
	@Column(name = "arrival")
	private String arrival;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	
	
}
