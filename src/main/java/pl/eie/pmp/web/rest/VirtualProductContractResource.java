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
import pl.eie.pmp.domain.VirtualProductContract;
import pl.eie.pmp.repository.VirtualProductContractRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProductContract}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductContractResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductContractResource.class);

    private static final String ENTITY_NAME = "virtualProductContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductContractRepository virtualProductContractRepository;

    public VirtualProductContractResource(VirtualProductContractRepository virtualProductContractRepository) {
        this.virtualProductContractRepository = virtualProductContractRepository;
    }

    /**
     * {@code POST  /virtual-product-contracts} : Create a new virtualProductContract.
     *
     * @param virtualProductContract the virtualProductContract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProductContract, or with status {@code 400 (Bad Request)} if the virtualProductContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-product-contracts")
    public ResponseEntity<VirtualProductContract> createVirtualProductContract(
        @Valid @RequestBody VirtualProductContract virtualProductContract
    ) throws URISyntaxException {
        log.debug("REST request to save VirtualProductContract : {}", virtualProductContract);
        if (virtualProductContract.getId() != null) {
            throw new BadRequestAlertException("A new virtualProductContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProductContract result = virtualProductContractRepository.save(virtualProductContract);
        return ResponseEntity
            .created(new URI("/api/virtual-product-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-product-contracts/:id} : Updates an existing virtualProductContract.
     *
     * @param id the id of the virtualProductContract to save.
     * @param virtualProductContract the virtualProductContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductContract,
     * or with status {@code 400 (Bad Request)} if the virtualProductContract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-product-contracts/{id}")
    public ResponseEntity<VirtualProductContract> updateVirtualProductContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirtualProductContract virtualProductContract
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProductContract : {}, {}", id, virtualProductContract);
        if (virtualProductContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProductContract result = virtualProductContractRepository.save(virtualProductContract);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductContract.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-product-contracts/:id} : Partial updates given fields of an existing virtualProductContract, field will ignore if it is null
     *
     * @param id the id of the virtualProductContract to save.
     * @param virtualProductContract the virtualProductContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductContract,
     * or with status {@code 400 (Bad Request)} if the virtualProductContract is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProductContract is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-product-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProductContract> partialUpdateVirtualProductContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirtualProductContract virtualProductContract
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProductContract partially : {}, {}", id, virtualProductContract);
        if (virtualProductContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProductContract> result = virtualProductContractRepository
            .findById(virtualProductContract.getId())
            .map(existingVirtualProductContract -> {
                if (virtualProductContract.getAncestorItemIdentifier() != null) {
                    existingVirtualProductContract.setAncestorItemIdentifier(virtualProductContract.getAncestorItemIdentifier());
                }
                if (virtualProductContract.getItemIdentifier() != null) {
                    existingVirtualProductContract.setItemIdentifier(virtualProductContract.getItemIdentifier());
                }
                if (virtualProductContract.getPriority() != null) {
                    existingVirtualProductContract.setPriority(virtualProductContract.getPriority());
                }

                return existingVirtualProductContract;
            })
            .map(virtualProductContractRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductContract.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-product-contracts} : get all the virtualProductContracts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProductContracts in body.
     */
    @GetMapping("/virtual-product-contracts")
    public List<VirtualProductContract> getAllVirtualProductContracts(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all VirtualProductContracts");
        return virtualProductContractRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /virtual-product-contracts/:id} : get the "id" virtualProductContract.
     *
     * @param id the id of the virtualProductContract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProductContract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-product-contracts/{id}")
    public ResponseEntity<VirtualProductContract> getVirtualProductContract(@PathVariable Long id) {
        log.debug("REST request to get VirtualProductContract : {}", id);
        Optional<VirtualProductContract> virtualProductContract = virtualProductContractRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(virtualProductContract);
    }

    /**
     * {@code DELETE  /virtual-product-contracts/:id} : delete the "id" virtualProductContract.
     *
     * @param id the id of the virtualProductContract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-product-contracts/{id}")
    public ResponseEntity<Void> deleteVirtualProductContract(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProductContract : {}", id);
        virtualProductContractRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
