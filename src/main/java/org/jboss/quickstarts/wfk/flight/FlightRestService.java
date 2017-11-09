package org.jboss.quickstarts.wfk.flight;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	
}
