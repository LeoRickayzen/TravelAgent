package org.jboss.quickstarts.wfk.user;

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
import org.jboss.quickstarts.wfk.user.UserRepository;
import org.jboss.quickstarts.wfk.user.UserValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class UserService {

	 @Inject
	    private @Named("logger") Logger log;

	    @Inject
	    private UserValidator validator;

	    @Inject
	    private UserRepository crud;

	    private ResteasyClient client;
	    
	    public UserService() {
	        client = new ResteasyClientBuilder().build();
	    }
	    
	    List<User> findAll(){
	    	return crud.findAllOrderedByName();
	    }
	    
	    User findByEmail(String email) {
	        return crud.findByEmail(email);
	    }
	    
	    User findById(long id){
	    	return crud.findById(id);
	    }
	    
	    User create(User user) throws ConstraintViolationException, ValidationException, Exception {
	        log.info("ContactService.create() - Creating " + user.getFirstName() + " " + user.getLastName());
	        
	        validator.validateUser(user);

	        return crud.create(user);
	    }
	    
	    User update(User user) throws ConstraintViolationException, ValidationException, Exception {
	        log.info("ContactService.update() - Updating " + user.getFirstName() + " " + user.getLastName());
	        
	        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
	        validator.validateUser(user);

	        // Either update the contact or add it if it can't be found.
	        return crud.update(user);
	    }
	    
	    void delete(User user) throws Exception{
	    	crud.delete(user);
	    }
}
