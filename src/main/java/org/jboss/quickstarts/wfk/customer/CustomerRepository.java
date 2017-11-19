package org.jboss.quickstarts.wfk.customer;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.contact.Contact;

public class CustomerRepository {
	
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;
    
    public List<Customer> findAllOrderedByName(){
    	TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return query.getResultList();
    }
    
    public Customer findById(Long id){
    	return em.find(Customer.class, id);
    }
    
    public Customer findByEmail(String email) {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class).setParameter("email", email);
        return query.getSingleResult();
    }
    
    public Customer create(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
        log.info("UserRepository.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());

        // Write the contact to the database.
        em.persist(customer);

        return customer;
    }
    
    public Customer update(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
        log.info("UserRepository.update() - Updating " + customer.getFirstName() + " " + customer.getLastName());

        // Either update the contact or add it if it can't be found.
        em.merge(customer);

        return customer;
    }
    
    public Customer delete(Customer customer) throws Exception{
        log.info("UserRepository.delete() - Deleting " + customer.getId());

        if (customer.getId() != null) {
            
            em.remove(em.merge(customer));

        } else {
            log.info("UserRepository.delete() - No ID was found so can't Delete.");
            throw new Exception("No user with that ID");
        }

        return customer;
    }
	
}
