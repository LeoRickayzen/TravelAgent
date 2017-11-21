package org.jboss.quickstarts.wfk.travelagent;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.InvalidCredentialsException;
import org.jboss.quickstarts.wfk.flight.FlightService;
import org.jboss.quickstarts.wfk.hotel.HotelBooking;
import org.jboss.quickstarts.wfk.taxi.TaxiBooking;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;

@Path("/travelagent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/travelagent", description = "Operations about travel agent bookings")
@Stateless
public class TABookingRestService {
	
	@Inject
    private @Named("logger") Logger log;
	
	@Inject
	TABookingService service;
	
	@POST
	public Response createTABooking(TABooking booking){
		
		TaxiBooking taxiBooking = null;
		HotelBooking hotelBooking = null;
		Booking flightBooking = null;
		
		try{
			taxiBooking = service.makeTaxiBooking(booking);
			log.info("taxi booking created: " + taxiBooking.toString());
		}catch(InvalidCredentialsException e){
			throw new RestServiceException("bad request: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
		}catch(Exception e){
			throw new RestServiceException(e);
		}
		
		try{
			hotelBooking = service.makeHotelBooking(booking);
			log.info("hotel booking created: " + hotelBooking.toString());
		}catch(InvalidCredentialsException e){
			if(taxiBooking != null && taxiBooking.getId() != null){
				service.rollBackTaxi(taxiBooking.getId());
				throw new RestServiceException("bad request: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
			}else{
				throw new RestServiceException("bad request: " + e.getMessage() + ". And no id received", Response.Status.BAD_REQUEST, e);
			}
		}catch(Exception e){
			throw new RestServiceException(e);
		}
		
		try{
			flightBooking = service.makeFlightBooking(booking);
		}catch(InvalidCredentialsException e){
			if(taxiBooking != null && taxiBooking.getId() != null && hotelBooking != null && hotelBooking.getId() != null){
				service.rollBackHotel(hotelBooking.getId());
				service.rollBackTaxi(taxiBooking.getId());
				throw new RestServiceException("bad request: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
			}else{
				throw new RestServiceException("bad request: " + e.getMessage() + ". And no id received", Response.Status.BAD_REQUEST, e);
			}
		}
		return Response.status(Status.CREATED).entity(booking).build();
	}
}
