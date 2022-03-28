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
import pl.eie.pmp.domain.VirtualProductLine;
import pl.eie.pmp.repository.VirtualProductLineRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProductLine}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductLineResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductLineResource.class);

    private static final String ENTITY_NAME = "virtualProductLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductLineRepository virtualProductLineRepository;

    public VirtualProductLineResource(VirtualProductLineRepository virtualProductLineRepository) {
        this.virtualProductLineRepository = virtualProductLineRepository;
    }

    /**
     * {@code POST  /virtual-product-lines} : Create a new virtualProductLine.
     *
     * @param virtualProductLine the virtualProductLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProductLine, or with status {@code 400 (Bad Request)} if the virtualProductLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-product-lines")
    public ResponseEntity<VirtualProductLine> createVirtualProductLine(@Valid @RequestBody VirtualProductLine virtualProductLine)
        throws URISyntaxException {
        log.debug("REST request to save VirtualProductLine : {}", virtualProductLine);
        if (virtualProductLine.getId() != null) {
            throw new BadRequestAlertException("A new virtualProductLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProductLine result = virtualProductLineRepository.save(virtualProductLine);
        return ResponseEntity
            .created(new URI("/api/virtual-product-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-product-lines/:id} : Updates an existing virtualProductLine.
     *
     * @param id the id of the virtualProductLine to save.
     * @param virtualProductLine the virtualProductLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductLine,
     * or with status {@code 400 (Bad Request)} if the virtualProductLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-product-lines/{id}")
    public ResponseEntity<VirtualProductLine> updateVirtualProductLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirtualProductLine virtualProductLine
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProductLine : {}, {}", id, virtualProductLine);
        if (virtualProductLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProductLine result = virtualProductLineRepository.save(virtualProductLine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductLine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-product-lines/:id} : Partial updates given fields of an existing virtualProductLine, field will ignore if it is null
     *
     * @param id the id of the virtualProductLine to save.
     * @param virtualProductLine the virtualProductLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProductLine,
     * or with status {@code 400 (Bad Request)} if the virtualProductLine is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProductLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProductLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-product-lines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProductLine> partialUpdateVirtualProductLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirtualProductLine virtualProductLine
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProductLine partially : {}, {}", id, virtualProductLine);
        if (virtualProductLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProductLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProductLine> result = virtualProductLineRepository
            .findById(virtualProductLine.getId())
            .map(existingVirtualProductLine -> {
                if (virtualProductLine.getAncestorItemIdentifier() != null) {
                    existingVirtualProductLine.setAncestorItemIdentifier(virtualProductLine.getAncestorItemIdentifier());
                }
                if (virtualProductLine.getItemIdentifier() != null) {
                    existingVirtualProductLine.setItemIdentifier(virtualProductLine.getItemIdentifier());
                }
                if (virtualProductLine.getLineAvailable() != null) {
                    existingVirtualProductLine.setLineAvailable(virtualProductLine.getLineAvailable());
                }
                if (virtualProductLine.getLineCode() != null) {
                    existingVirtualProductLine.setLineCode(virtualProductLine.getLineCode());
                }

                return existingVirtualProductLine;
            })
            .map(virtualProductLineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProductLine.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-product-lines} : get all the virtualProductLines.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProductLines in body.
     */
    @GetMapping("/virtual-product-lines")
    public List<VirtualProductLine> getAllVirtualProductLines(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all VirtualProductLines");
        return virtualProductLineRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /virtual-product-lines/:id} : get the "id" virtualProductLine.
     *
     * @param id the id of the virtualProductLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProductLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-product-lines/{id}")
    public ResponseEntity<VirtualProductLine> getVirtualProductLine(@PathVariable Long id) {
        log.debug("REST request to get VirtualProductLine : {}", id);
        Optional<VirtualProductLine> virtualProductLine = virtualProductLineRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(virtualProductLine);
    }

    /**
     * {@code DELETE  /virtual-product-lines/:id} : delete the "id" virtualProductLine.
     *
     * @param id the id of the virtualProductLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-product-lines/{id}")
    public ResponseEntity<Void> deleteVirtualProductLine(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProductLine : {}", id);
        virtualProductLineRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
