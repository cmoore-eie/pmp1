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
import pl.eie.pmp.domain.VirtualProductContract;
import pl.eie.pmp.repository.VirtualProductContractRepository;

/**
 * Integration tests for the {@link VirtualProductContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductContractResourceIT {

    private static final String DEFAULT_ANCESTOR_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTOR_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/virtual-product-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductContractRepository virtualProductContractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductContractMockMvc;

    private VirtualProductContract virtualProductContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductContract createEntity(EntityManager em) {
        VirtualProductContract virtualProductContract = new VirtualProductContract()
            .ancestorItemIdentifier(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(DEFAULT_ITEM_IDENTIFIER)
            .priority(DEFAULT_PRIORITY);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductContract.setVirtualProduct(virtualProduct);
        return virtualProductContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductContract createUpdatedEntity(EntityManager em) {
        VirtualProductContract virtualProductContract = new VirtualProductContract()
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .priority(UPDATED_PRIORITY);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createUpdatedEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductContract.setVirtualProduct(virtualProduct);
        return virtualProductContract;
    }

    @BeforeEach
    public void initTest() {
        virtualProductContract = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProductContract() throws Exception {
        int databaseSizeBeforeCreate = virtualProductContractRepository.findAll().size();
        // Create the VirtualProductContract
        restVirtualProductContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProductContract testVirtualProductContract = virtualProductContractList.get(virtualProductContractList.size() - 1);
        assertThat(testVirtualProductContract.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createVirtualProductContractWithExistingId() throws Exception {
        // Create the VirtualProductContract with an existing ID
        virtualProductContract.setId(1L);

        int databaseSizeBeforeCreate = virtualProductContractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProductContracts() throws Exception {
        // Initialize the database
        virtualProductContractRepository.saveAndFlush(virtualProductContract);

        // Get all the virtualProductContractList
        restVirtualProductContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProductContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancestorItemIdentifier").value(hasItem(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].itemIdentifier").value(hasItem(DEFAULT_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getVirtualProductContract() throws Exception {
        // Initialize the database
        virtualProductContractRepository.saveAndFlush(virtualProductContract);

        // Get the virtualProductContract
        restVirtualProductContractMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProductContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProductContract.getId().intValue()))
            .andExpect(jsonPath("$.ancestorItemIdentifier").value(DEFAULT_ANCESTOR_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.itemIdentifier").value(DEFAULT_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProductContract() throws Exception {
        // Get the virtualProductContract
        restVirtualProductContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProductContract() throws Exception {
        // Initialize the database
        virtualProductContractRepository.saveAndFlush(virtualProductContract);

        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();

        // Update the virtualProductContract
        VirtualProductContract updatedVirtualProductContract = virtualProductContractRepository
            .findById(virtualProductContract.getId())
            .get();
        // Disconnect from session so that the updates on updatedVirtualProductContract are not directly saved in db
        em.detach(updatedVirtualProductContract);
        updatedVirtualProductContract
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .priority(UPDATED_PRIORITY);

        restVirtualProductContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProductContract.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProductContract))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductContract testVirtualProductContract = virtualProductContractList.get(virtualProductContractList.size() - 1);
        assertThat(testVirtualProductContract.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProductContract() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();
        virtualProductContract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProductContract.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProductContract() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();
        virtualProductContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProductContract() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();
        virtualProductContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductContractMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductContractWithPatch() throws Exception {
        // Initialize the database
        virtualProductContractRepository.saveAndFlush(virtualProductContract);

        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();

        // Update the virtualProductContract using partial update
        VirtualProductContract partialUpdatedVirtualProductContract = new VirtualProductContract();
        partialUpdatedVirtualProductContract.setId(virtualProductContract.getId());

        partialUpdatedVirtualProductContract.itemIdentifier(UPDATED_ITEM_IDENTIFIER).priority(UPDATED_PRIORITY);

        restVirtualProductContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductContract))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductContract testVirtualProductContract = virtualProductContractList.get(virtualProductContractList.size() - 1);
        assertThat(testVirtualProductContract.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductContractWithPatch() throws Exception {
        // Initialize the database
        virtualProductContractRepository.saveAndFlush(virtualProductContract);

        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();

        // Update the virtualProductContract using partial update
        VirtualProductContract partialUpdatedVirtualProductContract = new VirtualProductContract();
        partialUpdatedVirtualProductContract.setId(virtualProductContract.getId());

        partialUpdatedVirtualProductContract
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .priority(UPDATED_PRIORITY);

        restVirtualProductContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductContract))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductContract testVirtualProductContract = virtualProductContractList.get(virtualProductContractList.size() - 1);
        assertThat(testVirtualProductContract.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductContract.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProductContract() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();
        virtualProductContract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProductContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProductContract() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();
        virtualProductContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProductContract() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductContractRepository.findAll().size();
        virtualProductContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductContractMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductContract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductContract in the database
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProductContract() throws Exception {
        // Initialize the database
        virtualProductContractRepository.saveAndFlush(virtualProductContract);

        int databaseSizeBeforeDelete = virtualProductContractRepository.findAll().size();

        // Delete the virtualProductContract
        restVirtualProductContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProductContract.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProductContract> virtualProductContractList = virtualProductContractRepository.findAll();
        assertThat(virtualProductContractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
