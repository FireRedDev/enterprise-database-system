package at.jku.employeeonboardingsystem.web.rest;

import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.repository.TargetsystemRepository;
import at.jku.employeeonboardingsystem.service.TargetsystemQueryService;
import at.jku.employeeonboardingsystem.service.TargetsystemService;
import at.jku.employeeonboardingsystem.service.criteria.TargetsystemCriteria;
import at.jku.employeeonboardingsystem.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.jku.employeeonboardingsystem.domain.Targetsystem}.
 */
@RestController
@RequestMapping("/api")
public class TargetsystemResource {

    private final Logger log = LoggerFactory.getLogger(TargetsystemResource.class);

    private static final String ENTITY_NAME = "targetsystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetsystemService targetsystemService;

    private final TargetsystemRepository targetsystemRepository;

    private final TargetsystemQueryService targetsystemQueryService;

    public TargetsystemResource(
        TargetsystemService targetsystemService,
        TargetsystemRepository targetsystemRepository,
        TargetsystemQueryService targetsystemQueryService
    ) {
        this.targetsystemService = targetsystemService;
        this.targetsystemRepository = targetsystemRepository;
        this.targetsystemQueryService = targetsystemQueryService;
    }

    /**
     * {@code POST  /targetsystems} : Create a new targetsystem.
     *
     * @param targetsystem the targetsystem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new targetsystem, or with status {@code 400 (Bad Request)} if the targetsystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/targetsystems")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_IT')")
    public ResponseEntity<Targetsystem> createTargetsystem(@RequestBody Targetsystem targetsystem) throws URISyntaxException {
        log.debug("REST request to save Targetsystem : {}", targetsystem);
        if (targetsystem.getId() != null) {
            throw new BadRequestAlertException("A new targetsystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Targetsystem result = targetsystemService.save(targetsystem);
        return ResponseEntity
            .created(new URI("/api/targetsystems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /targetsystems/:id} : Updates an existing targetsystem.
     *
     * @param id the id of the targetsystem to save.
     * @param targetsystem the targetsystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetsystem,
     * or with status {@code 400 (Bad Request)} if the targetsystem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the targetsystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/targetsystems/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_IT')")
    public ResponseEntity<Targetsystem> updateTargetsystem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Targetsystem targetsystem
    ) throws URISyntaxException {
        log.debug("REST request to update Targetsystem : {}, {}", id, targetsystem);
        if (targetsystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetsystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetsystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Targetsystem result = targetsystemService.save(targetsystem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetsystem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /targetsystems/:id} : Partial updates given fields of an existing targetsystem, field will ignore if it is null
     *
     * @param id the id of the targetsystem to save.
     * @param targetsystem the targetsystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetsystem,
     * or with status {@code 400 (Bad Request)} if the targetsystem is not valid,
     * or with status {@code 404 (Not Found)} if the targetsystem is not found,
     * or with status {@code 500 (Internal Server Error)} if the targetsystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/targetsystems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_IT')")
    public ResponseEntity<Targetsystem> partialUpdateTargetsystem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Targetsystem targetsystem
    ) throws URISyntaxException {
        log.debug("REST request to partial update Targetsystem partially : {}, {}", id, targetsystem);
        if (targetsystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetsystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetsystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Targetsystem> result = targetsystemService.partialUpdate(targetsystem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetsystem.getId().toString())
        );
    }

    /**
     * {@code GET  /targetsystems} : get all the targetsystems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targetsystems in body.
     */
    @GetMapping("/targetsystems")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_IT')")
    public ResponseEntity<List<Targetsystem>> getAllTargetsystems(TargetsystemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Targetsystems by criteria: {}", criteria);
        Page<Targetsystem> page = targetsystemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /targetsystems/count} : count all the targetsystems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/targetsystems/count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_IT')")
    public ResponseEntity<Long> countTargetsystems(TargetsystemCriteria criteria) {
        log.debug("REST request to count Targetsystems by criteria: {}", criteria);
        return ResponseEntity.ok().body(targetsystemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /targetsystems/:id} : get the "id" targetsystem.
     *
     * @param id the id of the targetsystem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the targetsystem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/targetsystems/{id}")
    public ResponseEntity<Targetsystem> getTargetsystem(@PathVariable Long id) {
        log.debug("REST request to get Targetsystem : {}", id);
        Optional<Targetsystem> targetsystem = targetsystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(targetsystem);
    }

    /**
     * {@code DELETE  /targetsystems/:id} : delete the "id" targetsystem.
     *
     * @param id the id of the targetsystem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/targetsystems/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" + "|| hasAuthority('ROLE_IT')")
    public ResponseEntity<Void> deleteTargetsystem(@PathVariable Long id) {
        log.debug("REST request to delete Targetsystem : {}", id);
        targetsystemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
