package at.jku.employeeonboardingsystem.ldap;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.repository.SystemuserRepository;
import at.jku.employeeonboardingsystem.service.SystemuserQueryService;
import at.jku.employeeonboardingsystem.service.SystemuserService;
import at.jku.employeeonboardingsystem.service.criteria.SystemuserCriteria;
import at.jku.employeeonboardingsystem.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Systemuser}.
 */
@RestController
@RequestMapping("/api")
public class LDAPResource {

    private final Logger log = LoggerFactory.getLogger(LDAPResource.class);

    private static final String ENTITY_NAME = "LDAP_Person";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LDAPRepository ldapRepository;

    public LDAPResource(LDAPRepository ldapRepository) {
        this.ldapRepository = ldapRepository;
    }

    @GetMapping("ldap/users/{id}")
    public Person findOneLDAPUser(@PathVariable Optional<String> id) {
        return id.map(ldapRepository::findPerson).orElse(null);
    }

    @GetMapping("ldap/users/delete/{id}")
    public List<Person> deleteAndFindUser(@PathVariable Optional<String> id) {
        ldapRepository.deletePerson(id);
        return ldapRepository.getAllPersons();
    }

    @GetMapping("ldap/users/")
    public List<Person> findAllLDAPUser() {
        return ldapRepository.getAllPersons();
    }

    @PostMapping("ldap/users/{person}")
    public void updateOrCreateOneLDAPUser(@PathVariable Optional<Person> person) {
        ldapRepository.updatePerson(person);
    }

    @DeleteMapping("ldap/users/delete/{id}")
    public void deleteOneLDAPUser(@PathVariable Optional<String> id) {
        ldapRepository.deletePerson(id);
    }
}
