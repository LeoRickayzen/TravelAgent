package org.jboss.quickstarts.wfk.customer;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.quickstarts.wfk.booking.FlightBooking;
import org.jboss.quickstarts.wfk.contact.Contact;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>code is based on example code given in Contact.java by Joshua Wilson</p>
 * 
 * <p>Represents how users are stored and retreived from the database</p>
 *
 * @author Leo Rickayzen
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Customer.FIND_ALL, query = "SELECT c FROM Customer c ORDER BY c.lastName ASC, c.firstName ASC"),
        @NamedQuery(name = Customer.FIND_BY_EMAIL, query = "SELECT c FROM Customer c WHERE c.email = :email")
})
@XmlRootElement
@Table(name = "Customer", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	public static final String FIND_ALL = "Customer.findAll";
    public static final String FIND_BY_EMAIL = "Customer.findByEmail";
	
	@NotNull
	@Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials")
    @Column(name = "first_name")
	private String firstName;
	
	@NotNull
	@Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials")
    @Column(name = "last_name")
	private String lastName;
	
	@NotNull
    @NotEmpty
    @Email(message = "The email address must be in the format of name@domain.com")
    private String email;
	
	@NotNull
    @Pattern(regexp = "^0[0-9]{10}$")
    @Column(name = "phone_number")
    private String phoneNumber;
	
	@NotNull
    @Past(message = "Birthdates can not be in the future. Please choose one from the past")
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
	
	@JsonIgnore
	@OneToMany(mappedBy="customer", cascade = CascadeType.ALL)
	private Set<FlightBooking> bookings;
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public void setBookings(Set<FlightBooking> bookings){
    	this.bookings = bookings;
    }
    
    public Set<FlightBooking> getBookings(){
    	return bookings;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer user = (Customer) o;
        if (!email.equals(user.email)) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
