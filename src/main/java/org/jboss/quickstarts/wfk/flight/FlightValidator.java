package org.jboss.quickstarts.wfk.flight;

public class FlightValidator {
	boolean departIsDifferent(Flight flight){
		return flight.getArrival().equals(flight.getDeparture());
	}
}
