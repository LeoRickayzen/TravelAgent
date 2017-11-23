package org.jboss.quickstarts.wfk.hotel;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
public interface HotelBookingService {
	@POST
	Response makeBooking(HotelBooking booking);
	
	@DELETE
	@Path("/{id:[0-9]+}")
	Response deleteBooking(@PathParam("id")  Long id);
}
