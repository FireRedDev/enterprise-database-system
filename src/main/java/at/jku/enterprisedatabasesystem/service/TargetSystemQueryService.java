package at.jku.enterprisedatabasesystem.service;

import at.jku.enterprisedatabasesystem.domain.*; // for static metamodels
import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import at.jku.enterprisedatabasesystem.repository.TargetSystemRepository;
import at.jku.enterprisedatabasesystem.service.criteria.TargetSystemCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TargetSystem} entities in the database.
 * The main input is a {@link TargetSystemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TargetSystem} or a {@link Page} of {@link TargetSystem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TargetSystemQueryService extends QueryService<TargetSystem> {

    private final Logger log = LoggerFactory.getLogger(TargetSystemQueryService.class);

    private final TargetSystemRepository targetSystemRepository;

    public TargetSystemQueryService(TargetSystemRepository targetSystemRepository) {
        this.targetSystemRepository = targetSystemRepository;
    }

    /**
     * Return a {@link List} of {@link TargetSystem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TargetSystem> findByCriteria(TargetSystemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TargetSystem> specification = createSpecification(criteria);
        return targetSystemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TargetSystem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TargetSystem> findByCriteria(TargetSystemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TargetSystem> specification = createSpecification(criteria);
        return targetSystemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TargetSystemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TargetSystem> specification = createSpecification(criteria);
        return targetSystemRepository.count(specification);
    }

    /**
     * Function to convert {@link TargetSystemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TargetSystem> createSpecification(TargetSystemCriteria criteria) {
        Specification<TargetSystem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TargetSystem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TargetSystem_.name));
            }
        }
        return specification;
    }
}
