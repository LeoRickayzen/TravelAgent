package org.jboss.quickstarts.user;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.quickstarts.wfk.contact.ContactRestService;
import org.jboss.quickstarts.wfk.user.User;
import org.jboss.quickstarts.wfk.user.UserRestService;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import junit.framework.Assert;

@RunWith(Arquillian.class)
public class UserTest {
	
	@Deployment
    public static Archive<?> createTestArchive() {
        // This is currently not well tested. If you run into issues, comment line 67 (the contents of 'resolve') and
        // uncomment 65. This will build our war with all dependencies instead.
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
//                .importRuntimeAndTestDependencies()
                .resolve(
                        "io.swagger:swagger-jaxrs:1.5.15"
        ).withTransitivity().asFile();

        return ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, "org.jboss.quickstarts.wfk")
                .addAsLibraries(libs)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("arquillian-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	
	@Inject
    UserRestService userRestService;

    @Inject
    @Named("logger") Logger log;

    //Set millis 498484800000 from 1985-10-10T12:00:00.000Z
    private Date date = new Date(498484800000L);
    
    @Test
    @InSequence(1)
    public void testRegistration(){
    	User user = createUser("leo", "r", "lr1@ncl.ac.uk", "07859237477", new Date(1995, 06, 28));
    	Response response = userRestService.createUser(user);
    	
    	assertEquals(201, response.getStatus());
        log.info(" New contact was persisted and returned status " + response.getStatus());
    }
    
    public User createUser(String firstname, String lastname, String email, String phonenumber, Date birthDate){
    	User user = new User();
    	user.setFirstName(firstname);
    	user.setLastName(lastname);
    	user.setEmail(email);
    	user.setPhoneNumber(phonenumber);
    	user.setBirthDate(birthDate);
    	return user;
    }
}
