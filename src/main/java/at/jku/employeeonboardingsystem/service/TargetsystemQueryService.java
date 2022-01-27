package at.jku.employeeonboardingsystem.service;

import at.jku.employeeonboardingsystem.domain.*; // for static metamodels
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.repository.TargetsystemRepository;
import at.jku.employeeonboardingsystem.service.criteria.TargetsystemCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Targetsystem} entities in the database.
 * The main input is a {@link TargetsystemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Targetsystem} or a {@link Page} of {@link Targetsystem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TargetsystemQueryService extends QueryService<Targetsystem> {

    private final Logger log = LoggerFactory.getLogger(TargetsystemQueryService.class);

    private final TargetsystemRepository targetsystemRepository;

    public TargetsystemQueryService(TargetsystemRepository targetsystemRepository) {
        this.targetsystemRepository = targetsystemRepository;
    }

    /**
     * Return a {@link List} of {@link Targetsystem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Targetsystem> findByCriteria(TargetsystemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Targetsystem> specification = createSpecification(criteria);
        return targetsystemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Targetsystem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Targetsystem> findByCriteria(TargetsystemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Targetsystem> specification = createSpecification(criteria);
        return targetsystemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TargetsystemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Targetsystem> specification = createSpecification(criteria);
        return targetsystemRepository.count(specification);
    }

    /**
     * Function to convert {@link TargetsystemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Targetsystem> createSpecification(TargetsystemCriteria criteria) {
        Specification<Targetsystem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Targetsystem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Targetsystem_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Targetsystem_.type));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Targetsystem_.url));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Targetsystem_.password));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), Targetsystem_.username));
            }
            if (criteria.getCsvAttributes() != null) {
                specification = specification.and(buildSpecification(criteria.getCsvAttributes(), Targetsystem_.csvAttributes));
            }
        }
        return specification;
    }
}
