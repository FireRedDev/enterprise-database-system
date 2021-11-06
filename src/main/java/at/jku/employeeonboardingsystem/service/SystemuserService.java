package at.jku.employeeonboardingsystem.service;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Systemuser}.
 */
public interface SystemuserService {
    /**
     * Save a systemuser.
     *
     * @param systemuser the entity to save.
     * @return the persisted entity.
     */
    Systemuser save(Systemuser systemuser);

    /**
     * Partially updates a systemuser.
     *
     * @param systemuser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Systemuser> partialUpdate(Systemuser systemuser);

    /**
     * Get all the systemusers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Systemuser> findAll(Pageable pageable);

    /**
     * Get all the systemusers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Systemuser> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" systemuser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Systemuser> findOne(Long id);

    /**
     * Delete the "id" systemuser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
