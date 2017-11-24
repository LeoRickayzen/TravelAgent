package org.jboss.quickstarts.booking;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.quickstarts.wfk.booking.BookingRestService;
import org.jboss.quickstarts.wfk.booking.FlightBooking;
import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightRestService;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRestService;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BookingTest {
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
	
	@Inject
	BookingRestService bookingRestService;
	
	@Inject
	CustomerRestService customerRestService;
	
	@Inject EntityManager em;

    @Inject
    @Named("logger") Logger log;

    //Set millis 498484800000 from 1985-10-10T12:00:00.000Z
    private Date date = new Date(498484800000L);

	Customer user = createUser("leo", "rick", "lr1@ncl.com", "01231231230", date);
	Flight flight = createFlight("STR", "ING", "str1n");
    
    @Test
    @InSequence(1)
    public void testSuccesfulBookingCreation(){
    	customerRestService.createCustomer(user);
    	flightRestService.createFlight(flight);
    	FlightBooking booking = new FlightBooking();
    	Customer customer = new Customer();
    	customer.setId(new Long(1));
    	Flight flight = new Flight();
    	flight.setId(new Long(2));
    	booking.setCustomer(customer);
    	booking.setFlightBooked(flight);
    	booking.setTime(date);
    	assertEquals(201, bookingRestService.createBookings(booking).getStatus());
    }
    
    @Test
    @InSequence(2)
    public void testInvalidFlightBookingCreation(){
    	FlightBooking booking = new FlightBooking();
    	Customer customer = new Customer();
    	customer.setId(new Long(1));
    	Flight flight = new Flight();
    	flight.setId(new Long(3));
    	booking.setCustomer(customer);
    	booking.setFlightBooked(flight);
    	booking.setTime(date);
    	try{
    		bookingRestService.createBookings(booking);
    		fail("should return 400");
    	}catch(RestServiceException e){
    		assertEquals(400, e.getStatus().getStatusCode());
    	}
    }
    
    @Test
    @InSequence(3)
    public void testInvalidUserBookingCreation(){
    	FlightBooking booking = new FlightBooking();
    	Customer customer = new Customer();
    	customer.setId(new Long(2));
    	Flight flight = new Flight();
    	flight.setId(new Long(2));
    	booking.setCustomer(customer);
    	booking.setFlightBooked(flight);
    	booking.setTime(date);
    	try{
    		bookingRestService.createBookings(booking);
    		fail("should return 400");
    	}catch(RestServiceException e){
    		assertEquals(400, e.getStatus().getStatusCode());
    	}
    }
    
    @Test
    @InSequence(4)
    public void testSuccesfulBookingDeletion(){
    	FlightBooking booking = new FlightBooking();
    	booking.setBookingNumber(new Long(3));
    	assertEquals(204, bookingRestService.deleteBooking(booking).getStatus());
    }
    
    @Test
    @InSequence(5)
    public void testInvalidBookingDeletion(){
    	FlightBooking booking = new FlightBooking();
    	booking.setBookingNumber(new Long(4));
    	try{
    		bookingRestService.deleteBooking(booking);
    		fail("should return 400");
    	}catch(RestServiceException e){
    		assertEquals(404, e.getStatus().getStatusCode());
    	}
    }
    
    @Test
    @InSequence(6)
    public void testGetAllBookings(){
    	assertEquals(200, bookingRestService.getBookings().getStatus());
    }
    
    @Test
    @InSequence(7)
    public void testUniqueConstraint(){
    	customerRestService.createCustomer(user);
    	flightRestService.createFlight(flight);
    	FlightBooking booking = new FlightBooking();
    	Customer customer = new Customer();
    	customer.setId(new Long(1));
    	Flight flight = new Flight();
    	flight.setId(new Long(2));
    	booking.setCustomer(customer);
    	booking.setFlightBooked(flight);
    	booking.setTime(date);
    	try{
    		bookingRestService.createBookings(booking).getStatus();
    	}catch(RestServiceException e){
    		assertEquals(409, e.getStatus().getStatusCode());
    	}
    }
    
    public Flight createFlight(String arrival, String departure, String flightNumber){
    	Flight flight = new Flight();
    	flight.setArrival(arrival);
    	flight.setDeparture(departure);
    	flight.setFlightNumber(flightNumber);
    	return flight;
    }
    
    public Customer createUser(String firstname, String lastname, String email, String phonenumber, Date birthDate){
    	Customer user = new Customer();
    	user.setFirstName(firstname);
    	user.setLastName(lastname);
    	user.setEmail(email);
    	user.setPhoneNumber(phonenumber);
    	user.setBirthDate(birthDate);
    	return user;
    }
}
