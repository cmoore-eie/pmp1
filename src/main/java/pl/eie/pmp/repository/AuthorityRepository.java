package pl.eie.pmp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eie.pmp.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
