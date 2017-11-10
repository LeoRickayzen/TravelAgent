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
	
	Flight createFlight(Flight flight){
		return crud.createFlight(flight);
	}
	
	List<Flight> findAll(){
		return crud.findAllOrderedByNumber();
	}
}
