package pl.eie.pmp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.VirtualProductSeasoning;

/**
 * Spring Data SQL repository for the VirtualProductSeasoning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VirtualProductSeasoningRepository extends JpaRepository<VirtualProductSeasoning, Long> {}
