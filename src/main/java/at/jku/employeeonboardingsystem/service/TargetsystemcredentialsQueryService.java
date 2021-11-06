package at.jku.employeeonboardingsystem.service;

import at.jku.employeeonboardingsystem.domain.*; // for static metamodels
import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import at.jku.employeeonboardingsystem.repository.TargetsystemcredentialsRepository;
import at.jku.employeeonboardingsystem.service.criteria.TargetsystemcredentialsCriteria;
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
 * Service for executing complex queries for {@link Targetsystemcredentials} entities in the database.
 * The main input is a {@link TargetsystemcredentialsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Targetsystemcredentials} or a {@link Page} of {@link Targetsystemcredentials} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TargetsystemcredentialsQueryService extends QueryService<Targetsystemcredentials> {

    private final Logger log = LoggerFactory.getLogger(TargetsystemcredentialsQueryService.class);

    private final TargetsystemcredentialsRepository targetsystemcredentialsRepository;

    public TargetsystemcredentialsQueryService(TargetsystemcredentialsRepository targetsystemcredentialsRepository) {
        this.targetsystemcredentialsRepository = targetsystemcredentialsRepository;
    }

    /**
     * Return a {@link List} of {@link Targetsystemcredentials} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Targetsystemcredentials> findByCriteria(TargetsystemcredentialsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Targetsystemcredentials> specification = createSpecification(criteria);
        return targetsystemcredentialsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Targetsystemcredentials} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Targetsystemcredentials> findByCriteria(TargetsystemcredentialsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Targetsystemcredentials> specification = createSpecification(criteria);
        return targetsystemcredentialsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TargetsystemcredentialsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Targetsystemcredentials> specification = createSpecification(criteria);
        return targetsystemcredentialsRepository.count(specification);
    }

    /**
     * Function to convert {@link TargetsystemcredentialsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Targetsystemcredentials> createSpecification(TargetsystemcredentialsCriteria criteria) {
        Specification<Targetsystemcredentials> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Targetsystemcredentials_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), Targetsystemcredentials_.username));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Targetsystemcredentials_.password));
            }
            if (criteria.getSystemuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemuserId(),
                            root -> root.join(Targetsystemcredentials_.systemuser, JoinType.LEFT).get(Systemuser_.id)
                        )
                    );
            }
            if (criteria.getTargetsystemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTargetsystemId(),
                            root -> root.join(Targetsystemcredentials_.targetsystem, JoinType.LEFT).get(Targetsystem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
