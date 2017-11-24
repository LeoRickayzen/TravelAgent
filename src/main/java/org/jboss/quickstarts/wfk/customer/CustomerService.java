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

/**
 * Provides service level functionality for customer entities
 * 
 * @author Leo Rickayzen
 */
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
	 
	 /**
	  * returns a list of all customers ordered by name
	  * 
	  * @return list of all customers
	  */
	 List<Customer> findAll(){
	 	return crud.findAllOrderedByName();
	 }
	 
	 /**
	  * Find a customer by their email
	  * 
	  * @param email the email to find the customer by
	  * @return customer with corresponding email
	  */
	 Customer findByEmail(String email) {
	     return crud.findByEmail(email);
	 }
	 
	 /**
	  * find a customer by their id
	  * 
	  * @param id of customer to find
	  * @return customer with corresponding id
	  */
	 public Customer findById(long id){
	 	return crud.findById(id);
	 }
	 
	 /**
	  * validate and persist a customer
	  * 
	  * @param customer to create
	  * @return
	  * @throws ConstraintViolationException when customer conflicts with another customer
	  * @throws ValidationException when the customer provided conflicts with the validation rules defined in the entity
	  * @throws Exception
	  */
	 public Customer create(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
	     log.info("ContactService.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());
	     
	     validator.validateUser(customer);

	     return crud.create(customer);
	 }
	 
	 /**
	  * delete a customer from the database
	  * 
	  * @param customer to delete
	  * @return the deleted customer
	  * @throws Exception
	  */
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
