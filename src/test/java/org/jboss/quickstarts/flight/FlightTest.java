package org.jboss.quickstarts.flight;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.quickstarts.wfk.customer.CustomerRestService;
import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightRestService;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FlightTest {
	@Deployment
    public static Archive<?> createTestArchive() {
        // This is currently not well tested. If you run into issues, comment line 67 (the contents of 'resolve') and
        // uncomment 65. This will build our war with all dependencies instead.
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
//                .importRuntimeAndTestDependencies()
                .resolve(
                        "io.swagger:swagger-jaxrs:1.5.15"
        ).withTransitivity().asFile();

        return ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, "org.jboss.quickstarts.wfk")
                .addAsLibraries(libs)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("arquillian-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	
	@Inject
    FlightRestService flightRestService;
	
	@Inject EntityManager em;

    @Inject
    @Named("logger") Logger log;

    //Set millis 498484800000 from 1985-10-10T12:00:00.000Z
    private Date date = new Date(498484800000L);
    
    @Test
    @InSequence(1)
    public void testSuccessfulCreation(){
    	Flight flight = new Flight();
    	flight.setArrival("STR");
    	flight.setDeparture("ING");
    	flight.setFlightNumber("str1g");
    	Response response = flightRestService.createFlight(flight);
    	assertEquals(201, response.getStatus());
    }
    
    @Test
    @InSequence(2)
    public void testInvalidLocations(){
    	Flight flight = new Flight();
    	flight.setArrival("str");
    	flight.setDeparture("ING");
    	flight.setFlightNumber("str2g");
    	try{
        	flightRestService.createFlight(flight);
    	}catch(RestServiceException e){
    		log.info("exception caugh, response status: " + e.getStatus().getStatusCode());
        	assertEquals(400, e.getStatus().getStatusCode());
    	}catch(Exception e){
        	fail("should throw rest exception");
    	}
    }
    
    @Test
    @InSequence(3)
    public void testInvalidNumber(){
    	Flight flight = new Flight();
    	flight.setArrival("STR");
    	flight.setDeparture("ING");
    	flight.setFlightNumber("str3ng");
    	try{
        	flightRestService.createFlight(flight);
        	fail("should throw rest exception");
    	}catch(RestServiceException e){
    		log.info("exception caught, response status: " + e.getStatus().getStatusCode());
        	assertEquals(400, e.getStatus().getStatusCode());
    	}catch(Exception e){
        	fail("should throw rest exception");
    	}
    }
    
    @Test
    @InSequence(4)
    public void  testConflictingFlight(){
    	Flight flight = new Flight();
    	flight.setArrival("STR");
    	flight.setDeparture("ING");
    	flight.setFlightNumber("str4g");
    	try{
        	flightRestService.createFlight(flight);
        	flightRestService.createFlight(flight);
        	fail("should throw rest exception");
    	}catch(RestServiceException e){
    		log.info("exception caught, response status: " + e.getStatus().getStatusCode());
        	assertEquals(409, e.getStatus().getStatusCode());
    	}catch(Exception e){
        	fail("should throw rest exception");
    	}
    }
    
    @Test
    @InSequence(5)
    public void testDelete(){
    	Response response = flightRestService.deleteFlight(1);
    	assertEquals(204, response.getStatus());
    }
    
    @Test
    @InSequence(6)
    public void testDeleteWrongId(){
    	try{
    		flightRestService.deleteFlight(100);
    	}catch(RestServiceException e){
    		assertEquals(404, e.getStatus().getStatusCode());
    	}
    }
    
    @Test
    @InSequence(7)
    public void testGetAllFlights(){
    	assertEquals(200, flightRestService.getFlights().getStatus());
    }
}
