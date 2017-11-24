package org.jboss.quickstarts.wfk.flight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Provides RESTful functionality for flight crud operations
 * 
 * @author Leo Rickayzen
 *
 */
@Path("/flights")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/flights", description = "Operations about flights")
@Stateless
public class FlightRestService {
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private FlightService service;
    
    /**
     * fetch all the flights from the database
     * 
     * @return a response consisting of all flights in the database
     */
    @GET
    @ApiOperation(value = "Fetch all flights", notes = "Returns a JSON array of all stored flight objects.")
    public Response getFlights(){
    	List<Flight> flights = service.findAll();
    	flights.retainAll(service.findAll());
		return Response.ok(flights).build();
    }
    
    /**
     * Create a flight and store it in the database
     * 
     * @param the flight to be created
     * @return the created flight object
     */
    @POST
    @ApiOperation(value = "Create a flight")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Flight created successfully."),
            @ApiResponse(code = 204, message = "Flight id provided when it should be automatically generated"),
            @ApiResponse(code = 400, message = "Invalid Flight supplied in request body"),
            @ApiResponse(code = 409, message = "Flight supplied in request body conflicts with an existing flight"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createFlight(Flight flight){
    	/*
    	 * tried to catch ConstraintViolationException using
    	 *    	 
    	 *    	try{
    	 *    		createdFlight = service.create(flight);
    	 *        	return Response.status(Status.CREATED).entity(createdFlight).build();
    	 *    	}catch(ConstraintViolationException e){
    	 *    		log.info("woop woop");
    	 *    		Map<String, String> responseObj = new HashMap<>();
    	 *
    	 *            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
    	 *                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
    	 *            }
    	 *            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
    	 *    	}
    	 * 
    	 * but it doesn't work atall and no matter what I try it doesn't catch the exception,
    	 * so using a messy work around to validate the fields in the flight entity
    	 */
    	Flight createdFlight;
    	
    	roughValidate(flight);
    	
    	try{
    		
    		createdFlight = service.create(flight);
        	
    		return Response.status(Status.CREATED).entity(createdFlight).build();
    	}catch(InvalidRouteException e){
    		throw new RestServiceException("Bad request, departure and arrival destination must be different", Response.Status.BAD_REQUEST, e);
    	}catch(FlightNumberExistsException e){
    		throw new RestServiceException("Flight number already exists", Response.Status.CONFLICT, e);
    	}catch(EJBTransactionRolledbackException e){
    		log.info("caught exception hard");
    		throw new RestServiceException(e);
    	}catch(Exception e){
    		log.info("caught exception hard");
    		e.printStackTrace();
    		throw new RestServiceException(e);
    	}
    }
    
    /**
     * deleted a flight from the database
     * 
     * @param id of the flight to be deleted
     * @return the deleted flight
     */
    @DELETE
    @ApiOperation(value = "Delete a flight")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Flight deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid Flight id supplied"),
            @ApiResponse(code = 404, message = "Flight with id not found"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    @Path("/{id}")
    public Response deleteFlight(@ApiParam(value = "Id of flight to be deleted", allowableValues = "range[0, infinity]", required = true)
	@PathParam("id") 
	long id){
    	Flight flight = service.findById(id);
    	if(flight == null){
    		throw new RestServiceException("No Contact with the id " + id + " was found!", Response.Status.NOT_FOUND);
		}else{
			try {
				service.deleteFlight(flight);
				return Response.noContent().build();
			} catch (Exception e) {
				throw new RestServiceException(e);
			}
		}
    }
    
    /**
     * utility method which manually validates flight,
     * explained in comments on line 85
     * 
     * @param the flight to be validated
     */
    private void roughValidate(Flight flight){
    	Pattern locationsPattern = Pattern.compile("^[A-Z]{3}$");
		Pattern flightNumberPattern = Pattern.compile("^([a-z]|\\d){5}$");
		
		Matcher dep = locationsPattern.matcher(flight.getDeparture());
		Matcher arr = locationsPattern.matcher(flight.getArrival());
		Matcher flightNumber = flightNumberPattern.matcher(flight.getFlightNumber());
		
		if(!dep.matches()){
			throw new RestServiceException("Bad Request, invalid departure destination, must be an upper case string length 3", Response.Status.BAD_REQUEST);
		}
		if(!arr.matches()){
			throw new RestServiceException("Bad Request, invalid arrival destination, must be an upper case string length 3", Response.Status.BAD_REQUEST);
    	}
		if(!flightNumber.matches()){
			throw new RestServiceException("Bad Request, invalid flight number destination, must be a 5 letter alphanumeric string", Response.Status.BAD_REQUEST);
    	}
    }
}