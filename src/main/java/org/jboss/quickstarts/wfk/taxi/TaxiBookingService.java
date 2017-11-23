package org.jboss.quickstarts.wfk.taxi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
public interface TaxiBookingService{
	
	@POST
	Response makeBooking(TaxiBooking booking);
	
	@DELETE
	@Path("/{id:[0-9]+}")
	Response deleteBooking(@PathParam("id") Long id);
}
