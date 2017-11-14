package org.jboss.quickstarts.wfk.flight;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class FlightService {
	@Inject
	private @Named("logger") Logger log;

	@Inject
	private FlightValidator validator;

	@Inject
	private FlightRepository crud;

	private ResteasyClient client;
	
	public FlightService(){
		client = new ResteasyClientBuilder().build();
	}
	
	Flight createFlight(Flight flight) throws InvalidDestinationException{
		if(!validator.departIsDifferent(flight)){
			return crud.createFlight(flight);
		}else{
			throw new InvalidDestinationException("destination must be different from point of departure");
		}
	}
	
	List<Flight> findAll(){
		return crud.findAllOrderedByNumber();
	}

	public Flight deleteBooking(Flight flight) {
		return crud.deleteFlight(flight);
	}
	
	public Flight findById(long id){
		return crud.findFlightById(id);
	}
}
