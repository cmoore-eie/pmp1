package pl.eie.pmp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProductLine;

/**
 * Spring Data SQL repository for the VirtualProductLine entity.
 */
@Repository
public interface VirtualProductLineRepository extends JpaRepository<VirtualProductLine, Long> {
    default Optional<VirtualProductLine> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VirtualProductLine> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VirtualProductLine> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct virtualProductLine from VirtualProductLine virtualProductLine left join fetch virtualProductLine.virtualProduct",
        countQuery = "select count(distinct virtualProductLine) from VirtualProductLine virtualProductLine"
    )
    Page<VirtualProductLine> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct virtualProductLine from VirtualProductLine virtualProductLine left join fetch virtualProductLine.virtualProduct"
    )
    List<VirtualProductLine> findAllWithToOneRelationships();

    @Query(
        "select virtualProductLine from VirtualProductLine virtualProductLine left join fetch virtualProductLine.virtualProduct where virtualProductLine.id =:id"
    )
    Optional<VirtualProductLine> findOneWithToOneRelationships(@Param("id") Long id);
}
