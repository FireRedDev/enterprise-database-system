package at.jku.employeeonboardingsystem.repository;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Systemuser entity.
 */
@Repository
public interface SystemuserRepository extends JpaRepository<Systemuser, Long>, JpaSpecificationExecutor<Systemuser> {
    @Query(
        value = "select distinct systemuser from Systemuser systemuser left join fetch systemuser.departments",
        countQuery = "select count(distinct systemuser) from Systemuser systemuser"
    )
    Page<Systemuser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct systemuser from Systemuser systemuser left join fetch systemuser.departments")
    List<Systemuser> findAllWithEagerRelationships();

    @Query("select systemuser from Systemuser systemuser left join fetch systemuser.departments where systemuser.id =:id")
    Optional<Systemuser> findOneWithEagerRelationships(@Param("id") Long id);
}
