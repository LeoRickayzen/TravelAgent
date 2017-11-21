package org.jboss.quickstarts.wfk.taxi;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jboss.quickstarts.wfk.area.Area;

public class TaxiBooking implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long customerID;
	
	private Long taxiID;
	
	private Date bookingDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Long customerId) {
		this.customerID = customerId;
	}

	public Long getTaxiID() {
		return taxiID;
	}

	public void setTaxiID(Long taxiId) {
		this.taxiID = taxiId;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date date) {
		this.bookingDate = date;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaxiBooking)) return false;

        TaxiBooking tab = (TaxiBooking) o;

        if (!id.equals(tab.getId())) return false;
        if (!customerID.equals(tab.getCustomerID())) return false;
        if (!taxiID.equals(tab.getTaxiID())) return false;
        return bookingDate.equals(tab.getBookingDate());

    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + customerID.intValue();
        result = 31 * result + taxiID.intValue();
        result = 31 * result + bookingDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaxiBooking{" +
                "id=" + id +
                ", customerID='" + customerID + '\'' +
                ", taxiID='" + taxiID + '\'' +
                ", date='" + bookingDate.toString() + '\'' +
                '}';
    }
	
}
