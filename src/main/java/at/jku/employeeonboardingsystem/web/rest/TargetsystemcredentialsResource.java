package at.jku.employeeonboardingsystem.web.rest;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import at.jku.employeeonboardingsystem.jdbc.TargetSystemJdbc;
import at.jku.employeeonboardingsystem.repository.TargetsystemRepository;
import at.jku.employeeonboardingsystem.repository.TargetsystemcredentialsRepository;
import at.jku.employeeonboardingsystem.service.TargetsystemcredentialsQueryService;
import at.jku.employeeonboardingsystem.service.TargetsystemcredentialsService;
import at.jku.employeeonboardingsystem.service.criteria.TargetsystemcredentialsCriteria;
import at.jku.employeeonboardingsystem.web.rest.errors.BadRequestAlertException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.Objects;
import java.util.Objects;
import java.util.Optional;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * REST controller for managing {@link at.jku.employeeonboardingsystem.domain.Targetsystemcredentials}.
 */
@RestController
@RequestMapping("/api")
public class TargetsystemcredentialsResource {

    private final Logger log = LoggerFactory.getLogger(TargetsystemcredentialsResource.class);

    private static final String ENTITY_NAME = "targetsystemcredentials";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetsystemcredentialsService targetsystemcredentialsService;

    private final TargetsystemcredentialsRepository targetsystemcredentialsRepository;

    private final TargetsystemcredentialsQueryService targetsystemcredentialsQueryService;

    public TargetsystemcredentialsResource(
        TargetsystemcredentialsService targetsystemcredentialsService,
        TargetsystemcredentialsRepository targetsystemcredentialsRepository,
        TargetsystemcredentialsQueryService targetsystemcredentialsQueryService,
        TargetsystemRepository targetsystemRepository
    ) {
        this.targetsystemcredentialsService = targetsystemcredentialsService;
        this.targetsystemcredentialsRepository = targetsystemcredentialsRepository;
        this.targetsystemcredentialsQueryService = targetsystemcredentialsQueryService;
    }

    @GetMapping("/targetsystemcredentials/csv/{id}/{attributes}")
    public void getCSV(@PathVariable("id") long id, HttpServletResponse response, @PathVariable String attributes) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=credentials_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Targetsystemcredentials> listCredentials = targetsystemcredentialsService.listAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = attributes.split(",");
        String[] nameMapping = attributes.split(",");

        csvWriter.writeHeader(csvHeader);

        for (Targetsystemcredentials c : listCredentials) {
            if (c.getTargetsystem().getId().equals(id)) csvWriter.write(c, nameMapping);
        }

        csvWriter.close();
    }

    @GetMapping("/targetsystemcredentials/database/{id}/{attributes}")
    public void getDatabaseConnection(@PathVariable("id") long id, @PathVariable String attributes) throws IOException {
        try {
            List<Targetsystemcredentials> listCredentials = targetsystemcredentialsService.listAll();
            for (Targetsystemcredentials c : listCredentials) {
                if (c.getTargetsystem().getId().equals(id)) {
                    String[] nameMapping = attributes.split(",");
                    c.getTargetsystem().setUrl(nameMapping[0]);
                    c.getTargetsystem().setUsername(nameMapping[1]);
                    c.getTargetsystem().setPassword(nameMapping[2]);
                    TargetSystemJdbc.copyDatabaseData(
                        c.getTargetsystem().getUrl(),
                        c.getTargetsystem().getUsername(),
                        c.getTargetsystem().getPassword()
                    );
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @GetMapping("/targetsystemcredentials/json/{id}")
    public ResponseEntity<byte[]> getJson(@PathVariable long id) {
        List<Targetsystemcredentials> listCredentials = targetsystemcredentialsService.listAll();
        listCredentials = listCredentials.stream().filter(c -> c.getTargetsystem().getId().equals(id)).collect(Collectors.toList());
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String credentialsInJson = gson.toJson(listCredentials);
        byte[] customerJsonBytes = credentialsInJson.getBytes();

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=credentials_" + currentDateTime + ".json")
            .contentType(MediaType.APPLICATION_JSON)
            .contentLength(customerJsonBytes.length)
            .body(customerJsonBytes);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @GetMapping("/targetsystemcredentials/xml/{id}")
    public void getXML(@PathVariable long id, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=credentials_" + currentDateTime + ".xml";
        response.setHeader(headerKey, headerValue);
        PrintWriter xmlWriter = response.getWriter();
        //  Targetsystemcredentials credentials = targetsystemcredentialsRepository.findById(id).orElseThrow();
        List<Targetsystemcredentials> listCredentials = targetsystemcredentialsService.listAll();
        StringWriter sw = new StringWriter();
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Targetsystemcredentials.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            listCredentials.forEach(c -> {
                if (c.getTargetsystem().getId().equals(id)) {
                    try {
                        //Write XML to StringWriter
                        jaxbMarshaller.marshal(c, sw);
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }
                }
            });

            //Verify XML Content
            String xmlContent = sw.toString();

            xmlWriter.append(xmlContent);
            xmlWriter.close();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
 * {@code GET  /targetsystemcredentials/csv/{id}} : get all the targetsystemcredentials for one targetsystem.
     * @param id
     * @param response
     * @param id the id of the targetsystem

 */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @GetMapping("/targetsystemcredentials/csv/{id}")
    public void getCSV(@PathVariable long id, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=credentials_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Targetsystemcredentials> listCredentials = targetsystemcredentialsService.listAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = { "User ID", "Username", "password", "Systemuser", "targetsystem" };
        String[] nameMapping = { "id", "username", "password", "systemuser", "targetsystem" };

        csvWriter.writeHeader(csvHeader);

        for (Targetsystemcredentials c : listCredentials) {
            if (c.getTargetsystem().getId().equals(id)) csvWriter.write(c, nameMapping);
        }

        csvWriter.close();
    }

    /**
     * {@code POST  /targetsystemcredentials} : Create a new targetsystemcredentials.
     *
     * @param targetsystemcredentials the targetsystemcredentials to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new targetsystemcredentials, or with status {@code 400 (Bad Request)} if the targetsystemcredentials has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @PostMapping("/targetsystemcredentials")
    public ResponseEntity<Targetsystemcredentials> createTargetsystemcredentials(
        @Valid @RequestBody Targetsystemcredentials targetsystemcredentials
    ) throws URISyntaxException {
        log.debug("REST request to save Targetsystemcredentials : {}", targetsystemcredentials);
        if (targetsystemcredentials.getId() != null) {
            throw new BadRequestAlertException("A new targetsystemcredentials cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Targetsystemcredentials result = targetsystemcredentialsService.save(targetsystemcredentials);
        return ResponseEntity
            .created(new URI("/api/targetsystemcredentials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /targetsystemcredentials/:id} : Updates an existing targetsystemcredentials.
     *
     * @param id the id of the targetsystemcredentials to save.
     * @param targetsystemcredentials the targetsystemcredentials to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetsystemcredentials,
     * or with status {@code 400 (Bad Request)} if the targetsystemcredentials is not valid,
     * or with status {@code 500 (Internal Server Error)} if the targetsystemcredentials couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @PutMapping("/targetsystemcredentials/{id}")
    public ResponseEntity<Targetsystemcredentials> updateTargetsystemcredentials(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Targetsystemcredentials targetsystemcredentials
    ) throws URISyntaxException {
        log.debug("REST request to update Targetsystemcredentials : {}, {}", id, targetsystemcredentials);
        if (targetsystemcredentials.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetsystemcredentials.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetsystemcredentialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Targetsystemcredentials result = targetsystemcredentialsService.save(targetsystemcredentials);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetsystemcredentials.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /targetsystemcredentials/:id} : Partial updates given fields of an existing targetsystemcredentials, field will ignore if it is null
     *
     * @param id the id of the targetsystemcredentials to save.
     * @param targetsystemcredentials the targetsystemcredentials to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetsystemcredentials,
     * or with status {@code 400 (Bad Request)} if the targetsystemcredentials is not valid,
     * or with status {@code 404 (Not Found)} if the targetsystemcredentials is not found,
     * or with status {@code 500 (Internal Server Error)} if the targetsystemcredentials couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @PatchMapping(value = "/targetsystemcredentials/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Targetsystemcredentials> partialUpdateTargetsystemcredentials(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Targetsystemcredentials targetsystemcredentials
    ) throws URISyntaxException {
        log.debug("REST request to partial update Targetsystemcredentials partially : {}, {}", id, targetsystemcredentials);
        if (targetsystemcredentials.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetsystemcredentials.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetsystemcredentialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Targetsystemcredentials> result = targetsystemcredentialsService.partialUpdate(targetsystemcredentials);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetsystemcredentials.getId().toString())
        );
    }

    /**
     * {@code GET  /targetsystemcredentials} : get all the targetsystemcredentials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targetsystemcredentials in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @GetMapping("/targetsystemcredentials")
    public ResponseEntity<List<Targetsystemcredentials>> getAllTargetsystemcredentials(
        TargetsystemcredentialsCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Targetsystemcredentials by criteria: {}", criteria);
        Page<Targetsystemcredentials> page = targetsystemcredentialsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /targetsystemcredentials/count} : count all the targetsystemcredentials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @GetMapping("/targetsystemcredentials/count")
    public ResponseEntity<Long> countTargetsystemcredentials(TargetsystemcredentialsCriteria criteria) {
        log.debug("REST request to count Targetsystemcredentials by criteria: {}", criteria);
        return ResponseEntity.ok().body(targetsystemcredentialsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /targetsystemcredentials/:id} : get the "id" targetsystemcredentials.
     *
     * @param id the id of the targetsystemcredentials to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the targetsystemcredentials, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @GetMapping("/targetsystemcredentials/{id}")
    public ResponseEntity<Targetsystemcredentials> getTargetsystemcredentials(@PathVariable Long id) {
        log.debug("REST request to get Targetsystemcredentials : {}", id);
        Optional<Targetsystemcredentials> targetsystemcredentials = targetsystemcredentialsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(targetsystemcredentials);
    }

    /**
     * {@code DELETE  /targetsystemcredentials/:id} : delete the "id" targetsystemcredentials.
     *
     * @param id the id of the targetsystemcredentials to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_HR')")
    @DeleteMapping("/targetsystemcredentials/{id}")
    public ResponseEntity<Void> deleteTargetsystemcredentials(@PathVariable Long id) {
        log.debug("REST request to delete Targetsystemcredentials : {}", id);
        targetsystemcredentialsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
