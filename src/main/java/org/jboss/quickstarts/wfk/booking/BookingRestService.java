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
import javax.ws.rs.core.Response.Status;

import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.flight.FlightService;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
    	List<FlightBooking> bookings = service.findAll();
    	bookings.retainAll(service.findAll());
    	return Response.ok(bookings).build();
    }
    
    @POST
    @ApiOperation(value = "Create a booking")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking created successfully."),
            @ApiResponse(code = 204, message = "Booking id provided when it should be automatically generated"),
            @ApiResponse(code = 400, message = "Invalid Booking supplied in request body"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createBookings(FlightBooking booking){
    	try{
        	service.createBooking(booking);
    	}catch(ConstraintViolationException e){
    		Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
    	}catch(InvalidCredentialsException e){
    		throw new RestServiceException(e.getMessage(), Response.Status.BAD_REQUEST, e);
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new RestServiceException(e.getMessage());
    	}
    	return Response.status(Status.CREATED).entity(booking).build();
    }
    
    @DELETE
    @ApiOperation(value = "delete a booking")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Booking deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid booking id supplied"),
            @ApiResponse(code = 404, message = "Booking with id not found"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response deleteBooking(FlightBooking booking){
    	FlightBooking b = service.findById(booking.getBookingNumber());
    	if(b == null){
    		throw new RestServiceException("No booking with the id " + booking.getBookingNumber() + " was found!", Response.Status.NOT_FOUND);
		}else{
			try {
				service.deleteBooking(b);
				return Response.noContent().build();
			} catch (Exception e) {
				throw new RestServiceException(e);
			}
		}
    }
}