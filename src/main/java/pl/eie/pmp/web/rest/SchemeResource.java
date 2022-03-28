package pl.eie.pmp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.eie.pmp.domain.Scheme;
import pl.eie.pmp.repository.SchemeRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.Scheme}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SchemeResource {

    private final Logger log = LoggerFactory.getLogger(SchemeResource.class);

    private static final String ENTITY_NAME = "scheme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchemeRepository schemeRepository;

    public SchemeResource(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    /**
     * {@code POST  /schemes} : Create a new scheme.
     *
     * @param scheme the scheme to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheme, or with status {@code 400 (Bad Request)} if the scheme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schemes")
    public ResponseEntity<Scheme> createScheme(@RequestBody Scheme scheme) throws URISyntaxException {
        log.debug("REST request to save Scheme : {}", scheme);
        if (scheme.getId() != null) {
            throw new BadRequestAlertException("A new scheme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scheme result = schemeRepository.save(scheme);
        return ResponseEntity
            .created(new URI("/api/schemes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schemes/:id} : Updates an existing scheme.
     *
     * @param id the id of the scheme to save.
     * @param scheme the scheme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheme,
     * or with status {@code 400 (Bad Request)} if the scheme is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schemes/{id}")
    public ResponseEntity<Scheme> updateScheme(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scheme scheme)
        throws URISyntaxException {
        log.debug("REST request to update Scheme : {}, {}", id, scheme);
        if (scheme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schemeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Scheme result = schemeRepository.save(scheme);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheme.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schemes/:id} : Partial updates given fields of an existing scheme, field will ignore if it is null
     *
     * @param id the id of the scheme to save.
     * @param scheme the scheme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheme,
     * or with status {@code 400 (Bad Request)} if the scheme is not valid,
     * or with status {@code 404 (Not Found)} if the scheme is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schemes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Scheme> partialUpdateScheme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Scheme scheme
    ) throws URISyntaxException {
        log.debug("REST request to partial update Scheme partially : {}, {}", id, scheme);
        if (scheme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schemeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Scheme> result = schemeRepository
            .findById(scheme.getId())
            .map(existingScheme -> {
                if (scheme.getCode() != null) {
                    existingScheme.setCode(scheme.getCode());
                }
                if (scheme.getName() != null) {
                    existingScheme.setName(scheme.getName());
                }

                return existingScheme;
            })
            .map(schemeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheme.getId().toString())
        );
    }

    /**
     * {@code GET  /schemes} : get all the schemes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schemes in body.
     */
    @GetMapping("/schemes")
    public List<Scheme> getAllSchemes() {
        log.debug("REST request to get all Schemes");
        return schemeRepository.findAll();
    }

    /**
     * {@code GET  /schemes/:id} : get the "id" scheme.
     *
     * @param id the id of the scheme to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheme, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schemes/{id}")
    public ResponseEntity<Scheme> getScheme(@PathVariable Long id) {
        log.debug("REST request to get Scheme : {}", id);
        Optional<Scheme> scheme = schemeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheme);
    }

    /**
     * {@code DELETE  /schemes/:id} : delete the "id" scheme.
     *
     * @param id the id of the scheme to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schemes/{id}")
    public ResponseEntity<Void> deleteScheme(@PathVariable Long id) {
        log.debug("REST request to delete Scheme : {}", id);
        schemeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
