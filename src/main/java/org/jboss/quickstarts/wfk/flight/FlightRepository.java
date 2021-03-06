package org.jboss.quickstarts.wfk.flight;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

public class FlightRepository {

	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;
    
    public List<Flight> findAllOrderedByNumber(){
    	TypedQuery<Flight> query = em.createNamedQuery(Flight.FIND_ALL, Flight.class);
		return query.getResultList();
    }
    
    public Flight createFlight(Flight flight) throws ConstraintViolationException, ValidationException, Exception{
    	log.info("creating a flight");
    	em.persist(flight);
    	return flight;
    }
    
    public Flight deleteFlight(Flight flight){
    	em.remove(flight);
    	return flight;
    }
    
    public Flight findByNumber(String flightNumber){
    	System.out.println("zzzzz: " + flightNumber);
    	TypedQuery<Flight> query = em.createNamedQuery(Flight.FIND_BY_NUMBER, Flight.class).setParameter("number", flightNumber);
    	Flight flight = null;
    	try{
        	flight = query.getSingleResult();
        }catch(NoResultException nre){
        	
        }
    	return flight;
    }
    
    public Flight findFlightById(Long id){
    	return em.find(Flight.class, id);
    }
}