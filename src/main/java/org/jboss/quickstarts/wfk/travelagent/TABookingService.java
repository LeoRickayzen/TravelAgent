package org.jboss.quickstarts.wfk.travelagent;

import java.util.Date;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.booking.InvalidCredentialsException;
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
	
	private final Long agentId = new Long("3");
	
	@Inject
    private @Named("logger") Logger log;
	
	public TABookingService(){
		client = new ResteasyClientBuilder().build();
	}
	
	public TaxiBooking makeTaxiBooking(TABooking booking){
		Long taxiId = booking.getTaxiId();
		
		TaxiBooking taxiBooking = new TaxiBooking();
		
		taxiBooking.setTaxiID(taxiId);
		taxiBooking.setCustomerID(agentId);
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
			
			TaxiBooking returnedBooking = (TaxiBooking)response.getEntity();
			return returnedBooking;
		}catch(ClientErrorException e){
			return null;
		}
	}
}