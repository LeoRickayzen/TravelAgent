package org.jboss.quickstarts.wfk.travelagent;

import java.util.Date;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.booking.InvalidCredentialsException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightService;
import org.jboss.quickstarts.wfk.hotel.HotelBooking;
import org.jboss.quickstarts.wfk.hotel.HotelBookingService;
import org.jboss.quickstarts.wfk.taxi.TaxiBooking;
import org.jboss.quickstarts.wfk.taxi.TaxiBookingService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

@Dependent
public class TABookingService {
	@Inject
	private TABookingRepository crud;
	
	private ResteasyClient client;
	
	private final Long agentIdTaxi = new Long("10001");
	private final Long agentIdHotel = new Long("10001");
	private final Long agentIdFlight = new Long("2");
	
	@Inject
    private @Named("logger") Logger log;
	
	@Inject
	private BookingService flightBookingService;
		
	public HotelBooking makeHotelBooking(TABooking booking) throws Exception{
		
		client = new ResteasyClientBuilder().build();
		
		Long hotelId = booking.getHotelId();
		
		HotelBooking hotelBooking = new HotelBooking();
		
		hotelBooking.setHotelId(hotelId);
		hotelBooking.setCustomerId(agentIdHotel);;
		hotelBooking.setDate(booking.getTime());
		log.info("booking: " + hotelBooking.toString());
		
		ResteasyWebTarget hotelTarget = client.target("http://api-deployment-csc8104-130167853.7e14.starter-us-west-2.openshiftapps.com/api");
		
		HotelBookingService service = hotelTarget.proxy(HotelBookingService.class);
		
		try{
			Response response = service.makeBooking(hotelBooking);
			
			log.info("code: " + response.getStatus());
			
			if(response.getStatus() == 400){
				throw new InvalidCredentialsException("Invalid input provided to the hotel booking");
			}
			
			if(response.getStatus() == 409){
				throw new InvalidCredentialsException("duplicate hotel booking provided");
			}
			
			if(response.getStatus() != 201){
				throw new Exception("Unkown response code: " + response.getStatus());
			}
			
			HotelBooking returnedBooking = response.readEntity(HotelBooking.class);
			
			client.close();
			
			return returnedBooking;
		}catch(ClientErrorException e){
			return null;
		}
	}
	
	public TaxiBooking makeTaxiBooking(TABooking booking) throws Exception{
		
		client = new ResteasyClientBuilder().build();
		
		Long taxiId = booking.getTaxiId();
		
		TaxiBooking taxiBooking = new TaxiBooking();
		
		taxiBooking.setTaxiID(taxiId);
		taxiBooking.setCustomerID(agentIdTaxi);
		taxiBooking.setBookingDate(booking.getTime());
		log.info("booking: " + taxiBooking.toString());
		
		ResteasyWebTarget taxiTarget = client.target("http://api-deployment-csc8104-130277853.7e14.starter-us-west-2.openshiftapps.com/api");
		
		TaxiBookingService service = taxiTarget.proxy(TaxiBookingService.class);
		
		try{
			Response response = service.makeBooking(taxiBooking);
			
			log.info("code: " + response.getStatus());
			
			if(response.getStatus() == 400){
				throw new InvalidCredentialsException("Invalid input provided to the taxi booking");
			}
			
			if(response.getStatus() == 409){
				throw new InvalidCredentialsException("duplicate taxi booking provided");
			}
			
			if(response.getStatus() != 201){
				throw new Exception("Unkown response code: " + response.getStatus());
			}
			
			TaxiBooking returnedBooking = response.readEntity(TaxiBooking.class);
			
			client.close();
			
			return returnedBooking;
		}catch(ClientErrorException e){
			return null;
		}
	}

	public Booking makeFlightBooking(TABooking booking){
		Booking flightBooking = new Booking();
		
		Customer customer = new Customer();
		customer.setId(agentIdFlight);
		
		Flight flight = new Flight();
		flight.setId(booking.getFlightId());
		
		flightBooking.setCustomer(customer);
		flightBooking.setFlightBooked(flight);
		flightBooking.setTime(booking.getTime());
		
		flightBookingService.createBooking(flightBooking);
		return flightBooking;
	}
	
	public TABooking storeTABooking(TABooking booking){
		return crud.createBooking(booking);
	}
	
	public void rollBackHotel(Long id){
		
	}
	
	public void rollBackTaxi(Long id){
		
	}
	
	public void rollBackFlight(Long id){
		
	}
}