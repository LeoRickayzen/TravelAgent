package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
    	em.remove(booking);
    	return booking;
    }
}
