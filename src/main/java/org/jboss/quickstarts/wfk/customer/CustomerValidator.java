package org.jboss.quickstarts.wfk.customer;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRepository;

public class CustomerValidator {
	
	@Inject
    private Validator validator;

    @Inject
    private CustomerRepository crud;
	
    void validateUser(Customer customer) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(customer.getEmail(), customer.getId())) {
            throw new UniqueEmailException("Unique Email Violation");
        }
    }
    
	boolean emailAlreadyExists(String email, Long id) {
        Customer user = null;
        Customer contactWithID = null;
        try {
            user = crud.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }

        if (user != null && id != null) {
            try {
                contactWithID = crud.findById(id);
                if (contactWithID != null && contactWithID.getEmail().equals(email)) {
                    user = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return user != null;
    }
}
