package org.jboss.quickstarts.wfk.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Operations about contacts")
@Stateless
public class UserRestService {
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private UserService service;
    
    @GET
    @ApiOperation(value = "Fetch all users", notes = "Returns a JSON array of all stored user objects.")
    public Response getAllUsers(){
    	List<User> users;
    	users = service.findAll();
    	users.retainAll(service.findAll());
		return Response.ok(users).build();
    }
    
    @SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new User to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully."),
            @ApiResponse(code = 400, message = "Invalid User supplied in request body"),
            @ApiResponse(code = 409, message = "User supplied in request body conflicts with an existing user"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createUser(User user){
    	
    	if (user == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
    	
    	Response.ResponseBuilder builder;
    	
    	try {
			
    		service.create(user);

            builder = Response.status(Response.Status.CREATED).entity(user);
		
    	} catch (ConstraintViolationException ce) {
    		//bean validation issue
    		Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            
            ce.printStackTrace();
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
		
    	} catch (ValidationException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    
    public Response deleteUser(long id){
    	return null;
    }
    
    public Response updateUser(){
    	return null;
    }
    
    public Response findUser(){
    	return null;
    }
}