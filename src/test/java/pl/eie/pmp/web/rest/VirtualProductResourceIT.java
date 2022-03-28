package pl.eie.pmp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import pl.eie.pmp.domain.enumeration.VirtualProductType;
import pl.eie.pmp.repository.VirtualProductRepository;

/**
 * Integration tests for the {@link VirtualProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductResourceIT {

    private static final Boolean DEFAULT_ALLOW_AFFINITY = false;
    private static final Boolean UPDATED_ALLOW_AFFINITY = true;

    private static final Boolean DEFAULT_ALLOW_CAMPAIGN = false;
    private static final Boolean UPDATED_ALLOW_CAMPAIGN = true;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_LOCKED = false;
    private static final Boolean UPDATED_LOCKED = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final VirtualProductType DEFAULT_VIRTUAL_PRODUCT_TYPE = VirtualProductType.OPEN;
    private static final VirtualProductType UPDATED_VIRTUAL_PRODUCT_TYPE = VirtualProductType.CLOSED;

    private static final String ENTITY_API_URL = "/api/virtual-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductRepository virtualProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductMockMvc;

    private VirtualProduct virtualProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProduct createEntity(EntityManager em) {
        VirtualProduct virtualProduct = new VirtualProduct()
            .allowAffinity(DEFAULT_ALLOW_AFFINITY)
            .allowCampaign(DEFAULT_ALLOW_CAMPAIGN)
            .code(DEFAULT_CODE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .locked(DEFAULT_LOCKED)
            .name(DEFAULT_NAME)
            .productCode(DEFAULT_PRODUCT_CODE)
            .virtualProductType(DEFAULT_VIRTUAL_PRODUCT_TYPE);
        return virtualProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProduct createUpdatedEntity(EntityManager em) {
        VirtualProduct virtualProduct = new VirtualProduct()
            .allowAffinity(UPDATED_ALLOW_AFFINITY)
            .allowCampaign(UPDATED_ALLOW_CAMPAIGN)
            .code(UPDATED_CODE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .locked(UPDATED_LOCKED)
            .name(UPDATED_NAME)
            .productCode(UPDATED_PRODUCT_CODE)
            .virtualProductType(UPDATED_VIRTUAL_PRODUCT_TYPE);
        return virtualProduct;
    }

    @BeforeEach
    public void initTest() {
        virtualProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProduct() throws Exception {
        int databaseSizeBeforeCreate = virtualProductRepository.findAll().size();
        // Create the VirtualProduct
        restVirtualProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProduct testVirtualProduct = virtualProductList.get(virtualProductList.size() - 1);
        assertThat(testVirtualProduct.getAllowAffinity()).isEqualTo(DEFAULT_ALLOW_AFFINITY);
        assertThat(testVirtualProduct.getAllowCampaign()).isEqualTo(DEFAULT_ALLOW_CAMPAIGN);
        assertThat(testVirtualProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVirtualProduct.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testVirtualProduct.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testVirtualProduct.getLocked()).isEqualTo(DEFAULT_LOCKED);
        assertThat(testVirtualProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVirtualProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testVirtualProduct.getVirtualProductType()).isEqualTo(DEFAULT_VIRTUAL_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void createVirtualProductWithExistingId() throws Exception {
        // Create the VirtualProduct with an existing ID
        virtualProduct.setId(1L);

        int databaseSizeBeforeCreate = virtualProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProducts() throws Exception {
        // Initialize the database
        virtualProductRepository.saveAndFlush(virtualProduct);

        // Get all the virtualProductList
        restVirtualProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].allowAffinity").value(hasItem(DEFAULT_ALLOW_AFFINITY.booleanValue())))
            .andExpect(jsonPath("$.[*].allowCampaign").value(hasItem(DEFAULT_ALLOW_CAMPAIGN.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].locked").value(hasItem(DEFAULT_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].virtualProductType").value(hasItem(DEFAULT_VIRTUAL_PRODUCT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getVirtualProduct() throws Exception {
        // Initialize the database
        virtualProductRepository.saveAndFlush(virtualProduct);

        // Get the virtualProduct
        restVirtualProductMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProduct.getId().intValue()))
            .andExpect(jsonPath("$.allowAffinity").value(DEFAULT_ALLOW_AFFINITY.booleanValue()))
            .andExpect(jsonPath("$.allowCampaign").value(DEFAULT_ALLOW_CAMPAIGN.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.locked").value(DEFAULT_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.virtualProductType").value(DEFAULT_VIRTUAL_PRODUCT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProduct() throws Exception {
        // Get the virtualProduct
        restVirtualProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProduct() throws Exception {
        // Initialize the database
        virtualProductRepository.saveAndFlush(virtualProduct);

        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();

        // Update the virtualProduct
        VirtualProduct updatedVirtualProduct = virtualProductRepository.findById(virtualProduct.getId()).get();
        // Disconnect from session so that the updates on updatedVirtualProduct are not directly saved in db
        em.detach(updatedVirtualProduct);
        updatedVirtualProduct
            .allowAffinity(UPDATED_ALLOW_AFFINITY)
            .allowCampaign(UPDATED_ALLOW_CAMPAIGN)
            .code(UPDATED_CODE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .locked(UPDATED_LOCKED)
            .name(UPDATED_NAME)
            .productCode(UPDATED_PRODUCT_CODE)
            .virtualProductType(UPDATED_VIRTUAL_PRODUCT_TYPE);

        restVirtualProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProduct.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProduct))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
        VirtualProduct testVirtualProduct = virtualProductList.get(virtualProductList.size() - 1);
        assertThat(testVirtualProduct.getAllowAffinity()).isEqualTo(UPDATED_ALLOW_AFFINITY);
        assertThat(testVirtualProduct.getAllowCampaign()).isEqualTo(UPDATED_ALLOW_CAMPAIGN);
        assertThat(testVirtualProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProduct.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testVirtualProduct.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testVirtualProduct.getLocked()).isEqualTo(UPDATED_LOCKED);
        assertThat(testVirtualProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testVirtualProduct.getVirtualProductType()).isEqualTo(UPDATED_VIRTUAL_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProduct() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();
        virtualProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProduct.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProduct() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();
        virtualProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProduct() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();
        virtualProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductWithPatch() throws Exception {
        // Initialize the database
        virtualProductRepository.saveAndFlush(virtualProduct);

        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();

        // Update the virtualProduct using partial update
        VirtualProduct partialUpdatedVirtualProduct = new VirtualProduct();
        partialUpdatedVirtualProduct.setId(virtualProduct.getId());

        partialUpdatedVirtualProduct.allowAffinity(UPDATED_ALLOW_AFFINITY).code(UPDATED_CODE).name(UPDATED_NAME);

        restVirtualProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProduct))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
        VirtualProduct testVirtualProduct = virtualProductList.get(virtualProductList.size() - 1);
        assertThat(testVirtualProduct.getAllowAffinity()).isEqualTo(UPDATED_ALLOW_AFFINITY);
        assertThat(testVirtualProduct.getAllowCampaign()).isEqualTo(DEFAULT_ALLOW_CAMPAIGN);
        assertThat(testVirtualProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProduct.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testVirtualProduct.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testVirtualProduct.getLocked()).isEqualTo(DEFAULT_LOCKED);
        assertThat(testVirtualProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testVirtualProduct.getVirtualProductType()).isEqualTo(DEFAULT_VIRTUAL_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductWithPatch() throws Exception {
        // Initialize the database
        virtualProductRepository.saveAndFlush(virtualProduct);

        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();

        // Update the virtualProduct using partial update
        VirtualProduct partialUpdatedVirtualProduct = new VirtualProduct();
        partialUpdatedVirtualProduct.setId(virtualProduct.getId());

        partialUpdatedVirtualProduct
            .allowAffinity(UPDATED_ALLOW_AFFINITY)
            .allowCampaign(UPDATED_ALLOW_CAMPAIGN)
            .code(UPDATED_CODE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .locked(UPDATED_LOCKED)
            .name(UPDATED_NAME)
            .productCode(UPDATED_PRODUCT_CODE)
            .virtualProductType(UPDATED_VIRTUAL_PRODUCT_TYPE);

        restVirtualProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProduct))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
        VirtualProduct testVirtualProduct = virtualProductList.get(virtualProductList.size() - 1);
        assertThat(testVirtualProduct.getAllowAffinity()).isEqualTo(UPDATED_ALLOW_AFFINITY);
        assertThat(testVirtualProduct.getAllowCampaign()).isEqualTo(UPDATED_ALLOW_CAMPAIGN);
        assertThat(testVirtualProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProduct.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testVirtualProduct.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testVirtualProduct.getLocked()).isEqualTo(UPDATED_LOCKED);
        assertThat(testVirtualProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testVirtualProduct.getVirtualProductType()).isEqualTo(UPDATED_VIRTUAL_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProduct() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();
        virtualProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProduct() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();
        virtualProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProduct() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductRepository.findAll().size();
        virtualProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProduct in the database
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProduct() throws Exception {
        // Initialize the database
        virtualProductRepository.saveAndFlush(virtualProduct);

        int databaseSizeBeforeDelete = virtualProductRepository.findAll().size();

        // Delete the virtualProduct
        restVirtualProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProduct.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProduct> virtualProductList = virtualProductRepository.findAll();
        assertThat(virtualProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
