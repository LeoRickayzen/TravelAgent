package org.jboss.quickstarts.wfk.travelagent;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
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

import org.jboss.quickstarts.wfk.booking.FlightBooking;
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
	
	private TaxiBooking makeTaxiBooking(TABooking booking){
		TaxiBooking taxiBooking;
		try{
			taxiBooking = service.makeTaxiBooking(booking);
			booking.setTaxiBookingId(taxiBooking.getId());
			log.info("taxi booking created: " + taxiBooking.toString());
			return taxiBooking;
		}catch(InvalidCredentialsException e){
			throw new RestServiceException("bad request: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
		}catch(Exception e){
			throw new RestServiceException(e);
		}
	}
	
	private HotelBooking makeHotelBooking(TABooking booking, TaxiBooking taxiBooking){
		HotelBooking hotelBooking;
		try{
			hotelBooking = service.makeHotelBooking(booking);
			booking.setHotelBookingId(hotelBooking.getId());
			log.info("hotel booking created: " + hotelBooking.toString());
			return hotelBooking;
		}catch(InvalidCredentialsException e){
			if(taxiBooking != null && taxiBooking.getId() != null){
				try{
					service.rollBackTaxi(taxiBooking.getId());
					throw new RestServiceException("bad request: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
				}catch(Exception ea){
					ea.printStackTrace();
					throw new RestServiceException("bad request: " + e.getMessage() + ", AND rollback failed because: " + ea.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, ea);
				}
			}else{
				throw new RestServiceException("bad request: " + e.getMessage() + ". And no id received", Response.Status.BAD_REQUEST, e);
			}
		}catch(Exception e){
			throw new RestServiceException(e);
		}
	}
	
	private FlightBooking makeFlightBooking(TABooking booking, TaxiBooking taxiBooking, HotelBooking hotelBooking){
		FlightBooking flightBooking;
		try{
			flightBooking = service.makeFlightBooking(booking);
			booking.setFlightBookingId(flightBooking.getBookingNumber());
			return flightBooking;
		}catch(InvalidCredentialsException e){
			if(taxiBooking != null && taxiBooking.getId() != null && hotelBooking != null && hotelBooking.getId() != null){
				try{
					service.rollBackHotel(hotelBooking.getId());
					service.rollBackTaxi(taxiBooking.getId());
					throw new RestServiceException("bad request: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
				}catch(Exception ea){
					ea.printStackTrace();
					throw new RestServiceException("bad request: " + e.getMessage() + ", AND rollback failed because: " + ea.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, ea);
				}
			}else{
				throw new RestServiceException("bad request: " + e.getMessage() + ". And no id received", Response.Status.BAD_REQUEST, e);
			}
		}
	}
	
	@POST
	public Response createTABooking(TABooking booking){
		
		TaxiBooking taxiBooking = makeTaxiBooking(booking);
		booking.setTaxiBookingId(taxiBooking.getId());
		HotelBooking hotelBooking = makeHotelBooking(booking, taxiBooking);
		booking.setHotelBookingId(hotelBooking.getId());
		FlightBooking flightBooking = makeFlightBooking(booking, taxiBooking, hotelBooking);
		booking.setFlightBookingId(flightBooking.getBookingNumber());
		
		TABooking tab = service.storeTABooking(booking);
		
		return Response.status(Status.CREATED).entity(tab).build();
	}
	
	@GET
	public Response getAllBookings(){
		return Response.status(Status.OK).entity(service.getAllBookings()).build();
	}
	
	@DELETE
	public Response deleteTABooking(TABooking booking){
		TABooking bookingFull = service.getFlightBooking(booking);
		if(bookingFull != null){
			service.rollBackFlight(bookingFull.getFlightBookingId());
			try{
				service.rollBackHotel(bookingFull.getHotelBookingId());
				service.rollBackTaxi(bookingFull.getTaxiBookingId());
			}catch(Exception e){
				e.printStackTrace();
				throw new RestServiceException("bad request: " + e.getMessage() + ", rollback failed", Response.Status.BAD_REQUEST, e); 
			}
			service.deleteTABooking(bookingFull);
			return Response.noContent().entity(bookingFull).build();
		}else{
			throw new RestServiceException("No booking with id " + booking.getId() + " found", Response.Status.NOT_FOUND);
		}
	}
	
	@GET
	@Path("/{id:[0-9]+}")
	public Response getBookingById(@PathParam("id")  Long id){
		TABooking booking = service.getBookingById(id);
		if(booking == null){
			throw new RestServiceException("no booking with id + " + id + " found", Response.Status.NOT_FOUND);
		}else{
			return Response.ok().entity(booking).build();
		}
	}
}