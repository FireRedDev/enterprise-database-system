package at.jku.enterprisedatabasesystem.service;

import at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TargetSystemCredentials}.
 */
public interface TargetSystemCredentialsService {
    /**
     * Save a targetSystemCredentials.
     *
     * @param targetSystemCredentials the entity to save.
     * @return the persisted entity.
     */
    TargetSystemCredentials save(TargetSystemCredentials targetSystemCredentials);

    /**
     * Partially updates a targetSystemCredentials.
     *
     * @param targetSystemCredentials the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TargetSystemCredentials> partialUpdate(TargetSystemCredentials targetSystemCredentials);

    /**
     * Get all the targetSystemCredentials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TargetSystemCredentials> findAll(Pageable pageable);

    /**
     * Get the "id" targetSystemCredentials.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TargetSystemCredentials> findOne(Long id);

    /**
     * Delete the "id" targetSystemCredentials.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
