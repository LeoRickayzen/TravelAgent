package org.jboss.quickstarts.wfk.user;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.user.User;
import org.jboss.quickstarts.wfk.user.UserRepository;

public class UserValidator {
	
	@Inject
    private Validator validator;

    @Inject
    private UserRepository crud;
	
    void validateUser(User user) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(user.getEmail(), user.getId())) {
            throw new UniqueEmailException("Unique Email Violation");
        }
    }
    
	boolean emailAlreadyExists(String email, Long id) {
        User user = null;
        User contactWithID = null;
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
