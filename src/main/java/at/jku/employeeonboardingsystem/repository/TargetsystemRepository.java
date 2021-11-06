package at.jku.employeeonboardingsystem.repository;

import at.jku.employeeonboardingsystem.domain.Targetsystem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Targetsystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetsystemRepository extends JpaRepository<Targetsystem, Long>, JpaSpecificationExecutor<Targetsystem> {}
