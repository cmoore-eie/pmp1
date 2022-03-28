package pl.eie.pmp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProductContract;

/**
 * Spring Data SQL repository for the VirtualProductContract entity.
 */
@Repository
public interface VirtualProductContractRepository extends JpaRepository<VirtualProductContract, Long> {
    default Optional<VirtualProductContract> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VirtualProductContract> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VirtualProductContract> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct virtualProductContract from VirtualProductContract virtualProductContract left join fetch virtualProductContract.virtualProduct",
        countQuery = "select count(distinct virtualProductContract) from VirtualProductContract virtualProductContract"
    )
    Page<VirtualProductContract> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct virtualProductContract from VirtualProductContract virtualProductContract left join fetch virtualProductContract.virtualProduct"
    )
    List<VirtualProductContract> findAllWithToOneRelationships();

    @Query(
        "select virtualProductContract from VirtualProductContract virtualProductContract left join fetch virtualProductContract.virtualProduct where virtualProductContract.id =:id"
    )
    Optional<VirtualProductContract> findOneWithToOneRelationships(@Param("id") Long id);
}
