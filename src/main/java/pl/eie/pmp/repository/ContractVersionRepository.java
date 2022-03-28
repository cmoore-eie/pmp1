package pl.eie.pmp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.eie.pmp.domain.ContractVersion;

/**
 * Spring Data SQL repository for the ContractVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractVersionRepository extends JpaRepository<ContractVersion, Long> {}
