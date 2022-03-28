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
import pl.eie.pmp.domain.VirtualProduct;
import pl.eie.pmp.repository.VirtualProductRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.VirtualProduct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VirtualProductResource {

    private final Logger log = LoggerFactory.getLogger(VirtualProductResource.class);

    private static final String ENTITY_NAME = "virtualProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirtualProductRepository virtualProductRepository;

    public VirtualProductResource(VirtualProductRepository virtualProductRepository) {
        this.virtualProductRepository = virtualProductRepository;
    }

    /**
     * {@code POST  /virtual-products} : Create a new virtualProduct.
     *
     * @param virtualProduct the virtualProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virtualProduct, or with status {@code 400 (Bad Request)} if the virtualProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virtual-products")
    public ResponseEntity<VirtualProduct> createVirtualProduct(@RequestBody VirtualProduct virtualProduct) throws URISyntaxException {
        log.debug("REST request to save VirtualProduct : {}", virtualProduct);
        if (virtualProduct.getId() != null) {
            throw new BadRequestAlertException("A new virtualProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirtualProduct result = virtualProductRepository.save(virtualProduct);
        return ResponseEntity
            .created(new URI("/api/virtual-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virtual-products/:id} : Updates an existing virtualProduct.
     *
     * @param id the id of the virtualProduct to save.
     * @param virtualProduct the virtualProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProduct,
     * or with status {@code 400 (Bad Request)} if the virtualProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virtualProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virtual-products/{id}")
    public ResponseEntity<VirtualProduct> updateVirtualProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VirtualProduct virtualProduct
    ) throws URISyntaxException {
        log.debug("REST request to update VirtualProduct : {}, {}", id, virtualProduct);
        if (virtualProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirtualProduct result = virtualProductRepository.save(virtualProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virtual-products/:id} : Partial updates given fields of an existing virtualProduct, field will ignore if it is null
     *
     * @param id the id of the virtualProduct to save.
     * @param virtualProduct the virtualProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virtualProduct,
     * or with status {@code 400 (Bad Request)} if the virtualProduct is not valid,
     * or with status {@code 404 (Not Found)} if the virtualProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the virtualProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virtual-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirtualProduct> partialUpdateVirtualProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VirtualProduct virtualProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update VirtualProduct partially : {}, {}", id, virtualProduct);
        if (virtualProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virtualProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virtualProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirtualProduct> result = virtualProductRepository
            .findById(virtualProduct.getId())
            .map(existingVirtualProduct -> {
                if (virtualProduct.getAllowAffinity() != null) {
                    existingVirtualProduct.setAllowAffinity(virtualProduct.getAllowAffinity());
                }
                if (virtualProduct.getAllowCampaign() != null) {
                    existingVirtualProduct.setAllowCampaign(virtualProduct.getAllowCampaign());
                }
                if (virtualProduct.getCode() != null) {
                    existingVirtualProduct.setCode(virtualProduct.getCode());
                }
                if (virtualProduct.getEffectiveDate() != null) {
                    existingVirtualProduct.setEffectiveDate(virtualProduct.getEffectiveDate());
                }
                if (virtualProduct.getExpirationDate() != null) {
                    existingVirtualProduct.setExpirationDate(virtualProduct.getExpirationDate());
                }
                if (virtualProduct.getLocked() != null) {
                    existingVirtualProduct.setLocked(virtualProduct.getLocked());
                }
                if (virtualProduct.getName() != null) {
                    existingVirtualProduct.setName(virtualProduct.getName());
                }
                if (virtualProduct.getProductCode() != null) {
                    existingVirtualProduct.setProductCode(virtualProduct.getProductCode());
                }
                if (virtualProduct.getVirtualProductType() != null) {
                    existingVirtualProduct.setVirtualProductType(virtualProduct.getVirtualProductType());
                }

                return existingVirtualProduct;
            })
            .map(virtualProductRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, virtualProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /virtual-products} : get all the virtualProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virtualProducts in body.
     */
    @GetMapping("/virtual-products")
    public List<VirtualProduct> getAllVirtualProducts() {
        log.debug("REST request to get all VirtualProducts");
        return virtualProductRepository.findAll();
    }

    /**
     * {@code GET  /virtual-products/:id} : get the "id" virtualProduct.
     *
     * @param id the id of the virtualProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virtualProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virtual-products/{id}")
    public ResponseEntity<VirtualProduct> getVirtualProduct(@PathVariable Long id) {
        log.debug("REST request to get VirtualProduct : {}", id);
        Optional<VirtualProduct> virtualProduct = virtualProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(virtualProduct);
    }

    /**
     * {@code DELETE  /virtual-products/:id} : delete the "id" virtualProduct.
     *
     * @param id the id of the virtualProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virtual-products/{id}")
    public ResponseEntity<Void> deleteVirtualProduct(@PathVariable Long id) {
        log.debug("REST request to delete VirtualProduct : {}", id);
        virtualProductRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
