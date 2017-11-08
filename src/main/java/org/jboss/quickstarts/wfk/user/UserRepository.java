package org.jboss.quickstarts.wfk.user;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.contact.Contact;

public class UserRepository {
	
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;
    
    List<User> findAllOrderedByName(){
    	TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
        return query.getResultList();
    }
    
    User findById(Long id){
    	return em.find(User.class, id);
    }
    
    User findByEmail(String email) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_EMAIL, User.class).setParameter("email", email);
        return query.getSingleResult();
    }
    
    User create(User user) throws ConstraintViolationException, ValidationException, Exception {
        log.info("UserRepository.create() - Creating " + user.getFirstName() + " " + user.getLastName());

        // Write the contact to the database.
        em.persist(user);

        return user;
    }
    
    User update(User user) throws ConstraintViolationException, ValidationException, Exception {
        log.info("UserRepository.update() - Updating " + user.getFirstName() + " " + user.getLastName());

        // Either update the contact or add it if it can't be found.
        em.merge(user);

        return user;
    }
    
    User delete(User user) throws Exception {
        log.info("UserRepository.delete() - Deleting " + user.getId());

        if (user.getId() != null) {
            
            em.remove(em.merge(user));

        } else {
            log.info("UserRepository.delete() - No ID was found so can't Delete.");
        }

        return user;
    }
	
}
