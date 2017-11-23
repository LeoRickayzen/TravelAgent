package org.jboss.quickstarts.user;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.quickstarts.wfk.contact.ContactRestService;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRestService;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import junit.framework.Assert;

@RunWith(Arquillian.class)
public class CustomerTest {
	
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
    CustomerRestService userRestService;
	
	@Inject EntityManager em;

    @Inject
    @Named("logger") Logger log;

    //Set millis 498484800000 from 1985-10-10T12:00:00.000Z
    private Date date = new Date(498484800000L);
    
    @Test
    @InSequence(1)
    public void testRegistration(){
    	Customer user = createUser("leo", "rick", "lr1@ncl.com", "01231231230", date);
    	Response response = userRestService.createCustomer(user);
    	
    	assertEquals(201, response.getStatus());
    }
    
    @Test
    @InSequence(2)
    public void testBadEmailRegistration(){
    	Customer user = createUser("leo", "rick", "lr1", "01231231230", date);
    	try{
        	userRestService.createCustomer(user);
        	fail("should throw RestServiceException");
    	}catch(RestServiceException e){
        	assertEquals(400, e.getStatus().getStatusCode());
    	}
    }
    
    @Test
    @InSequence(3)
    public void testBadNumberRegistration(){
    	Customer user = createUser("leo", "rick", "lr1@ncl.com", "012312331230", date);
    	try{
        	userRestService.createCustomer(user);
        	fail("should throw RestServiceException");
    	}catch(RestServiceException e){
        	assertEquals(400, e.getStatus().getStatusCode());
    	}
    }
    
    
    @Test
    @InSequence(4)
    public void testDuplicateEmailRegistration(){
    	Customer user = createUser("leo", "rick", "lr1@ncl.com", "01231231230", date);
    	try{
    		userRestService.createCustomer(user);
        	userRestService.createCustomer(user);
        	fail("should throw RestServiceException");
    	}catch(RestServiceException e){
        	assertEquals(409, e.getStatus().getStatusCode());
    	}
    }
    
    @SuppressWarnings("unchecked")
	@Test
    @InSequence(5)
    public void testDeletion(){
    	
    	List<Customer> customersInitial = (List<Customer>) userRestService.getAllCustomers().getEntity();
    	    	
    	int initialNumber = customersInitial.size();
    	
    	Customer user = createUser("leo", "r", "lr2@ncl.com", "01231231230", date);
    	Response response1 = userRestService.createCustomer(user);
    	
    	Customer createdUser = (Customer)response1.getEntity();
    	
    	Response response2 = userRestService.deleteCustomer(createdUser.getId());
    	
    	List<Customer> customersFinal = (List<Customer>) userRestService.getAllCustomers().getEntity();
    	
    	int finalNumber = customersFinal.size();
    	
    	assertTrue((finalNumber == initialNumber) && customersInitial.containsAll(customersFinal));
    	
    	assertEquals(204, response2.getStatus());
    }
    
    @Test
    @InSequence(6)
    public void testGetById(){
    	Customer user = createUser("leo", "rick", "lr4@ncl.com", "01231231230", date);
    	Response response = userRestService.createCustomer(user);
    	Customer returnedCustomer = (Customer)response.getEntity();
    	Response response2 = userRestService.findCustomer(returnedCustomer.getId());
    	Customer returnedCustomer2 = (Customer)response2.getEntity();
    	assertTrue(returnedCustomer2.equals(returnedCustomer));
    }
    
    @Test
    @InSequence(7)
    public void testGetByInvalidId(){
    	Response response = userRestService.findCustomer(2);
        assertEquals(204, response.getStatus());
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
