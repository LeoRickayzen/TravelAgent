package org.jboss.quickstarts.wfk.hotel;

import java.io.Serializable;
import java.util.Date;

import org.jboss.quickstarts.wfk.taxi.TaxiBooking;

public class HotelBooking implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long customerId;
	
	private Long hotelId;
	
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
    public String toString() {
        return "HotelBooking{" +
                "id=" + id +
                ", customerID='" + customerId + '\'' +
                ", hotelID='" + hotelId + '\'' +
                ", date='" + date.toString() + '\'' +
                '}';
    }
}
