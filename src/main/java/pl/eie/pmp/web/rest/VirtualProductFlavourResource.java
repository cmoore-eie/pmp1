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
import pl.eie.pmp.domain.VirtualProductFlavour;
import pl.eie.pmp.repository.VirtualProductFlavourRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProductFlavour}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductFlavourResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductFlavourResource.class);

    private static final String ENTITY_NAME = "virtualProductFlavour";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductFlavourRepository virtualProductFlavourRepository;

    public VirtualProductFlavourResource(VirtualProductFlavourRepository virtualProductFlavourRepository) {
        this.virtualProductFlavourRepository = virtualProductFlavourRepository;
    }

    /**
     * {@code POST  /virtual-product-flavours} : Create a new virtualProductFlavour.
     *
     * @param virtualProductFlavour the virtualProductFlavour to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProductFlavour, or with status {@code 400 (Bad Request)} if the virtualProductFlavour has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-product-flavours")
    public ResponseEntity<VirtualProductFlavour> createVirtualProductFlavour(
        @Valid @RequestBody VirtualProductFlavour virtualProductFlavour
    ) throws URISyntaxException {
        log.debug("REST request to save VirtualProductFlavour : {}", virtualProductFlavour);
        if (virtualProductFlavour.getId() != null) {
            throw new BadRequestAlertException("A new virtualProductFlavour cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProductFlavour result = virtualProductFlavourRepository.save(virtualProductFlavour);
        return ResponseEntity
            .created(new URI("/api/virtual-product-flavours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-product-flavours/:id} : Updates an existing virtualProductFlavour.
     *
     * @param id the id of the virtualProductFlavour to save.
     * @param virtualProductFlavour the virtualProductFlavour to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductFlavour,
     * or with status {@code 400 (Bad Request)} if the virtualProductFlavour is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductFlavour couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-product-flavours/{id}")
    public ResponseEntity<VirtualProductFlavour> updateVirtualProductFlavour(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirtualProductFlavour virtualProductFlavour
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProductFlavour : {}, {}", id, virtualProductFlavour);
        if (virtualProductFlavour.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductFlavour.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductFlavourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProductFlavour result = virtualProductFlavourRepository.save(virtualProductFlavour);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductFlavour.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-product-flavours/:id} : Partial updates given fields of an existing virtualProductFlavour, field will ignore if it is null
     *
     * @param id the id of the virtualProductFlavour to save.
     * @param virtualProductFlavour the virtualProductFlavour to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductFlavour,
     * or with status {@code 400 (Bad Request)} if the virtualProductFlavour is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProductFlavour is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductFlavour couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-product-flavours/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProductFlavour> partialUpdateVirtualProductFlavour(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirtualProductFlavour virtualProductFlavour
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProductFlavour partially : {}, {}", id, virtualProductFlavour);
        if (virtualProductFlavour.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductFlavour.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductFlavourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProductFlavour> result = virtualProductFlavourRepository
            .findById(virtualProductFlavour.getId())
            .map(existingVirtualProductFlavour -> {
                if (virtualProductFlavour.getAncestorItemIdentifier() != null) {
                    existingVirtualProductFlavour.setAncestorItemIdentifier(virtualProductFlavour.getAncestorItemIdentifier());
                }
                if (virtualProductFlavour.getCode() != null) {
                    existingVirtualProductFlavour.setCode(virtualProductFlavour.getCode());
                }
                if (virtualProductFlavour.getCondition() != null) {
                    existingVirtualProductFlavour.setCondition(virtualProductFlavour.getCondition());
                }
                if (virtualProductFlavour.getDefaultFlavour() != null) {
                    existingVirtualProductFlavour.setDefaultFlavour(virtualProductFlavour.getDefaultFlavour());
                }
                if (virtualProductFlavour.getEffectiveDate() != null) {
                    existingVirtualProductFlavour.setEffectiveDate(virtualProductFlavour.getEffectiveDate());
                }
                if (virtualProductFlavour.getExpirationDate() != null) {
                    existingVirtualProductFlavour.setExpirationDate(virtualProductFlavour.getExpirationDate());
                }
                if (virtualProductFlavour.getGrandfathering() != null) {
                    existingVirtualProductFlavour.setGrandfathering(virtualProductFlavour.getGrandfathering());
                }
                if (virtualProductFlavour.getItemIdentifier() != null) {
                    existingVirtualProductFlavour.setItemIdentifier(virtualProductFlavour.getItemIdentifier());
                }
                if (virtualProductFlavour.getLineCode() != null) {
                    existingVirtualProductFlavour.setLineCode(virtualProductFlavour.getLineCode());
                }
                if (virtualProductFlavour.getName() != null) {
                    existingVirtualProductFlavour.setName(virtualProductFlavour.getName());
                }
                if (virtualProductFlavour.getPriority() != null) {
                    existingVirtualProductFlavour.setPriority(virtualProductFlavour.getPriority());
                }
                if (virtualProductFlavour.getRank() != null) {
                    existingVirtualProductFlavour.setRank(virtualProductFlavour.getRank());
                }

                return existingVirtualProductFlavour;
            })
            .map(virtualProductFlavourRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductFlavour.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-product-flavours} : get all the virtualProductFlavours.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProductFlavours in body.
     */
    @GetMapping("/virtual-product-flavours")
    public List<VirtualProductFlavour> getAllVirtualProductFlavours(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all VirtualProductFlavours");
        return virtualProductFlavourRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /virtual-product-flavours/:id} : get the "id" virtualProductFlavour.
     *
     * @param id the id of the virtualProductFlavour to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProductFlavour, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-product-flavours/{id}")
    public ResponseEntity<VirtualProductFlavour> getVirtualProductFlavour(@PathVariable Long id) {
        log.debug("REST request to get VirtualProductFlavour : {}", id);
        Optional<VirtualProductFlavour> virtualProductFlavour = virtualProductFlavourRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(virtualProductFlavour);
    }

    /**
     * {@code DELETE  /virtual-product-flavours/:id} : delete the "id" virtualProductFlavour.
     *
     * @param id the id of the virtualProductFlavour to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-product-flavours/{id}")
    public ResponseEntity<Void> deleteVirtualProductFlavour(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProductFlavour : {}", id);
        virtualProductFlavourRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
