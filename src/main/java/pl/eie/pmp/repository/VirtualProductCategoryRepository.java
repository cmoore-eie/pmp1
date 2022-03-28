package pl.eie.pmp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProductCategory;

/**
 * Spring Data SQL repository for the VirtualProductCategory entity.
 */
@Repository
public interface VirtualProductCategoryRepository extends JpaRepository<VirtualProductCategory, Long> {
    default Optional<VirtualProductCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VirtualProductCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VirtualProductCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct virtualProductCategory from VirtualProductCategory virtualProductCategory left join fetch virtualProductCategory.virtualProduct",
        countQuery = "select count(distinct virtualProductCategory) from VirtualProductCategory virtualProductCategory"
    )
    Page<VirtualProductCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct virtualProductCategory from VirtualProductCategory virtualProductCategory left join fetch virtualProductCategory.virtualProduct"
    )
    List<VirtualProductCategory> findAllWithToOneRelationships();

    @Query(
        "select virtualProductCategory from VirtualProductCategory virtualProductCategory left join fetch virtualProductCategory.virtualProduct where virtualProductCategory.id =:id"
    )
    Optional<VirtualProductCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
