package pl.eie.pmp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.eie.pmp.domain.ContractVersion;
import pl.eie.pmp.repository.ContractVersionRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.ContractVersion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContractVersionResource {

    private final Logger log = LoggerFactory.getLogger(ContractVersionResource.class);

    private static final String ENTITY_NAME = "contractVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractVersionRepository contractVersionRepository;

    public ContractVersionResource(ContractVersionRepository contractVersionRepository) {
        this.contractVersionRepository = contractVersionRepository;
    }

    /**
     * {@code POST  /contract-versions} : Create a new contractVersion.
     *
     * @param contractVersion the contractVersion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractVersion, or with status {@code 400 (Bad Request)} if the contractVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contract-versions")
    public ResponseEntity<ContractVersion> createContractVersion(@Valid @RequestBody ContractVersion contractVersion)
        throws URISyntaxException {
        log.debug("REST request to save ContractVersion : {}", contractVersion);
        if (contractVersion.getId() != null) {
            throw new BadRequestAlertException("A new contractVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractVersion result = contractVersionRepository.save(contractVersion);
        return ResponseEntity
            .created(new URI("/api/contract-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contract-versions/:id} : Updates an existing contractVersion.
     *
     * @param id the id of the contractVersion to save.
     * @param contractVersion the contractVersion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractVersion,
     * or with status {@code 400 (Bad Request)} if the contractVersion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractVersion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contract-versions/{id}")
    public ResponseEntity<ContractVersion> updateContractVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractVersion contractVersion
    ) throws URISyntaxException {
        log.debug("REST request to update ContractVersion : {}, {}", id, contractVersion);
        if (contractVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractVersion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContractVersion result = contractVersionRepository.save(contractVersion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractVersion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contract-versions/:id} : Partial updates given fields of an existing contractVersion, field will ignore if it is null
     *
     * @param id the id of the contractVersion to save.
     * @param contractVersion the contractVersion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractVersion,
     * or with status {@code 400 (Bad Request)} if the contractVersion is not valid,
     * or with status {@code 404 (Not Found)} if the contractVersion is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractVersion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contract-versions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractVersion> partialUpdateContractVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractVersion contractVersion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractVersion partially : {}, {}", id, contractVersion);
        if (contractVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractVersion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractVersion> result = contractVersionRepository
            .findById(contractVersion.getId())
            .map(existingContractVersion -> {
                if (contractVersion.getEffectiveDate() != null) {
                    existingContractVersion.setEffectiveDate(contractVersion.getEffectiveDate());
                }
                if (contractVersion.getExpirationDate() != null) {
                    existingContractVersion.setExpirationDate(contractVersion.getExpirationDate());
                }
                if (contractVersion.getHiddenContract() != null) {
                    existingContractVersion.setHiddenContract(contractVersion.getHiddenContract());
                }
                if (contractVersion.getVersionNumber() != null) {
                    existingContractVersion.setVersionNumber(contractVersion.getVersionNumber());
                }

                return existingContractVersion;
            })
            .map(contractVersionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractVersion.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-versions} : get all the contractVersions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractVersions in body.
     */
    @GetMapping("/contract-versions")
    public List<ContractVersion> getAllContractVersions() {
        log.debug("REST request to get all ContractVersions");
        return contractVersionRepository.findAll();
    }

    /**
     * {@code GET  /contract-versions/:id} : get the "id" contractVersion.
     *
     * @param id the id of the contractVersion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractVersion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contract-versions/{id}")
    public ResponseEntity<ContractVersion> getContractVersion(@PathVariable Long id) {
        log.debug("REST request to get ContractVersion : {}", id);
        Optional<ContractVersion> contractVersion = contractVersionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contractVersion);
    }

    /**
     * {@code DELETE  /contract-versions/:id} : delete the "id" contractVersion.
     *
     * @param id the id of the contractVersion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contract-versions/{id}")
    public ResponseEntity<Void> deleteContractVersion(@PathVariable Long id) {
        log.debug("REST request to delete ContractVersion : {}", id);
        contractVersionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
