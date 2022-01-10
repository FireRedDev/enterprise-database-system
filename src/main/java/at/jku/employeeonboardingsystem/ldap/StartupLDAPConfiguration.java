package at.jku.employeeonboardingsystem.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class StartupLDAPConfiguration {
    @Autowired
    LDAPRepository ldapRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("EMBEDDED LDAP TEST: " + ldapRepository.getAllPersons().toString());
    }
}

