package org.jboss.quickstarts.wfk.customer;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.area.Area;
import org.jboss.quickstarts.wfk.area.AreaService;
import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.quickstarts.wfk.contact.Contact;
import org.jboss.quickstarts.wfk.customer.CustomerRepository;
import org.jboss.quickstarts.wfk.customer.CustomerValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class CustomerService {

	 @Inject
	 private @Named("logger") Logger log;

	 @Inject
	 private CustomerValidator validator;

	 @Inject
	 private CustomerRepository crud;

	 private ResteasyClient client;
	 
	 public CustomerService() {
	     client = new ResteasyClientBuilder().build();
	 }
	 
	 List<Customer> findAll(){
	 	return crud.findAllOrderedByName();
	 }
	 
	 Customer findByEmail(String email) {
	     return crud.findByEmail(email);
	 }
	 
	 public Customer findById(long id){
	 	return crud.findById(id);
	 }
	 
	 public Customer create(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
	     log.info("ContactService.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());
	     
	     validator.validateUser(customer);

	     return crud.create(customer);
	 }
	 
	 Customer update(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
	     log.info("ContactService.update() - Updating " + customer.getFirstName() + " " + customer.getLastName());
	     
	     // Check to make sure the data fits with the parameters in the Contact model and passes validation.
	     validator.validateUser(customer);

	     // Either update the contact or add it if it can't be found.
	     return crud.update(customer);
	 }
	 
	 Customer delete(Customer customer) throws Exception{
		 log.info("delete() - Deleting " + customer.toString());

	     Customer deletedCustomer = null;

	     if (customer.getId() != null) {
	         deletedCustomer = crud.delete(customer);
	     } else {
	         log.info("delete() - No ID was found so can't Delete.");
	     }

	     return deletedCustomer;
	 }
}
