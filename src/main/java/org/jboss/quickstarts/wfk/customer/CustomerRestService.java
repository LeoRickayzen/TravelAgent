package org.jboss.quickstarts.wfk.customer;

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
import javax.ws.rs.core.Response.Status;

import org.jboss.quickstarts.wfk.contact.ContactService;
import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * provides RestFul functionality for {@link CustomerService}
 * 
 * @author Leo Rickayzen
 *
 */
@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/customers", description = "Operations about customers")
@Stateless
public class CustomerRestService {
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private CustomerService service;
    
    /**
     * retreives all customers
     * 
     * @return Response containing all customers
     */
    @GET
    @ApiOperation(value = "Fetch all customers", notes = "Returns a JSON array of all stored user objects.")
    public Response getAllCustomers(){
    	List<Customer> customers;
    	customers = service.findAll();
    	customers.retainAll(service.findAll());
		return Response.ok(customers).build();
    }
    
    /**
     * adds a new customer to the database
     * 
     * @param customer the customer to be added
     * @return the customer created, with the id field filled
     */
    @SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new User to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Customer created successfully."),
            @ApiResponse(code = 204, message = "Customer id provided when it should be automatically generated"),
            @ApiResponse(code = 400, message = "Invalid Customer supplied in request body"),
            @ApiResponse(code = 409, message = "Customer supplied in request body conflicts with an existing user"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createCustomer(Customer customer){
    	
    	if (customer == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
    	
    	try {
			
    		Customer createdCustomer = service.create(customer);

            return Response.status(Status.CREATED).entity(createdCustomer).build();
		
    	} catch (ConstraintViolationException ce) {
    		//bean validation issue
    		Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            
            throw new RestServiceException("Bad Request" , responseObj, Response.Status.BAD_REQUEST, ce);
		
    	} catch (UniqueEmailException e) {
    		e.printStackTrace();
    		throw new RestServiceException("Bad Request, email must be unique", Response.Status.CONFLICT, e);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    /**
     * deletes user with the corresponding id from the database
     * 
     * @param id the id of the user to delete from the database
     * @return response with the the full object of the deleted customer
     */
    @DELETE
    @ApiOperation(value = "delete a customer from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Customer deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid Contact id supplied"),
            @ApiResponse(code = 404, message = "Contact with id not found"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    @Path("/{id}")
    public Response deleteCustomer(
    		@ApiParam(value = "Id of user to be deleted", allowableValues = "range[0, infinity]", required = true)
    		@PathParam("id") 
    		long id){
    	Customer user = service.findById(id);
    	if(user == null){
    		throw new RestServiceException("No Contact with the id " + id + " was found!", Response.Status.NOT_FOUND);
		}else{
			try {
				service.delete(user);
				return Response.noContent().build();
			} catch (Exception e) {
				throw new RestServiceException(e);
			}
		}
    }
    
    /**
     * Get a user by it's id
     * 
     * @param id of the user to fetch
     * @return response containing the user with the provided id
     */
    @GET
    @ApiOperation(value = "get a user by it's id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User retreived successfully"),
            @ApiResponse(code = 204, message = "No user with that ID")
    })
    @Path("/{id}")
    public Response findCustomer(@ApiParam(value = "Id of user to be fetched", allowableValues = "range[0, infinity]", required = true)
    		@PathParam("id") long id){
    	Customer user = service.findById(id);
    	if(user == null){
    		return Response.noContent().build();
    	}else{
    		return Response.ok(user).build();
    	}
    }
}