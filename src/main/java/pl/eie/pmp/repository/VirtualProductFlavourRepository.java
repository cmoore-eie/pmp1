package pl.eie.pmp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProductFlavour;

/**
 * Spring Data SQL repository for the VirtualProductFlavour entity.
 */
@Repository
public interface VirtualProductFlavourRepository extends JpaRepository<VirtualProductFlavour, Long> {
    default Optional<VirtualProductFlavour> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VirtualProductFlavour> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VirtualProductFlavour> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct virtualProductFlavour from VirtualProductFlavour virtualProductFlavour left join fetch virtualProductFlavour.virtualProduct",
        countQuery = "select count(distinct virtualProductFlavour) from VirtualProductFlavour virtualProductFlavour"
    )
    Page<VirtualProductFlavour> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct virtualProductFlavour from VirtualProductFlavour virtualProductFlavour left join fetch virtualProductFlavour.virtualProduct"
    )
    List<VirtualProductFlavour> findAllWithToOneRelationships();

    @Query(
        "select virtualProductFlavour from VirtualProductFlavour virtualProductFlavour left join fetch virtualProductFlavour.virtualProduct where virtualProductFlavour.id =:id"
    )
    Optional<VirtualProductFlavour> findOneWithToOneRelationships(@Param("id") Long id);
}
