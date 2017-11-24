package org.jboss.quickstarts.wfk.flight;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * Service level functionality for flights
 * 
 * @author Leo Rickayzen
 */
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
	
	/**
	 * validate and persist a flight
	 * 
	 * @param flight to create
	 * @return flight created including the id
	 * @throws InvalidRouteException when the destination and departure are the same
	 * @throws FlightNumberExistsException when the flight number already exists
	 * @throws ConstraintViolationException 
	 * @throws ValidationException when for instance the flight number is a character too long
	 * @throws Exception
	 */
	public Flight create(Flight flight) throws InvalidRouteException, FlightNumberExistsException, ConstraintViolationException, ValidationException, Exception{	
		validator.validateFlight(flight);	
		return crud.createFlight(flight);
	}
	
	/**
	 * get all flight records
	 * 
	 * @return a list of all flights
	 */
	List<Flight> findAll(){
		return crud.findAllOrderedByNumber();
	}
	
	/**
	 * delete the flight from a database
	 * 
	 * @param flight to delete
	 * @return the deleted flight
	 */
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
	
	/**
	 * find a flight by it's id
	 * 
	 * @param id
	 * @return the flight given the id
	 */
	public Flight findById(long id){
		return crud.findFlightById(id);
	}
}
