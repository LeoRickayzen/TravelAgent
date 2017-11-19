package org.jboss.quickstarts.wfk.guestbooking;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/booking/guest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/guest", description = "Create and manage guest bookings")
@Stateless
public class GuestBookingRestService {
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private GuestBookingService service;
    
    @POST
    @ApiOperation(value = "Create a guest booking")
    public Response createBookings(GuestBooking booking){
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
}
