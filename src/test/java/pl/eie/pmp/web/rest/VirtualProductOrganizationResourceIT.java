package pl.eie.pmp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.eie.pmp.IntegrationTest;
import pl.eie.pmp.domain.VirtualProduct;
import pl.eie.pmp.domain.VirtualProductOrganization;
import pl.eie.pmp.repository.VirtualProductOrganizationRepository;

/**
 * Integration tests for the {@link VirtualProductOrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductOrganizationResourceIT {

    private static final String DEFAULT_ANCESTOR_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTOR_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/virtual-product-organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductOrganizationRepository virtualProductOrganizationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductOrganizationMockMvc;

    private VirtualProductOrganization virtualProductOrganization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductOrganization createEntity(EntityManager em) {
        VirtualProductOrganization virtualProductOrganization = new VirtualProductOrganization()
            .ancestorItemIdentifier(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(DEFAULT_ITEM_IDENTIFIER)
            .organization(DEFAULT_ORGANIZATION)
            .producerCode(DEFAULT_PRODUCER_CODE);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductOrganization.setVirtualProduct(virtualProduct);
        return virtualProductOrganization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductOrganization createUpdatedEntity(EntityManager em) {
        VirtualProductOrganization virtualProductOrganization = new VirtualProductOrganization()
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .organization(UPDATED_ORGANIZATION)
            .producerCode(UPDATED_PRODUCER_CODE);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createUpdatedEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductOrganization.setVirtualProduct(virtualProduct);
        return virtualProductOrganization;
    }

    @BeforeEach
    public void initTest() {
        virtualProductOrganization = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeCreate = virtualProductOrganizationRepository.findAll().size();
        // Create the VirtualProductOrganization
        restVirtualProductOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProductOrganization testVirtualProductOrganization = virtualProductOrganizationList.get(
            virtualProductOrganizationList.size() - 1
        );
        assertThat(testVirtualProductOrganization.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getOrganization()).isEqualTo(DEFAULT_ORGANIZATION);
        assertThat(testVirtualProductOrganization.getProducerCode()).isEqualTo(DEFAULT_PRODUCER_CODE);
    }

    @Test
    @Transactional
    void createVirtualProductOrganizationWithExistingId() throws Exception {
        // Create the VirtualProductOrganization with an existing ID
        virtualProductOrganization.setId(1L);

        int databaseSizeBeforeCreate = virtualProductOrganizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProductOrganizations() throws Exception {
        // Initialize the database
        virtualProductOrganizationRepository.saveAndFlush(virtualProductOrganization);

        // Get all the virtualProductOrganizationList
        restVirtualProductOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProductOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancestorItemIdentifier").value(hasItem(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].itemIdentifier").value(hasItem(DEFAULT_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_ORGANIZATION)))
            .andExpect(jsonPath("$.[*].producerCode").value(hasItem(DEFAULT_PRODUCER_CODE)));
    }

    @Test
    @Transactional
    void getVirtualProductOrganization() throws Exception {
        // Initialize the database
        virtualProductOrganizationRepository.saveAndFlush(virtualProductOrganization);

        // Get the virtualProductOrganization
        restVirtualProductOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProductOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProductOrganization.getId().intValue()))
            .andExpect(jsonPath("$.ancestorItemIdentifier").value(DEFAULT_ANCESTOR_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.itemIdentifier").value(DEFAULT_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.organization").value(DEFAULT_ORGANIZATION))
            .andExpect(jsonPath("$.producerCode").value(DEFAULT_PRODUCER_CODE));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProductOrganization() throws Exception {
        // Get the virtualProductOrganization
        restVirtualProductOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProductOrganization() throws Exception {
        // Initialize the database
        virtualProductOrganizationRepository.saveAndFlush(virtualProductOrganization);

        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();

        // Update the virtualProductOrganization
        VirtualProductOrganization updatedVirtualProductOrganization = virtualProductOrganizationRepository
            .findById(virtualProductOrganization.getId())
            .get();
        // Disconnect from session so that the updates on updatedVirtualProductOrganization are not directly saved in db
        em.detach(updatedVirtualProductOrganization);
        updatedVirtualProductOrganization
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .organization(UPDATED_ORGANIZATION)
            .producerCode(UPDATED_PRODUCER_CODE);

        restVirtualProductOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProductOrganization.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProductOrganization))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductOrganization testVirtualProductOrganization = virtualProductOrganizationList.get(
            virtualProductOrganizationList.size() - 1
        );
        assertThat(testVirtualProductOrganization.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testVirtualProductOrganization.getProducerCode()).isEqualTo(UPDATED_PRODUCER_CODE);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();
        virtualProductOrganization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProductOrganization.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();
        virtualProductOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();
        virtualProductOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductOrganizationWithPatch() throws Exception {
        // Initialize the database
        virtualProductOrganizationRepository.saveAndFlush(virtualProductOrganization);

        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();

        // Update the virtualProductOrganization using partial update
        VirtualProductOrganization partialUpdatedVirtualProductOrganization = new VirtualProductOrganization();
        partialUpdatedVirtualProductOrganization.setId(virtualProductOrganization.getId());

        partialUpdatedVirtualProductOrganization
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .organization(UPDATED_ORGANIZATION)
            .producerCode(UPDATED_PRODUCER_CODE);

        restVirtualProductOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductOrganization.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductOrganization))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductOrganization testVirtualProductOrganization = virtualProductOrganizationList.get(
            virtualProductOrganizationList.size() - 1
        );
        assertThat(testVirtualProductOrganization.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testVirtualProductOrganization.getProducerCode()).isEqualTo(UPDATED_PRODUCER_CODE);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductOrganizationWithPatch() throws Exception {
        // Initialize the database
        virtualProductOrganizationRepository.saveAndFlush(virtualProductOrganization);

        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();

        // Update the virtualProductOrganization using partial update
        VirtualProductOrganization partialUpdatedVirtualProductOrganization = new VirtualProductOrganization();
        partialUpdatedVirtualProductOrganization.setId(virtualProductOrganization.getId());

        partialUpdatedVirtualProductOrganization
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .organization(UPDATED_ORGANIZATION)
            .producerCode(UPDATED_PRODUCER_CODE);

        restVirtualProductOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductOrganization.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductOrganization))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductOrganization testVirtualProductOrganization = virtualProductOrganizationList.get(
            virtualProductOrganizationList.size() - 1
        );
        assertThat(testVirtualProductOrganization.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductOrganization.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testVirtualProductOrganization.getProducerCode()).isEqualTo(UPDATED_PRODUCER_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();
        virtualProductOrganization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProductOrganization.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();
        virtualProductOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProductOrganization() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductOrganizationRepository.findAll().size();
        virtualProductOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductOrganization))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductOrganization in the database
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProductOrganization() throws Exception {
        // Initialize the database
        virtualProductOrganizationRepository.saveAndFlush(virtualProductOrganization);

        int databaseSizeBeforeDelete = virtualProductOrganizationRepository.findAll().size();

        // Delete the virtualProductOrganization
        restVirtualProductOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProductOrganization.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProductOrganization> virtualProductOrganizationList = virtualProductOrganizationRepository.findAll();
        assertThat(virtualProductOrganizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
