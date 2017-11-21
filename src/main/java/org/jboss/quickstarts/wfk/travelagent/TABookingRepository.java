package org.jboss.quickstarts.wfk.travelagent;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.quickstarts.wfk.booking.Booking;

public class TABookingRepository {

	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;
    
    List<TABooking> findAllBookings(){
    	TypedQuery<TABooking> query = em.createNamedQuery(TABooking.FIND_ALL, TABooking.class);
    	return query.getResultList();
    }
    
    TABooking createBooking(TABooking tab){
    	em.persist(tab);
    	return tab;
    }
    
    TABooking deleteBooking(TABooking tab){
    	TABooking b = em.merge(tab);
    	em.remove(b);
    	return tab;
    }
	
}
