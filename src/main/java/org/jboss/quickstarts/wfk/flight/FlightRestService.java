package org.jboss.quickstarts.wfk.flight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

import org.jboss.quickstarts.wfk.user.UserService;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
    
    @GET
    @ApiOperation(value = "Fetch all flights", notes = "Returns a JSON array of all stored flight objects.")
    public Response getFlights(){
    	List<Flight> flights = service.findAll();
    	flights.retainAll(service.findAll());
		return Response.ok(flights).build();
    }
    
    @POST
    @ApiOperation(value = "Create a flight")
    public Response createFlight(Flight flight){
    	Flight createdFlight;
    	try{
    		createdFlight = service.createFlight(flight);
    	}catch(ConstraintViolationException e){
    		Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
    	}catch(Exception e){
    		throw new RestServiceException(e.getMessage());
    	}
    	return Response.accepted(createdFlight).build();
    }
    
    @DELETE
    @ApiOperation(value = "Delete a flight")
    public Response deleteFlight(@ApiParam(value = "Id of user to be deleted", allowableValues = "range[0, infinity]", required = true)
	@PathParam("id") 
	long id){
    	Flight deletedFlight = service.findById(id);
    	service.deleteBooking(deletedFlight);
    	return Response.accepted(deletedFlight).build();
    }
}