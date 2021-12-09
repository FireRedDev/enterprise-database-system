package at.jku.employeeonboardingsystem.repository;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Targetsystemcredentials entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetsystemcredentialsRepository
    extends JpaRepository<Targetsystemcredentials, Long>, JpaSpecificationExecutor<Targetsystemcredentials> {
    List<Targetsystemcredentials> findBySystemuser(Systemuser systemuser);
    boolean existsTargetsystemcredentialsBySystemuserAndTargetsystem(Systemuser systemuser, Targetsystem targetsystem);
}
