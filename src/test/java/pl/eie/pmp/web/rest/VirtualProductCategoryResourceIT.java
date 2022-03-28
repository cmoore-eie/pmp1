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
import pl.eie.pmp.domain.VirtualProductCategory;
import pl.eie.pmp.repository.VirtualProductCategoryRepository;

/**
 * Integration tests for the {@link VirtualProductCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductCategoryResourceIT {

    private static final String DEFAULT_ANCESTOR_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTOR_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/virtual-product-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductCategoryRepository virtualProductCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductCategoryMockMvc;

    private VirtualProductCategory virtualProductCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductCategory createEntity(EntityManager em) {
        VirtualProductCategory virtualProductCategory = new VirtualProductCategory()
            .ancestorItemIdentifier(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)
            .code(DEFAULT_CODE)
            .itemIdentifier(DEFAULT_ITEM_IDENTIFIER)
            .name(DEFAULT_NAME)
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
        virtualProductCategory.setVirtualProduct(virtualProduct);
        return virtualProductCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductCategory createUpdatedEntity(EntityManager em) {
        VirtualProductCategory virtualProductCategory = new VirtualProductCategory()
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME)
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
        virtualProductCategory.setVirtualProduct(virtualProduct);
        return virtualProductCategory;
    }

    @BeforeEach
    public void initTest() {
        virtualProductCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProductCategory() throws Exception {
        int databaseSizeBeforeCreate = virtualProductCategoryRepository.findAll().size();
        // Create the VirtualProductCategory
        restVirtualProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProductCategory testVirtualProductCategory = virtualProductCategoryList.get(virtualProductCategoryList.size() - 1);
        assertThat(testVirtualProductCategory.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVirtualProductCategory.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVirtualProductCategory.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createVirtualProductCategoryWithExistingId() throws Exception {
        // Create the VirtualProductCategory with an existing ID
        virtualProductCategory.setId(1L);

        int databaseSizeBeforeCreate = virtualProductCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProductCategories() throws Exception {
        // Initialize the database
        virtualProductCategoryRepository.saveAndFlush(virtualProductCategory);

        // Get all the virtualProductCategoryList
        restVirtualProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProductCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancestorItemIdentifier").value(hasItem(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].itemIdentifier").value(hasItem(DEFAULT_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getVirtualProductCategory() throws Exception {
        // Initialize the database
        virtualProductCategoryRepository.saveAndFlush(virtualProductCategory);

        // Get the virtualProductCategory
        restVirtualProductCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProductCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProductCategory.getId().intValue()))
            .andExpect(jsonPath("$.ancestorItemIdentifier").value(DEFAULT_ANCESTOR_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.itemIdentifier").value(DEFAULT_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProductCategory() throws Exception {
        // Get the virtualProductCategory
        restVirtualProductCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProductCategory() throws Exception {
        // Initialize the database
        virtualProductCategoryRepository.saveAndFlush(virtualProductCategory);

        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();

        // Update the virtualProductCategory
        VirtualProductCategory updatedVirtualProductCategory = virtualProductCategoryRepository
            .findById(virtualProductCategory.getId())
            .get();
        // Disconnect from session so that the updates on updatedVirtualProductCategory are not directly saved in db
        em.detach(updatedVirtualProductCategory);
        updatedVirtualProductCategory
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY);

        restVirtualProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProductCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductCategory testVirtualProductCategory = virtualProductCategoryList.get(virtualProductCategoryList.size() - 1);
        assertThat(testVirtualProductCategory.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductCategory.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductCategory.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();
        virtualProductCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProductCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();
        virtualProductCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();
        virtualProductCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductCategoryWithPatch() throws Exception {
        // Initialize the database
        virtualProductCategoryRepository.saveAndFlush(virtualProductCategory);

        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();

        // Update the virtualProductCategory using partial update
        VirtualProductCategory partialUpdatedVirtualProductCategory = new VirtualProductCategory();
        partialUpdatedVirtualProductCategory.setId(virtualProductCategory.getId());

        partialUpdatedVirtualProductCategory
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME);

        restVirtualProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductCategory testVirtualProductCategory = virtualProductCategoryList.get(virtualProductCategoryList.size() - 1);
        assertThat(testVirtualProductCategory.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductCategory.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductCategory.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductCategoryWithPatch() throws Exception {
        // Initialize the database
        virtualProductCategoryRepository.saveAndFlush(virtualProductCategory);

        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();

        // Update the virtualProductCategory using partial update
        VirtualProductCategory partialUpdatedVirtualProductCategory = new VirtualProductCategory();
        partialUpdatedVirtualProductCategory.setId(virtualProductCategory.getId());

        partialUpdatedVirtualProductCategory
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY);

        restVirtualProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductCategory testVirtualProductCategory = virtualProductCategoryList.get(virtualProductCategoryList.size() - 1);
        assertThat(testVirtualProductCategory.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductCategory.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductCategory.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();
        virtualProductCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProductCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();
        virtualProductCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductCategoryRepository.findAll().size();
        virtualProductCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductCategory in the database
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProductCategory() throws Exception {
        // Initialize the database
        virtualProductCategoryRepository.saveAndFlush(virtualProductCategory);

        int databaseSizeBeforeDelete = virtualProductCategoryRepository.findAll().size();

        // Delete the virtualProductCategory
        restVirtualProductCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProductCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProductCategory> virtualProductCategoryList = virtualProductCategoryRepository.findAll();
        assertThat(virtualProductCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
