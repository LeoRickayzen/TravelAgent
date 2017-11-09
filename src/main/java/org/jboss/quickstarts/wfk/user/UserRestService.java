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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Operations about users")
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
            
            throw new RestServiceException("Bad Request" , responseObj, Response.Status.BAD_REQUEST, ce);
		
    	} catch (ValidationException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    @DELETE
    @ApiOperation(value = "delete a user from the database")
    @Path("/{id}")
    public Response deleteUser(
    		@ApiParam(value = "Id of user to be deleted", allowableValues = "range[0, infinity]", required = true)
    		@PathParam("id") 
    		long id){
    	User user = service.findById(id);
    	try {
			service.delete(user);
			return Response.ok(user).build();
		} catch (Exception e) {
			return Response.notModified("no user with that id").build();
		}
    }
    
    @GET
    @ApiOperation(value = "get a user by it's id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User retreived successfully"),
            @ApiResponse(code = 204, message = "No user with that ID")
    })
    @Path("/{id}")
    public Response findUser(@ApiParam(value = "Id of user to be fetched", allowableValues = "range[0, infinity]", required = true)
    		@PathParam("id") long id){
    	User user = service.findById(id);
    	if(user == null){
    		return Response.noContent().build();
    	}else{
    		return Response.ok(user).build();
    	}
    }
}