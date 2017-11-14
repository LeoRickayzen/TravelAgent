package org.jboss.quickstarts.wfk.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.flight.FlightService;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/bookings", description = "Operations about bookings")
@Stateless
public class BookingRestService {
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private BookingService service;
    
    @GET
    @ApiOperation(value = "Fetch all bookings", notes = "Returns a JSON array of all stored booking objects.")
    public Response getBookings(){
    	List<Booking> bookings = service.findAll();
    	bookings.retainAll(service.findAll());
    	return Response.ok(bookings).build();
    }
    
    @POST
    @ApiOperation(value = "Create a booking")
    public Response createBookings(Booking booking){
    	try{
        	service.createBooking(booking);
    	}catch(ConstraintViolationException e){
    		Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
    	}catch(Exception e){
    		throw new RestServiceException(e.getMessage());
    	}
    	return Response.accepted(booking).build();
    }
    
    @DELETE
    @ApiOperation(value = "delete a booking")
    public Response deleteBooking(Booking booking){
    	service.deleteBooking(booking);
    	return Response.accepted(booking).build();
    }
}