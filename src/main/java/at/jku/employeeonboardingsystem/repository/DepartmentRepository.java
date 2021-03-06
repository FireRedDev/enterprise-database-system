package at.jku.employeeonboardingsystem.repository;

import at.jku.employeeonboardingsystem.domain.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    @Query(
        value = "select distinct department from Department department left join fetch department.targetsystems",
        countQuery = "select count(distinct department) from Department department"
    )
    Page<Department> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct department from Department department left join fetch department.targetsystems")
    List<Department> findAllWithEagerRelationships();

    @Query("select department from Department department left join fetch department.targetsystems where department.id =:id")
    Optional<Department> findOneWithEagerRelationships(@Param("id") Long id);
}
