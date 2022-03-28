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
import pl.eie.pmp.domain.VirtualProductSeasoning;
import pl.eie.pmp.repository.VirtualProductSeasoningRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProductSeasoning}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductSeasoningResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductSeasoningResource.class);

    private static final String ENTITY_NAME = "virtualProductSeasoning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductSeasoningRepository virtualProductSeasoningRepository;

    public VirtualProductSeasoningResource(VirtualProductSeasoningRepository virtualProductSeasoningRepository) {
        this.virtualProductSeasoningRepository = virtualProductSeasoningRepository;
    }

    /**
     * {@code POST  /virtual-product-seasonings} : Create a new virtualProductSeasoning.
     *
     * @param virtualProductSeasoning the virtualProductSeasoning to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProductSeasoning, or with status {@code 400 (Bad Request)} if the virtualProductSeasoning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-product-seasonings")
    public ResponseEntity<VirtualProductSeasoning> createVirtualProductSeasoning(
        @Valid @RequestBody VirtualProductSeasoning virtualProductSeasoning
    ) throws URISyntaxException {
        log.debug("REST request to save VirtualProductSeasoning : {}", virtualProductSeasoning);
        if (virtualProductSeasoning.getId() != null) {
            throw new BadRequestAlertException("A new virtualProductSeasoning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProductSeasoning result = virtualProductSeasoningRepository.save(virtualProductSeasoning);
        return ResponseEntity
            .created(new URI("/api/virtual-product-seasonings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-product-seasonings/:id} : Updates an existing virtualProductSeasoning.
     *
     * @param id the id of the virtualProductSeasoning to save.
     * @param virtualProductSeasoning the virtualProductSeasoning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductSeasoning,
     * or with status {@code 400 (Bad Request)} if the virtualProductSeasoning is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductSeasoning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-product-seasonings/{id}")
    public ResponseEntity<VirtualProductSeasoning> updateVirtualProductSeasoning(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirtualProductSeasoning virtualProductSeasoning
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProductSeasoning : {}, {}", id, virtualProductSeasoning);
        if (virtualProductSeasoning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductSeasoning.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductSeasoningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProductSeasoning result = virtualProductSeasoningRepository.save(virtualProductSeasoning);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductSeasoning.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-product-seasonings/:id} : Partial updates given fields of an existing virtualProductSeasoning, field will ignore if it is null
     *
     * @param id the id of the virtualProductSeasoning to save.
     * @param virtualProductSeasoning the virtualProductSeasoning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductSeasoning,
     * or with status {@code 400 (Bad Request)} if the virtualProductSeasoning is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProductSeasoning is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductSeasoning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-product-seasonings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProductSeasoning> partialUpdateVirtualProductSeasoning(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirtualProductSeasoning virtualProductSeasoning
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProductSeasoning partially : {}, {}", id, virtualProductSeasoning);
        if (virtualProductSeasoning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductSeasoning.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductSeasoningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProductSeasoning> result = virtualProductSeasoningRepository
            .findById(virtualProductSeasoning.getId())
            .map(existingVirtualProductSeasoning -> {
                if (virtualProductSeasoning.getAncestorItemIdentifier() != null) {
                    existingVirtualProductSeasoning.setAncestorItemIdentifier(virtualProductSeasoning.getAncestorItemIdentifier());
                }
                if (virtualProductSeasoning.getCode() != null) {
                    existingVirtualProductSeasoning.setCode(virtualProductSeasoning.getCode());
                }
                if (virtualProductSeasoning.getCondition() != null) {
                    existingVirtualProductSeasoning.setCondition(virtualProductSeasoning.getCondition());
                }
                if (virtualProductSeasoning.getDefaultSeasoning() != null) {
                    existingVirtualProductSeasoning.setDefaultSeasoning(virtualProductSeasoning.getDefaultSeasoning());
                }
                if (virtualProductSeasoning.getItemIdentifier() != null) {
                    existingVirtualProductSeasoning.setItemIdentifier(virtualProductSeasoning.getItemIdentifier());
                }
                if (virtualProductSeasoning.getName() != null) {
                    existingVirtualProductSeasoning.setName(virtualProductSeasoning.getName());
                }
                if (virtualProductSeasoning.getPriority() != null) {
                    existingVirtualProductSeasoning.setPriority(virtualProductSeasoning.getPriority());
                }

                return existingVirtualProductSeasoning;
            })
            .map(virtualProductSeasoningRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductSeasoning.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-product-seasonings} : get all the virtualProductSeasonings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProductSeasonings in body.
     */
    @GetMapping("/virtual-product-seasonings")
    public List<VirtualProductSeasoning> getAllVirtualProductSeasonings() {
        log.debug("REST request to get all VirtualProductSeasonings");
        return virtualProductSeasoningRepository.findAll();
    }

    /**
     * {@code GET  /virtual-product-seasonings/:id} : get the "id" virtualProductSeasoning.
     *
     * @param id the id of the virtualProductSeasoning to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProductSeasoning, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-product-seasonings/{id}")
    public ResponseEntity<VirtualProductSeasoning> getVirtualProductSeasoning(@PathVariable Long id) {
        log.debug("REST request to get VirtualProductSeasoning : {}", id);
        Optional<VirtualProductSeasoning> virtualProductSeasoning = virtualProductSeasoningRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(virtualProductSeasoning);
    }

    /**
     * {@code DELETE  /virtual-product-seasonings/:id} : delete the "id" virtualProductSeasoning.
     *
     * @param id the id of the virtualProductSeasoning to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-product-seasonings/{id}")
    public ResponseEntity<Void> deleteVirtualProductSeasoning(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProductSeasoning : {}", id);
        virtualProductSeasoningRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
