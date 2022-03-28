package pl.eie.pmp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProductOrganization;

/**
 * Spring Data SQL repository for the VirtualProductOrganization entity.
 */
@Repository
public interface VirtualProductOrganizationRepository extends JpaRepository<VirtualProductOrganization, Long> {
    default Optional<VirtualProductOrganization> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VirtualProductOrganization> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VirtualProductOrganization> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct virtualProductOrganization from VirtualProductOrganization virtualProductOrganization left join fetch virtualProductOrganization.virtualProduct",
        countQuery = "select count(distinct virtualProductOrganization) from VirtualProductOrganization virtualProductOrganization"
    )
    Page<VirtualProductOrganization> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct virtualProductOrganization from VirtualProductOrganization virtualProductOrganization left join fetch virtualProductOrganization.virtualProduct"
    )
    List<VirtualProductOrganization> findAllWithToOneRelationships();

    @Query(
        "select virtualProductOrganization from VirtualProductOrganization virtualProductOrganization left join fetch virtualProductOrganization.virtualProduct where virtualProductOrganization.id =:id"
    )
    Optional<VirtualProductOrganization> findOneWithToOneRelationships(@Param("id") Long id);
}
