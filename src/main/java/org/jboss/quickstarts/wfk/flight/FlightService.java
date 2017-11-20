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
	
	Flight createFlight(Flight flight) throws InvalidRouteException, FlightNumberExistsException{	
		validator.validateFlight(flight);	
		return crud.createFlight(flight);
	}
	
	List<Flight> findAll(){
		return crud.findAllOrderedByNumber();
	}

	public Flight deleteFlight(Flight flight) {
		
		log.info("delete() - Deleting " + flight.toString());
		
		Flight deletedFlight = null;
		
		if(flight.getId() != null){
			deletedFlight = crud.deleteFlight(flight);
		}else{
			log.info("delete() - No ID was found so can't Delete.");
		}
		
		return deletedFlight;
	}
	
	public Flight findById(long id){
		return crud.findFlightById(id);
	}
}
