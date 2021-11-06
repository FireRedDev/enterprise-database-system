package at.jku.employeeonboardingsystem.repository;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Systemuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemuserRepository extends JpaRepository<Systemuser, Long>, JpaSpecificationExecutor<Systemuser> {}
