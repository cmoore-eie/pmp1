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
import pl.eie.pmp.domain.VirtualProductFlavour;
import pl.eie.pmp.domain.VirtualProductSeasoning;
import pl.eie.pmp.repository.VirtualProductSeasoningRepository;

/**
 * Integration tests for the {@link VirtualProductSeasoningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductSeasoningResourceIT {

    private static final String DEFAULT_ANCESTOR_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTOR_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_SEASONING = false;
    private static final Boolean UPDATED_DEFAULT_SEASONING = true;

    private static final String DEFAULT_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/virtual-product-seasonings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductSeasoningRepository virtualProductSeasoningRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductSeasoningMockMvc;

    private VirtualProductSeasoning virtualProductSeasoning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductSeasoning createEntity(EntityManager em) {
        VirtualProductSeasoning virtualProductSeasoning = new VirtualProductSeasoning()
            .ancestorItemIdentifier(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)
            .code(DEFAULT_CODE)
            .condition(DEFAULT_CONDITION)
            .defaultSeasoning(DEFAULT_DEFAULT_SEASONING)
            .itemIdentifier(DEFAULT_ITEM_IDENTIFIER)
            .name(DEFAULT_NAME)
            .priority(DEFAULT_PRIORITY);
        // Add required entity
        VirtualProductFlavour virtualProductFlavour;
        if (TestUtil.findAll(em, VirtualProductFlavour.class).isEmpty()) {
            virtualProductFlavour = VirtualProductFlavourResourceIT.createEntity(em);
            em.persist(virtualProductFlavour);
            em.flush();
        } else {
            virtualProductFlavour = TestUtil.findAll(em, VirtualProductFlavour.class).get(0);
        }
        virtualProductSeasoning.setVirtualProductFlavour(virtualProductFlavour);
        return virtualProductSeasoning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductSeasoning createUpdatedEntity(EntityManager em) {
        VirtualProductSeasoning virtualProductSeasoning = new VirtualProductSeasoning()
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .condition(UPDATED_CONDITION)
            .defaultSeasoning(UPDATED_DEFAULT_SEASONING)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY);
        // Add required entity
        VirtualProductFlavour virtualProductFlavour;
        if (TestUtil.findAll(em, VirtualProductFlavour.class).isEmpty()) {
            virtualProductFlavour = VirtualProductFlavourResourceIT.createUpdatedEntity(em);
            em.persist(virtualProductFlavour);
            em.flush();
        } else {
            virtualProductFlavour = TestUtil.findAll(em, VirtualProductFlavour.class).get(0);
        }
        virtualProductSeasoning.setVirtualProductFlavour(virtualProductFlavour);
        return virtualProductSeasoning;
    }

    @BeforeEach
    public void initTest() {
        virtualProductSeasoning = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeCreate = virtualProductSeasoningRepository.findAll().size();
        // Create the VirtualProductSeasoning
        restVirtualProductSeasoningMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProductSeasoning testVirtualProductSeasoning = virtualProductSeasoningList.get(virtualProductSeasoningList.size() - 1);
        assertThat(testVirtualProductSeasoning.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVirtualProductSeasoning.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testVirtualProductSeasoning.getDefaultSeasoning()).isEqualTo(DEFAULT_DEFAULT_SEASONING);
        assertThat(testVirtualProductSeasoning.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVirtualProductSeasoning.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createVirtualProductSeasoningWithExistingId() throws Exception {
        // Create the VirtualProductSeasoning with an existing ID
        virtualProductSeasoning.setId(1L);

        int databaseSizeBeforeCreate = virtualProductSeasoningRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductSeasoningMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProductSeasonings() throws Exception {
        // Initialize the database
        virtualProductSeasoningRepository.saveAndFlush(virtualProductSeasoning);

        // Get all the virtualProductSeasoningList
        restVirtualProductSeasoningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProductSeasoning.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancestorItemIdentifier").value(hasItem(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION)))
            .andExpect(jsonPath("$.[*].defaultSeasoning").value(hasItem(DEFAULT_DEFAULT_SEASONING.booleanValue())))
            .andExpect(jsonPath("$.[*].itemIdentifier").value(hasItem(DEFAULT_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getVirtualProductSeasoning() throws Exception {
        // Initialize the database
        virtualProductSeasoningRepository.saveAndFlush(virtualProductSeasoning);

        // Get the virtualProductSeasoning
        restVirtualProductSeasoningMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProductSeasoning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProductSeasoning.getId().intValue()))
            .andExpect(jsonPath("$.ancestorItemIdentifier").value(DEFAULT_ANCESTOR_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION))
            .andExpect(jsonPath("$.defaultSeasoning").value(DEFAULT_DEFAULT_SEASONING.booleanValue()))
            .andExpect(jsonPath("$.itemIdentifier").value(DEFAULT_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProductSeasoning() throws Exception {
        // Get the virtualProductSeasoning
        restVirtualProductSeasoningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProductSeasoning() throws Exception {
        // Initialize the database
        virtualProductSeasoningRepository.saveAndFlush(virtualProductSeasoning);

        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();

        // Update the virtualProductSeasoning
        VirtualProductSeasoning updatedVirtualProductSeasoning = virtualProductSeasoningRepository
            .findById(virtualProductSeasoning.getId())
            .get();
        // Disconnect from session so that the updates on updatedVirtualProductSeasoning are not directly saved in db
        em.detach(updatedVirtualProductSeasoning);
        updatedVirtualProductSeasoning
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .condition(UPDATED_CONDITION)
            .defaultSeasoning(UPDATED_DEFAULT_SEASONING)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY);

        restVirtualProductSeasoningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProductSeasoning.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProductSeasoning))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductSeasoning testVirtualProductSeasoning = virtualProductSeasoningList.get(virtualProductSeasoningList.size() - 1);
        assertThat(testVirtualProductSeasoning.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductSeasoning.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testVirtualProductSeasoning.getDefaultSeasoning()).isEqualTo(UPDATED_DEFAULT_SEASONING);
        assertThat(testVirtualProductSeasoning.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductSeasoning.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();
        virtualProductSeasoning.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductSeasoningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProductSeasoning.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();
        virtualProductSeasoning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductSeasoningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();
        virtualProductSeasoning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductSeasoningMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductSeasoningWithPatch() throws Exception {
        // Initialize the database
        virtualProductSeasoningRepository.saveAndFlush(virtualProductSeasoning);

        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();

        // Update the virtualProductSeasoning using partial update
        VirtualProductSeasoning partialUpdatedVirtualProductSeasoning = new VirtualProductSeasoning();
        partialUpdatedVirtualProductSeasoning.setId(virtualProductSeasoning.getId());

        partialUpdatedVirtualProductSeasoning.defaultSeasoning(UPDATED_DEFAULT_SEASONING).itemIdentifier(UPDATED_ITEM_IDENTIFIER);

        restVirtualProductSeasoningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductSeasoning.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductSeasoning))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductSeasoning testVirtualProductSeasoning = virtualProductSeasoningList.get(virtualProductSeasoningList.size() - 1);
        assertThat(testVirtualProductSeasoning.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVirtualProductSeasoning.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testVirtualProductSeasoning.getDefaultSeasoning()).isEqualTo(UPDATED_DEFAULT_SEASONING);
        assertThat(testVirtualProductSeasoning.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVirtualProductSeasoning.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductSeasoningWithPatch() throws Exception {
        // Initialize the database
        virtualProductSeasoningRepository.saveAndFlush(virtualProductSeasoning);

        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();

        // Update the virtualProductSeasoning using partial update
        VirtualProductSeasoning partialUpdatedVirtualProductSeasoning = new VirtualProductSeasoning();
        partialUpdatedVirtualProductSeasoning.setId(virtualProductSeasoning.getId());

        partialUpdatedVirtualProductSeasoning
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .condition(UPDATED_CONDITION)
            .defaultSeasoning(UPDATED_DEFAULT_SEASONING)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY);

        restVirtualProductSeasoningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductSeasoning.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductSeasoning))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductSeasoning testVirtualProductSeasoning = virtualProductSeasoningList.get(virtualProductSeasoningList.size() - 1);
        assertThat(testVirtualProductSeasoning.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductSeasoning.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testVirtualProductSeasoning.getDefaultSeasoning()).isEqualTo(UPDATED_DEFAULT_SEASONING);
        assertThat(testVirtualProductSeasoning.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductSeasoning.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductSeasoning.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();
        virtualProductSeasoning.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductSeasoningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProductSeasoning.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();
        virtualProductSeasoning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductSeasoningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProductSeasoning() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductSeasoningRepository.findAll().size();
        virtualProductSeasoning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductSeasoningMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductSeasoning))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductSeasoning in the database
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProductSeasoning() throws Exception {
        // Initialize the database
        virtualProductSeasoningRepository.saveAndFlush(virtualProductSeasoning);

        int databaseSizeBeforeDelete = virtualProductSeasoningRepository.findAll().size();

        // Delete the virtualProductSeasoning
        restVirtualProductSeasoningMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProductSeasoning.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProductSeasoning> virtualProductSeasoningList = virtualProductSeasoningRepository.findAll();
        assertThat(virtualProductSeasoningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
