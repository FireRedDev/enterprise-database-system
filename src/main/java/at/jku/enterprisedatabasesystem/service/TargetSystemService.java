package at.jku.enterprisedatabasesystem.service;

import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TargetSystem}.
 */
public interface TargetSystemService {
    /**
     * Save a targetSystem.
     *
     * @param targetSystem the entity to save.
     * @return the persisted entity.
     */
    TargetSystem save(TargetSystem targetSystem);

    /**
     * Partially updates a targetSystem.
     *
     * @param targetSystem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TargetSystem> partialUpdate(TargetSystem targetSystem);

    /**
     * Get all the targetSystems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TargetSystem> findAll(Pageable pageable);

    /**
     * Get the "id" targetSystem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TargetSystem> findOne(Long id);

    /**
     * Delete the "id" targetSystem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
