package at.jku.employeeonboardingsystem.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.ldap.core.support.BaseLdapPathBeanPostProcessor;

@Configuration
public class StartupLDAPConfiguration {

    @Autowired
    LDAPRepository ldapRepository;

    //@Value("${spring.ldap.embedded.base-dn}")
    private String baseDn = "dc=memorynotfound,dc=com";

    @Bean
    public BaseLdapPathBeanPostProcessor ldapPathBeanPostProcessor() {
        BaseLdapPathBeanPostProcessor baseLdapPathBeanPostProcessor = new BaseLdapPathBeanPostProcessor();
        baseLdapPathBeanPostProcessor.setBasePath(baseDn);
        System.out.print(baseDn);
        System.out.print("TEST BASE PATH");
        return baseLdapPathBeanPostProcessor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("EMBEDDED LDAP TEST: " + ldapRepository.getAllPersons().toString());
    }
}
