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
import pl.eie.pmp.domain.Agreement;
import pl.eie.pmp.repository.AgreementRepository;
import pl.eie.pmp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.eie.pmp.domain.Agreement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AgreementResource {

    private final Logger log = LoggerFactory.getLogger(AgreementResource.class);

    private static final String ENTITY_NAME = "agreement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgreementRepository agreementRepository;

    public AgreementResource(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    /**
     * {@code POST  /agreements} : Create a new agreement.
     *
     * @param agreement the agreement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agreement, or with status {@code 400 (Bad Request)} if the agreement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agreements")
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) throws URISyntaxException {
        log.debug("REST request to save Agreement : {}", agreement);
        if (agreement.getId() != null) {
            throw new BadRequestAlertException("A new agreement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agreement result = agreementRepository.save(agreement);
        return ResponseEntity
            .created(new URI("/api/agreements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agreements/:id} : Updates an existing agreement.
     *
     * @param id the id of the agreement to save.
     * @param agreement the agreement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agreement,
     * or with status {@code 400 (Bad Request)} if the agreement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agreement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agreements/{id}")
    public ResponseEntity<Agreement> updateAgreement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Agreement agreement
    ) throws URISyntaxException {
        log.debug("REST request to update Agreement : {}, {}", id, agreement);
        if (agreement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agreement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agreementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Agreement result = agreementRepository.save(agreement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agreement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agreements/:id} : Partial updates given fields of an existing agreement, field will ignore if it is null
     *
     * @param id the id of the agreement to save.
     * @param agreement the agreement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agreement,
     * or with status {@code 400 (Bad Request)} if the agreement is not valid,
     * or with status {@code 404 (Not Found)} if the agreement is not found,
     * or with status {@code 500 (Internal Server Error)} if the agreement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agreements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Agreement> partialUpdateAgreement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Agreement agreement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agreement partially : {}, {}", id, agreement);
        if (agreement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agreement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agreementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Agreement> result = agreementRepository
            .findById(agreement.getId())
            .map(existingAgreement -> {
                if (agreement.getCancelReason() != null) {
                    existingAgreement.setCancelReason(agreement.getCancelReason());
                }

                return existingAgreement;
            })
            .map(agreementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agreement.getId().toString())
        );
    }

    /**
     * {@code GET  /agreements} : get all the agreements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agreements in body.
     */
    @GetMapping("/agreements")
    public List<Agreement> getAllAgreements() {
        log.debug("REST request to get all Agreements");
        return agreementRepository.findAll();
    }

    /**
     * {@code GET  /agreements/:id} : get the "id" agreement.
     *
     * @param id the id of the agreement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agreement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agreements/{id}")
    public ResponseEntity<Agreement> getAgreement(@PathVariable Long id) {
        log.debug("REST request to get Agreement : {}", id);
        Optional<Agreement> agreement = agreementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agreement);
    }

    /**
     * {@code DELETE  /agreements/:id} : delete the "id" agreement.
     *
     * @param id the id of the agreement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agreements/{id}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable Long id) {
        log.debug("REST request to delete Agreement : {}", id);
        agreementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
