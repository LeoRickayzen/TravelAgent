package org.jboss.quickstarts.TA;
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
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRestService;
import org.jboss.quickstarts.wfk.flight.Flight;
import org.jboss.quickstarts.wfk.flight.FlightRestService;
import org.jboss.quickstarts.wfk.travelagent.TABooking;
import org.jboss.quickstarts.wfk.travelagent.TABookingRestService;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TATest {
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
	
	@Inject
	TABookingRestService tabookingRestService;
	
	@Inject EntityManager em;

    @Inject
    @Named("logger") Logger log;

    //Set millis 498484800000 from 1985-10-10T12:00:00.000Z
    private Date date = new Date(1484179200000L);
    
    Customer user = createUser("leo", "rick", "lr1@ncl.com", "01231231230", date);
	Flight flight = createFlight("STR", "ING", "str1n");
	
	@Test
	@InSequence(1)
	public void validCreationTest(){
		flightRestService.createFlight(flight);
		customerRestService.createCustomer(user);
		TABooking tabooking = new TABooking();
		user.setId(new Long(2));
		tabooking.setCustomer(user);
		flight.setId(new Long(1));
		tabooking.setFlightId(flight.getId());
		tabooking.setHotelId(new Long(10001));
		tabooking.setTaxiId(new Long(20001));
		tabooking.setTime(date);
		assertEquals(201, tabookingRestService.createTABooking(tabooking).getStatus());
	}
	
	@Test
	@InSequence(2)
	public void testInvalidTaxi(){
		TABooking tabooking = new TABooking();
		tabooking.setCustomer(user);
		tabooking.setFlightId(flight.getId());
		tabooking.setHotelId(new Long(10001));
		tabooking.setTaxiId(new Long(1));
		tabooking.setTime(date);
		assertEquals(400, tabookingRestService.createTABooking(tabooking).getStatus());
	}
	
	@Test
	@InSequence(3)
	public void testInvalidHotel(){
		TABooking tabooking = new TABooking();
		tabooking.setCustomer(user);
		tabooking.setFlightId(flight.getId());
		tabooking.setHotelId(new Long(11));
		tabooking.setTaxiId(new Long(20001));
		tabooking.setTime(date);
		assertEquals(400, tabookingRestService.createTABooking(tabooking).getStatus());
	}
	
	@Test
	@InSequence(4)
	public void testInvalidFlight(){
		TABooking tabooking = new TABooking();
		tabooking.setCustomer(user);
		tabooking.setFlightId(new Long(13213));
		tabooking.setHotelId(new Long(10001));
		tabooking.setTaxiId(new Long(20001));
		tabooking.setTime(date);
		assertEquals(400, tabookingRestService.createTABooking(tabooking).getStatus());
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
