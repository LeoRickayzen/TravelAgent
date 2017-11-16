package org.jboss.quickstarts.wfk.flight;

public class FlightValidator {
	
	private FlightRepository crud = new FlightRepository();
	
	void validateFlight(Flight flight){
		if(departIsDifferent(flight)){
			throw new InvalidRouteException("departure and arrival destination must be different");
		}
		/*
		if(flightNumberExists(flight)){
			throw new FlightNumberExistsException("Please enter a unique flight number");
		}
		*/
	}
	
	boolean departIsDifferent(Flight flight){
		return flight.getArrival().equals(flight.getDeparture());
	}
	
	boolean flightNumberExists(Flight flight){
		return crud.findByNumber(flight.getFlightNumber()) != null;
	}
}