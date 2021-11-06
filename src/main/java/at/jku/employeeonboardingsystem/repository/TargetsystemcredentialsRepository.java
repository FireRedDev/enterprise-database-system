package at.jku.employeeonboardingsystem.repository;

import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Targetsystemcredentials entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetsystemcredentialsRepository
    extends JpaRepository<Targetsystemcredentials, Long>, JpaSpecificationExecutor<Targetsystemcredentials> {}
