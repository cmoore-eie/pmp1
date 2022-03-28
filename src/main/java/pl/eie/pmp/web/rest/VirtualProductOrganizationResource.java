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
import pl.eie.pmp.domain.VirtualProductOrganization;
import pl.eie.pmp.repository.VirtualProductOrganizationRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProductOrganization}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductOrganizationResource.class);

    private static final String ENTITY_NAME = "virtualProductOrganization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductOrganizationRepository virtualProductOrganizationRepository;

    public VirtualProductOrganizationResource(VirtualProductOrganizationRepository virtualProductOrganizationRepository) {
        this.virtualProductOrganizationRepository = virtualProductOrganizationRepository;
    }

    /**
     * {@code POST  /virtual-product-organizations} : Create a new virtualProductOrganization.
     *
     * @param virtualProductOrganization the virtualProductOrganization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProductOrganization, or with status {@code 400 (Bad Request)} if the virtualProductOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-product-organizations")
    public ResponseEntity<VirtualProductOrganization> createVirtualProductOrganization(
        @Valid @RequestBody VirtualProductOrganization virtualProductOrganization
    ) throws URISyntaxException {
        log.debug("REST request to save VirtualProductOrganization : {}", virtualProductOrganization);
        if (virtualProductOrganization.getId() != null) {
            throw new BadRequestAlertException("A new virtualProductOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProductOrganization result = virtualProductOrganizationRepository.save(virtualProductOrganization);
        return ResponseEntity
            .created(new URI("/api/virtual-product-organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-product-organizations/:id} : Updates an existing virtualProductOrganization.
     *
     * @param id the id of the virtualProductOrganization to save.
     * @param virtualProductOrganization the virtualProductOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductOrganization,
     * or with status {@code 400 (Bad Request)} if the virtualProductOrganization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-product-organizations/{id}")
    public ResponseEntity<VirtualProductOrganization> updateVirtualProductOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirtualProductOrganization virtualProductOrganization
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProductOrganization : {}, {}", id, virtualProductOrganization);
        if (virtualProductOrganization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductOrganization.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProductOrganization result = virtualProductOrganizationRepository.save(virtualProductOrganization);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductOrganization.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-product-organizations/:id} : Partial updates given fields of an existing virtualProductOrganization, field will ignore if it is null
     *
     * @param id the id of the virtualProductOrganization to save.
     * @param virtualProductOrganization the virtualProductOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductOrganization,
     * or with status {@code 400 (Bad Request)} if the virtualProductOrganization is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProductOrganization is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-product-organizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProductOrganization> partialUpdateVirtualProductOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirtualProductOrganization virtualProductOrganization
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProductOrganization partially : {}, {}", id, virtualProductOrganization);
        if (virtualProductOrganization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductOrganization.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProductOrganization> result = virtualProductOrganizationRepository
            .findById(virtualProductOrganization.getId())
            .map(existingVirtualProductOrganization -> {
                if (virtualProductOrganization.getAncestorItemIdentifier() != null) {
                    existingVirtualProductOrganization.setAncestorItemIdentifier(virtualProductOrganization.getAncestorItemIdentifier());
                }
                if (virtualProductOrganization.getItemIdentifier() != null) {
                    existingVirtualProductOrganization.setItemIdentifier(virtualProductOrganization.getItemIdentifier());
                }
                if (virtualProductOrganization.getOrganization() != null) {
                    existingVirtualProductOrganization.setOrganization(virtualProductOrganization.getOrganization());
                }
                if (virtualProductOrganization.getProducerCode() != null) {
                    existingVirtualProductOrganization.setProducerCode(virtualProductOrganization.getProducerCode());
                }

                return existingVirtualProductOrganization;
            })
            .map(virtualProductOrganizationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductOrganization.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-product-organizations} : get all the virtualProductOrganizations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProductOrganizations in body.
     */
    @GetMapping("/virtual-product-organizations")
    public List<VirtualProductOrganization> getAllVirtualProductOrganizations(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all VirtualProductOrganizations");
        return virtualProductOrganizationRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /virtual-product-organizations/:id} : get the "id" virtualProductOrganization.
     *
     * @param id the id of the virtualProductOrganization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProductOrganization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-product-organizations/{id}")
    public ResponseEntity<VirtualProductOrganization> getVirtualProductOrganization(@PathVariable Long id) {
        log.debug("REST request to get VirtualProductOrganization : {}", id);
        Optional<VirtualProductOrganization> virtualProductOrganization = virtualProductOrganizationRepository.findOneWithEagerRelationships(
            id
        );
        return ResponseUtil.wrapOrNotFound(virtualProductOrganization);
    }

    /**
     * {@code DELETE  /virtual-product-organizations/:id} : delete the "id" virtualProductOrganization.
     *
     * @param id the id of the virtualProductOrganization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-product-organizations/{id}")
    public ResponseEntity<Void> deleteVirtualProductOrganization(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProductOrganization : {}", id);
        virtualProductOrganizationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
