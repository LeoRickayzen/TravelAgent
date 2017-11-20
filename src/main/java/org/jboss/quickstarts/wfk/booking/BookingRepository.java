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
    
    List<Booking> findAllBookings(){
    	TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_ALL, Booking.class);
    	return query.getResultList();
    }
    
    Booking createBooking(Booking booking){
    	em.persist(booking);
    	return booking;
    }
    
    Booking deleteBooking(Booking booking){
    	Booking b = em.merge(booking);
    	em.remove(b);
    	return booking;
    }
    
    Booking findByNumber(Long bookingNumber){
    	TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_BY_NUMBER, Booking.class).setParameter("number", bookingNumber);
    	Booking booking = null;
    	try{
        	booking = query.getSingleResult();
        }catch(NoResultException nre){
        	return null;
        }
    	return booking;
    }
}
