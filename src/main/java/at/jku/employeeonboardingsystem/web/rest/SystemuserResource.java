package at.jku.employeeonboardingsystem.web.rest;

import at.jku.employeeonboardingsystem.domain.Department;
import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.jdbc.TargetSystemJdbc;
import at.jku.employeeonboardingsystem.repository.SystemuserRepository;
import at.jku.employeeonboardingsystem.repository.TargetsystemRepository;
import at.jku.employeeonboardingsystem.repository.TargetsystemcredentialsRepository;
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
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import liquibase.pro.packaged.t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.jku.employeeonboardingsystem.domain.Systemuser}.
 */
@RestController
@RequestMapping("/api")
public class SystemuserResource {

    private final Logger log = LoggerFactory.getLogger(SystemuserResource.class);

    private static final String ENTITY_NAME = "systemuser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemuserService systemuserService;

    private final SystemuserRepository systemuserRepository;

    private final SystemuserQueryService systemuserQueryService;

    private final TargetsystemcredentialsRepository targetsystemcredentialsRepository;

    public SystemuserResource(
        SystemuserService systemuserService,
        SystemuserRepository systemuserRepository,
        SystemuserQueryService systemuserQueryService,
        TargetsystemcredentialsRepository targetsystemcredentialsRepository
    ) {
        this.systemuserService = systemuserService;
        this.systemuserRepository = systemuserRepository;
        this.systemuserQueryService = systemuserQueryService;
        this.targetsystemcredentialsRepository = targetsystemcredentialsRepository;
    }

    @GetMapping("/users/xml/{id}")
    public void getXML(@PathVariable long id, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=credentials_" + currentDateTime + ".xml";
        response.setHeader(headerKey, headerValue);
        PrintWriter xmlWriter = response.getWriter();
        Systemuser user = systemuserRepository.findById(id).orElseThrow();

        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Systemuser.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();

            //Write XML to StringWriter
            jaxbMarshaller.marshal(user, sw);

            //Verify XML Content
            String xmlContent = sw.toString();

            xmlWriter.append(xmlContent);
            //  xmlWriter.close();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/users/csv")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public void getCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Systemuser> listUsers = systemuserService.listAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = { "User ID", "Name", "Social Security Number", "Job Description" };
        String[] nameMapping = { "id", "name", "socialSecurityNumber", "jobDescription" };

        csvWriter.writeHeader(csvHeader);

        for (Systemuser user : listUsers) {
            csvWriter.write(user, nameMapping);
        }

        csvWriter.close();
    }

    /**
     * {@code POST  /systemusers} : Create a new systemuser.
     *
     * @param systemuser the systemuser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemuser, or with status {@code 400 (Bad Request)} if the systemuser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/systemusers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<Systemuser> createSystemuser(@RequestBody Systemuser systemuser) throws URISyntaxException {
        log.debug("REST request to save Systemuser : {}", systemuser);
        if (systemuser.getId() != null) {
            throw new BadRequestAlertException("A new systemuser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Systemuser result = systemuserService.save(systemuser);
        return ResponseEntity
            .created(new URI("/api/systemusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /systemusers/:id} : Updates an existing systemuser.
     *
     * @param id the id of the systemuser to save.
     * @param systemuser the systemuser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemuser,
     * or with status {@code 400 (Bad Request)} if the systemuser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemuser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Transactional
    @PutMapping("/systemusers/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<Systemuser> updateSystemuser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Systemuser systemuser
    ) throws URISyntaxException {
        log.debug("REST request to update Systemuser : {}, {}", id, systemuser);
        Optional<Systemuser> user = systemuserService.findOne(id);
        List<Systemuser> users = user.map(Collections::singletonList).orElseGet(Collections::emptyList);
        for (Department d : users.get(0).getDepartments()) {
            if (!systemuser.getDepartments().contains(d)) {
                d
                    .getTargetsystems()
                    .forEach(t -> {
                        targetsystemcredentialsRepository.deleteBySystemuserAndTargetsystem(users.get(0), t);
                    });
                try {
                    TargetSystemJdbc.deleteFromDatabaseWithUser(d, users.get(0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (systemuser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemuser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemuserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Systemuser result = systemuserService.save(systemuser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemuser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /systemusers/:id} : Partial updates given fields of an existing systemuser, field will ignore if it is null
     *
     * @param id the id of the systemuser to save.
     * @param systemuser the systemuser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemuser,
     * or with status {@code 400 (Bad Request)} if the systemuser is not valid,
     * or with status {@code 404 (Not Found)} if the systemuser is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemuser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/systemusers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<Systemuser> partialUpdateSystemuser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Systemuser systemuser
    ) throws URISyntaxException {
        log.debug("REST request to partial update Systemuser partially : {}, {}", id, systemuser);
        System.out.println("IN partialUpdate SYSTEMUSER");
        if (systemuser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemuser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemuserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Systemuser> result = systemuserService.partialUpdate(systemuser);
        System.out.println("IN partialUpdate SYSTEMUSER");

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemuser.getId().toString())
        );
    }

    /**
     * {@code GET  /systemusers} : get all the systemusers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemusers in body.
     */
    @GetMapping("/systemusers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<List<Systemuser>> getAllSystemusers(SystemuserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Systemusers by criteria: {}", criteria);
        Page<Systemuser> page = systemuserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /systemusers/count} : count all the systemusers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */

    @GetMapping("/systemusers/count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<Long> countSystemusers(SystemuserCriteria criteria) {
        log.debug("REST request to count Systemusers by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemuserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /systemusers/:id} : get the "id" systemuser.
     *
     * @param id the id of the systemuser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemuser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/systemusers/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<Systemuser> getSystemuser(@PathVariable Long id) {
        log.debug("REST request to get Systemuser : {}", id);
        Optional<Systemuser> systemuser = systemuserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemuser);
    }

    /**
     * {@code DELETE  /systemusers/:id} : delete the "id" systemuser.
     *
     * @param id the id of the systemuser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/systemusers/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    public ResponseEntity<Void> deleteSystemuser(@PathVariable Long id) {
        log.debug("REST request to delete Systemuser : {}", id);
        systemuserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
