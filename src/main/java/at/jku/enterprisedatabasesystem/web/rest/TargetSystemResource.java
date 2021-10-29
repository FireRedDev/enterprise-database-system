package at.jku.enterprisedatabasesystem.web.rest;

import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import at.jku.enterprisedatabasesystem.repository.TargetSystemRepository;
import at.jku.enterprisedatabasesystem.service.TargetSystemQueryService;
import at.jku.enterprisedatabasesystem.service.TargetSystemService;
import at.jku.enterprisedatabasesystem.service.criteria.TargetSystemCriteria;
import at.jku.enterprisedatabasesystem.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.jku.enterprisedatabasesystem.domain.TargetSystem}.
 */
@RestController
@RequestMapping("/api")
public class TargetSystemResource {

    private final Logger log = LoggerFactory.getLogger(TargetSystemResource.class);

    private static final String ENTITY_NAME = "targetSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetSystemService targetSystemService;

    private final TargetSystemRepository targetSystemRepository;

    private final TargetSystemQueryService targetSystemQueryService;

    public TargetSystemResource(
        TargetSystemService targetSystemService,
        TargetSystemRepository targetSystemRepository,
        TargetSystemQueryService targetSystemQueryService
    ) {
        this.targetSystemService = targetSystemService;
        this.targetSystemRepository = targetSystemRepository;
        this.targetSystemQueryService = targetSystemQueryService;
    }

    /**
     * {@code POST  /target-systems} : Create a new targetSystem.
     *
     * @param targetSystem the targetSystem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new targetSystem, or with status {@code 400 (Bad Request)} if the targetSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/target-systems")
    public ResponseEntity<TargetSystem> createTargetSystem(@RequestBody TargetSystem targetSystem) throws URISyntaxException {
        log.debug("REST request to save TargetSystem : {}", targetSystem);
        if (targetSystem.getId() != null) {
            throw new BadRequestAlertException("A new targetSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TargetSystem result = targetSystemService.save(targetSystem);
        return ResponseEntity
            .created(new URI("/api/target-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /target-systems/:id} : Updates an existing targetSystem.
     *
     * @param id the id of the targetSystem to save.
     * @param targetSystem the targetSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetSystem,
     * or with status {@code 400 (Bad Request)} if the targetSystem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the targetSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/target-systems/{id}")
    public ResponseEntity<TargetSystem> updateTargetSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TargetSystem targetSystem
    ) throws URISyntaxException {
        log.debug("REST request to update TargetSystem : {}, {}", id, targetSystem);
        if (targetSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetSystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TargetSystem result = targetSystemService.save(targetSystem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetSystem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /target-systems/:id} : Partial updates given fields of an existing targetSystem, field will ignore if it is null
     *
     * @param id the id of the targetSystem to save.
     * @param targetSystem the targetSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetSystem,
     * or with status {@code 400 (Bad Request)} if the targetSystem is not valid,
     * or with status {@code 404 (Not Found)} if the targetSystem is not found,
     * or with status {@code 500 (Internal Server Error)} if the targetSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/target-systems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TargetSystem> partialUpdateTargetSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TargetSystem targetSystem
    ) throws URISyntaxException {
        log.debug("REST request to partial update TargetSystem partially : {}, {}", id, targetSystem);
        if (targetSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, targetSystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TargetSystem> result = targetSystemService.partialUpdate(targetSystem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetSystem.getId().toString())
        );
    }

    /**
     * {@code GET  /target-systems} : get all the targetSystems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targetSystems in body.
     */
    @GetMapping("/target-systems")
    public ResponseEntity<List<TargetSystem>> getAllTargetSystems(TargetSystemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TargetSystems by criteria: {}", criteria);
        Page<TargetSystem> page = targetSystemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /target-systems/count} : count all the targetSystems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/target-systems/count")
    public ResponseEntity<Long> countTargetSystems(TargetSystemCriteria criteria) {
        log.debug("REST request to count TargetSystems by criteria: {}", criteria);
        return ResponseEntity.ok().body(targetSystemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /target-systems/:id} : get the "id" targetSystem.
     *
     * @param id the id of the targetSystem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the targetSystem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/target-systems/{id}")
    public ResponseEntity<TargetSystem> getTargetSystem(@PathVariable Long id) {
        log.debug("REST request to get TargetSystem : {}", id);
        Optional<TargetSystem> targetSystem = targetSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(targetSystem);
    }

    /**
     * {@code DELETE  /target-systems/:id} : delete the "id" targetSystem.
     *
     * @param id the id of the targetSystem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/target-systems/{id}")
    public ResponseEntity<Void> deleteTargetSystem(@PathVariable Long id) {
        log.debug("REST request to delete TargetSystem : {}", id);
        targetSystemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
