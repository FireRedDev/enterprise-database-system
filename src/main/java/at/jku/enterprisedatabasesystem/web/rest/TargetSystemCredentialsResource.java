package at.jku.enterprisedatabasesystem.web.rest;

import at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials;
import at.jku.enterprisedatabasesystem.repository.TargetSystemCredentialsRepository;
import at.jku.enterprisedatabasesystem.service.TargetSystemCredentialsQueryService;
import at.jku.enterprisedatabasesystem.service.TargetSystemCredentialsService;
import at.jku.enterprisedatabasesystem.service.criteria.TargetSystemCredentialsCriteria;
import at.jku.enterprisedatabasesystem.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials}.
 */
@RestController
@RequestMapping("/api")
public class TargetSystemCredentialsResource {

    private final Logger log = LoggerFactory.getLogger(TargetSystemCredentialsResource.class);

    private static final String ENTITY_NAME = "targetSystemCredentials";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetSystemCredentialsService targetSystemCredentialsService;

    private final TargetSystemCredentialsRepository targetSystemCredentialsRepository;

    private final TargetSystemCredentialsQueryService targetSystemCredentialsQueryService;

    public TargetSystemCredentialsResource(
        TargetSystemCredentialsService targetSystemCredentialsService,
        TargetSystemCredentialsRepository targetSystemCredentialsRepository,
        TargetSystemCredentialsQueryService targetSystemCredentialsQueryService
    ) {
        this.targetSystemCredentialsService = targetSystemCredentialsService;
        this.targetSystemCredentialsRepository = targetSystemCredentialsRepository;
        this.targetSystemCredentialsQueryService = targetSystemCredentialsQueryService;
    }

    /**
     * {@code POST  /target-system-credentials} : Create a new targetSystemCredentials.
     *
     * @param targetSystemCredentials the targetSystemCredentials to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new targetSystemCredentials, or with status {@code 400 (Bad Request)} if the targetSystemCredentials has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/target-system-credentials")
    public ResponseEntity<TargetSystemCredentials> createTargetSystemCredentials(
        @Valid @RequestBody TargetSystemCredentials targetSystemCredentials
    ) throws URISyntaxException {
        log.debug("REST request to save TargetSystemCredentials : {}", targetSystemCredentials);
        if (targetSystemCredentials.getId() != null) {
            throw new BadRequestAlertException("A new targetSystemCredentials cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TargetSystemCredentials result = targetSystemCredentialsService.save(targetSystemCredentials);
        return ResponseEntity
            .created(new URI("/api/target-system-credentials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /target-system-credentials/:id} : Updates an existing targetSystemCredentials.
     *
     * @param id the id of the targetSystemCredentials to save.
     * @param targetSystemCredentials the targetSystemCredentials to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetSystemCredentials,
     * or with status {@code 400 (Bad Request)} if the targetSystemCredentials is not valid,
     * or with status {@code 500 (Internal Server Error)} if the targetSystemCredentials couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/target-system-credentials/{id}")
    public ResponseEntity<TargetSystemCredentials> updateTargetSystemCredentials(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TargetSystemCredentials targetSystemCredentials
    ) throws URISyntaxException {
        log.debug("REST request to update TargetSystemCredentials : {}, {}", id, targetSystemCredentials);
        if (targetSystemCredentials.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetSystemCredentials.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetSystemCredentialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TargetSystemCredentials result = targetSystemCredentialsService.save(targetSystemCredentials);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetSystemCredentials.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /target-system-credentials/:id} : Partial updates given fields of an existing targetSystemCredentials, field will ignore if it is null
     *
     * @param id the id of the targetSystemCredentials to save.
     * @param targetSystemCredentials the targetSystemCredentials to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetSystemCredentials,
     * or with status {@code 400 (Bad Request)} if the targetSystemCredentials is not valid,
     * or with status {@code 404 (Not Found)} if the targetSystemCredentials is not found,
     * or with status {@code 500 (Internal Server Error)} if the targetSystemCredentials couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/target-system-credentials/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TargetSystemCredentials> partialUpdateTargetSystemCredentials(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TargetSystemCredentials targetSystemCredentials
    ) throws URISyntaxException {
        log.debug("REST request to partial update TargetSystemCredentials partially : {}, {}", id, targetSystemCredentials);
        if (targetSystemCredentials.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetSystemCredentials.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetSystemCredentialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TargetSystemCredentials> result = targetSystemCredentialsService.partialUpdate(targetSystemCredentials);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetSystemCredentials.getId().toString())
        );
    }

    /**
     * {@code GET  /target-system-credentials} : get all the targetSystemCredentials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targetSystemCredentials in body.
     */
    @GetMapping("/target-system-credentials")
    public ResponseEntity<List<TargetSystemCredentials>> getAllTargetSystemCredentials(
        TargetSystemCredentialsCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TargetSystemCredentials by criteria: {}", criteria);
        Page<TargetSystemCredentials> page = targetSystemCredentialsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /target-system-credentials/count} : count all the targetSystemCredentials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/target-system-credentials/count")
    public ResponseEntity<Long> countTargetSystemCredentials(TargetSystemCredentialsCriteria criteria) {
        log.debug("REST request to count TargetSystemCredentials by criteria: {}", criteria);
        return ResponseEntity.ok().body(targetSystemCredentialsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /target-system-credentials/:id} : get the "id" targetSystemCredentials.
     *
     * @param id the id of the targetSystemCredentials to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the targetSystemCredentials, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/target-system-credentials/{id}")
    public ResponseEntity<TargetSystemCredentials> getTargetSystemCredentials(@PathVariable Long id) {
        log.debug("REST request to get TargetSystemCredentials : {}", id);
        Optional<TargetSystemCredentials> targetSystemCredentials = targetSystemCredentialsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(targetSystemCredentials);
    }

    /**
     * {@code DELETE  /target-system-credentials/:id} : delete the "id" targetSystemCredentials.
     *
     * @param id the id of the targetSystemCredentials to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/target-system-credentials/{id}")
    public ResponseEntity<Void> deleteTargetSystemCredentials(@PathVariable Long id) {
        log.debug("REST request to delete TargetSystemCredentials : {}", id);
        targetSystemCredentialsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
