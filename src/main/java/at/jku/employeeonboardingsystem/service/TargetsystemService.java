package at.jku.employeeonboardingsystem.service;

import at.jku.employeeonboardingsystem.domain.Targetsystem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Targetsystem}.
 */
public interface TargetsystemService {
    /**
     * Save a targetsystem.
     *
     * @param targetsystem the entity to save.
     * @return the persisted entity.
     */
    Targetsystem save(Targetsystem targetsystem);

    /**
     * Partially updates a targetsystem.
     *
     * @param targetsystem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Targetsystem> partialUpdate(Targetsystem targetsystem);

    /**
     * Get all the targetsystems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Targetsystem> findAll(Pageable pageable);

    /**
     * Get the "id" targetsystem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Targetsystem> findOne(Long id);

    /**
     * Delete the "id" targetsystem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
