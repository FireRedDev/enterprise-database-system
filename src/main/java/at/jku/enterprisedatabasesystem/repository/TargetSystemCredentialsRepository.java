package at.jku.enterprisedatabasesystem.repository;

import at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TargetSystemCredentials entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetSystemCredentialsRepository
    extends JpaRepository<TargetSystemCredentials, Long>, JpaSpecificationExecutor<TargetSystemCredentials> {}
