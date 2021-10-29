package at.jku.enterprisedatabasesystem.service;

import at.jku.enterprisedatabasesystem.domain.*; // for static metamodels
import at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials;
import at.jku.enterprisedatabasesystem.repository.TargetSystemCredentialsRepository;
import at.jku.enterprisedatabasesystem.service.criteria.TargetSystemCredentialsCriteria;
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
 * Service for executing complex queries for {@link TargetSystemCredentials} entities in the database.
 * The main input is a {@link TargetSystemCredentialsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TargetSystemCredentials} or a {@link Page} of {@link TargetSystemCredentials} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TargetSystemCredentialsQueryService extends QueryService<TargetSystemCredentials> {

    private final Logger log = LoggerFactory.getLogger(TargetSystemCredentialsQueryService.class);

    private final TargetSystemCredentialsRepository targetSystemCredentialsRepository;

    public TargetSystemCredentialsQueryService(TargetSystemCredentialsRepository targetSystemCredentialsRepository) {
        this.targetSystemCredentialsRepository = targetSystemCredentialsRepository;
    }

    /**
     * Return a {@link List} of {@link TargetSystemCredentials} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TargetSystemCredentials> findByCriteria(TargetSystemCredentialsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TargetSystemCredentials> specification = createSpecification(criteria);
        return targetSystemCredentialsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TargetSystemCredentials} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TargetSystemCredentials> findByCriteria(TargetSystemCredentialsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TargetSystemCredentials> specification = createSpecification(criteria);
        return targetSystemCredentialsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TargetSystemCredentialsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TargetSystemCredentials> specification = createSpecification(criteria);
        return targetSystemCredentialsRepository.count(specification);
    }

    /**
     * Function to convert {@link TargetSystemCredentialsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TargetSystemCredentials> createSpecification(TargetSystemCredentialsCriteria criteria) {
        Specification<TargetSystemCredentials> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TargetSystemCredentials_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), TargetSystemCredentials_.username));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), TargetSystemCredentials_.password));
            }
            if (criteria.getTargetSystemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTargetSystemId(),
                            root -> root.join(TargetSystemCredentials_.targetSystem, JoinType.LEFT).get(TargetSystem_.id)
                        )
                    );
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(TargetSystemCredentials_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(TargetSystemCredentials_.department, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
