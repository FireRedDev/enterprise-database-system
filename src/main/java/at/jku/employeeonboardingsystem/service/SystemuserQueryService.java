package at.jku.employeeonboardingsystem.service;

import at.jku.employeeonboardingsystem.domain.*; // for static metamodels
import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.repository.SystemuserRepository;
import at.jku.employeeonboardingsystem.service.criteria.SystemuserCriteria;
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
 * Service for executing complex queries for {@link Systemuser} entities in the database.
 * The main input is a {@link SystemuserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Systemuser} or a {@link Page} of {@link Systemuser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemuserQueryService extends QueryService<Systemuser> {

    private final Logger log = LoggerFactory.getLogger(SystemuserQueryService.class);

    private final SystemuserRepository systemuserRepository;

    public SystemuserQueryService(SystemuserRepository systemuserRepository) {
        this.systemuserRepository = systemuserRepository;
    }

    /**
     * Return a {@link List} of {@link Systemuser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Systemuser> findByCriteria(SystemuserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Systemuser> specification = createSpecification(criteria);
        return systemuserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Systemuser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Systemuser> findByCriteria(SystemuserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Systemuser> specification = createSpecification(criteria);
        return systemuserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemuserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Systemuser> specification = createSpecification(criteria);
        return systemuserRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemuserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Systemuser> createSpecification(SystemuserCriteria criteria) {
        Specification<Systemuser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Systemuser_.id));
            }
            if (criteria.getEntryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntryDate(), Systemuser_.entryDate));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Systemuser_.name));
            }
            if (criteria.getSocialSecurityNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSocialSecurityNumber(), Systemuser_.socialSecurityNumber));
            }
            if (criteria.getJobDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobDescription(), Systemuser_.jobDescription));
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(Systemuser_.departments, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
