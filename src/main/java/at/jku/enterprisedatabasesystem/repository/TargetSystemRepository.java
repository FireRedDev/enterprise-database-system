package at.jku.enterprisedatabasesystem.repository;

import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TargetSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetSystemRepository extends JpaRepository<TargetSystem, Long>, JpaSpecificationExecutor<TargetSystem> {}
