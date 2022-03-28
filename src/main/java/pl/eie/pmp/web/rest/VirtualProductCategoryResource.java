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
import pl.eie.pmp.domain.VirtualProductCategory;
import pl.eie.pmp.repository.VirtualProductCategoryRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProductCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductCategoryResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductCategoryResource.class);

    private static final String ENTITY_NAME = "virtualProductCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductCategoryRepository virtualProductCategoryRepository;

    public VirtualProductCategoryResource(VirtualProductCategoryRepository virtualProductCategoryRepository) {
        this.virtualProductCategoryRepository = virtualProductCategoryRepository;
    }

    /**
     * {@code POST  /virtual-product-categories} : Create a new virtualProductCategory.
     *
     * @param virtualProductCategory the virtualProductCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProductCategory, or with status {@code 400 (Bad Request)} if the virtualProductCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-product-categories")
    public ResponseEntity<VirtualProductCategory> createVirtualProductCategory(
        @Valid @RequestBody VirtualProductCategory virtualProductCategory
    ) throws URISyntaxException {
        log.debug("REST request to save VirtualProductCategory : {}", virtualProductCategory);
        if (virtualProductCategory.getId() != null) {
            throw new BadRequestAlertException("A new virtualProductCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProductCategory result = virtualProductCategoryRepository.save(virtualProductCategory);
        return ResponseEntity
            .created(new URI("/api/virtual-product-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-product-categories/:id} : Updates an existing virtualProductCategory.
     *
     * @param id the id of the virtualProductCategory to save.
     * @param virtualProductCategory the virtualProductCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductCategory,
     * or with status {@code 400 (Bad Request)} if the virtualProductCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-product-categories/{id}")
    public ResponseEntity<VirtualProductCategory> updateVirtualProductCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirtualProductCategory virtualProductCategory
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProductCategory : {}, {}", id, virtualProductCategory);
        if (virtualProductCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProductCategory result = virtualProductCategoryRepository.save(virtualProductCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-product-categories/:id} : Partial updates given fields of an existing virtualProductCategory, field will ignore if it is null
     *
     * @param id the id of the virtualProductCategory to save.
     * @param virtualProductCategory the virtualProductCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductCategory,
     * or with status {@code 400 (Bad Request)} if the virtualProductCategory is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProductCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-product-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProductCategory> partialUpdateVirtualProductCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirtualProductCategory virtualProductCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProductCategory partially : {}, {}", id, virtualProductCategory);
        if (virtualProductCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProductCategory> result = virtualProductCategoryRepository
            .findById(virtualProductCategory.getId())
            .map(existingVirtualProductCategory -> {
                if (virtualProductCategory.getAncestorItemIdentifier() != null) {
                    existingVirtualProductCategory.setAncestorItemIdentifier(virtualProductCategory.getAncestorItemIdentifier());
                }
                if (virtualProductCategory.getCode() != null) {
                    existingVirtualProductCategory.setCode(virtualProductCategory.getCode());
                }
                if (virtualProductCategory.getItemIdentifier() != null) {
                    existingVirtualProductCategory.setItemIdentifier(virtualProductCategory.getItemIdentifier());
                }
                if (virtualProductCategory.getName() != null) {
                    existingVirtualProductCategory.setName(virtualProductCategory.getName());
                }
                if (virtualProductCategory.getPriority() != null) {
                    existingVirtualProductCategory.setPriority(virtualProductCategory.getPriority());
                }

                return existingVirtualProductCategory;
            })
            .map(virtualProductCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-product-categories} : get all the virtualProductCategories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProductCategories in body.
     */
    @GetMapping("/virtual-product-categories")
    public List<VirtualProductCategory> getAllVirtualProductCategories(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all VirtualProductCategories");
        return virtualProductCategoryRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /virtual-product-categories/:id} : get the "id" virtualProductCategory.
     *
     * @param id the id of the virtualProductCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProductCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-product-categories/{id}")
    public ResponseEntity<VirtualProductCategory> getVirtualProductCategory(@PathVariable Long id) {
        log.debug("REST request to get VirtualProductCategory : {}", id);
        Optional<VirtualProductCategory> virtualProductCategory = virtualProductCategoryRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(virtualProductCategory);
    }

    /**
     * {@code DELETE  /virtual-product-categories/:id} : delete the "id" virtualProductCategory.
     *
     * @param id the id of the virtualProductCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-product-categories/{id}")
    public ResponseEntity<Void> deleteVirtualProductCategory(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProductCategory : {}", id);
        virtualProductCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
