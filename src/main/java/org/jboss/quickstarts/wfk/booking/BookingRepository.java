package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.quickstarts.wfk.flight.Flight;

public class BookingRepository {
	
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;
    
    List<FlightBooking> findAllBookings(){
    	TypedQuery<FlightBooking> query = em.createNamedQuery(FlightBooking.FIND_ALL, FlightBooking.class);
    	return query.getResultList();
    }
    
    List<FlightBooking> findByCustomerId(Long id){
    	TypedQuery<FlightBooking> query = em.createNamedQuery(FlightBooking.FIND_BY_CUSTOMER, FlightBooking.class).setParameter("ids", id);
    	return query.getResultList();
    }
    
    FlightBooking createBooking(FlightBooking booking){
    	em.persist(booking);
    	return booking;
    }
    
    FlightBooking deleteBooking(FlightBooking booking){
    	FlightBooking b = em.merge(booking);
    	em.remove(b);
    	return booking;
    }
    
    FlightBooking findByNumber(Long bookingNumber){
    	TypedQuery<FlightBooking> query = em.createNamedQuery(FlightBooking.FIND_BY_NUMBER, FlightBooking.class).setParameter("number", bookingNumber);
    	FlightBooking booking = null;
    	try{
        	booking = query.getSingleResult();
        }catch(NoResultException nre){
        	return null;
        }
    	return booking;
    }
}
