package pl.eie.pmp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProduct;

/**
 * Spring Data SQL repository for the VirtualProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VirtualProductRepository extends JpaRepository<VirtualProduct, Long> {}
