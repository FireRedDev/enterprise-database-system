package at.jku.employeeonboardingsystem.service;

import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Targetsystemcredentials}.
 */
public interface TargetsystemcredentialsService {
    /**
     * Save a targetsystemcredentials.
     *
     * @param targetsystemcredentials the entity to save.
     * @return the persisted entity.
     */
    Targetsystemcredentials save(Targetsystemcredentials targetsystemcredentials);

    /**
     * Partially updates a targetsystemcredentials.
     *
     * @param targetsystemcredentials the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Targetsystemcredentials> partialUpdate(Targetsystemcredentials targetsystemcredentials);

    /**
     * Get all the targetsystemcredentials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Targetsystemcredentials> findAll(Pageable pageable);

    /**
     * Get the "id" targetsystemcredentials.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Targetsystemcredentials> findOne(Long id);

    /**
     * Delete the "id" targetsystemcredentials.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
