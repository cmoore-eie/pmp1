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
import pl.eie.pmp.domain.VirtualProductFlavour;
import pl.eie.pmp.domain.enumeration.VirtualFlavourAction;
import pl.eie.pmp.repository.VirtualProductFlavourRepository;

/**
 * Integration tests for the {@link VirtualProductFlavourResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductFlavourResourceIT {

    private static final String DEFAULT_ANCESTOR_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTOR_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_FLAVOUR = false;
    private static final Boolean UPDATED_DEFAULT_FLAVOUR = true;

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final VirtualFlavourAction DEFAULT_GRANDFATHERING = VirtualFlavourAction.GRANDFATHERSTD;
    private static final VirtualFlavourAction UPDATED_GRANDFATHERING = VirtualFlavourAction.GRANDFATHERASIS;

    private static final String DEFAULT_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LINE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final String ENTITY_API_URL = "/api/virtual-product-flavours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductFlavourRepository virtualProductFlavourRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductFlavourMockMvc;

    private VirtualProductFlavour virtualProductFlavour;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductFlavour createEntity(EntityManager em) {
        VirtualProductFlavour virtualProductFlavour = new VirtualProductFlavour()
            .ancestorItemIdentifier(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)
            .code(DEFAULT_CODE)
            .condition(DEFAULT_CONDITION)
            .defaultFlavour(DEFAULT_DEFAULT_FLAVOUR)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .grandfathering(DEFAULT_GRANDFATHERING)
            .itemIdentifier(DEFAULT_ITEM_IDENTIFIER)
            .lineCode(DEFAULT_LINE_CODE)
            .name(DEFAULT_NAME)
            .priority(DEFAULT_PRIORITY)
            .rank(DEFAULT_RANK);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductFlavour.setVirtualProduct(virtualProduct);
        return virtualProductFlavour;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductFlavour createUpdatedEntity(EntityManager em) {
        VirtualProductFlavour virtualProductFlavour = new VirtualProductFlavour()
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .condition(UPDATED_CONDITION)
            .defaultFlavour(UPDATED_DEFAULT_FLAVOUR)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .grandfathering(UPDATED_GRANDFATHERING)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .lineCode(UPDATED_LINE_CODE)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY)
            .rank(UPDATED_RANK);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createUpdatedEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductFlavour.setVirtualProduct(virtualProduct);
        return virtualProductFlavour;
    }

    @BeforeEach
    public void initTest() {
        virtualProductFlavour = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeCreate = virtualProductFlavourRepository.findAll().size();
        // Create the VirtualProductFlavour
        restVirtualProductFlavourMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProductFlavour testVirtualProductFlavour = virtualProductFlavourList.get(virtualProductFlavourList.size() - 1);
        assertThat(testVirtualProductFlavour.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVirtualProductFlavour.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testVirtualProductFlavour.getDefaultFlavour()).isEqualTo(DEFAULT_DEFAULT_FLAVOUR);
        assertThat(testVirtualProductFlavour.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testVirtualProductFlavour.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testVirtualProductFlavour.getGrandfathering()).isEqualTo(DEFAULT_GRANDFATHERING);
        assertThat(testVirtualProductFlavour.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getLineCode()).isEqualTo(DEFAULT_LINE_CODE);
        assertThat(testVirtualProductFlavour.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVirtualProductFlavour.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testVirtualProductFlavour.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void createVirtualProductFlavourWithExistingId() throws Exception {
        // Create the VirtualProductFlavour with an existing ID
        virtualProductFlavour.setId(1L);

        int databaseSizeBeforeCreate = virtualProductFlavourRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductFlavourMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProductFlavours() throws Exception {
        // Initialize the database
        virtualProductFlavourRepository.saveAndFlush(virtualProductFlavour);

        // Get all the virtualProductFlavourList
        restVirtualProductFlavourMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProductFlavour.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancestorItemIdentifier").value(hasItem(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION)))
            .andExpect(jsonPath("$.[*].defaultFlavour").value(hasItem(DEFAULT_DEFAULT_FLAVOUR.booleanValue())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].grandfathering").value(hasItem(DEFAULT_GRANDFATHERING.toString())))
            .andExpect(jsonPath("$.[*].itemIdentifier").value(hasItem(DEFAULT_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].lineCode").value(hasItem(DEFAULT_LINE_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)));
    }

    @Test
    @Transactional
    void getVirtualProductFlavour() throws Exception {
        // Initialize the database
        virtualProductFlavourRepository.saveAndFlush(virtualProductFlavour);

        // Get the virtualProductFlavour
        restVirtualProductFlavourMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProductFlavour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProductFlavour.getId().intValue()))
            .andExpect(jsonPath("$.ancestorItemIdentifier").value(DEFAULT_ANCESTOR_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION))
            .andExpect(jsonPath("$.defaultFlavour").value(DEFAULT_DEFAULT_FLAVOUR.booleanValue()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.grandfathering").value(DEFAULT_GRANDFATHERING.toString()))
            .andExpect(jsonPath("$.itemIdentifier").value(DEFAULT_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.lineCode").value(DEFAULT_LINE_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProductFlavour() throws Exception {
        // Get the virtualProductFlavour
        restVirtualProductFlavourMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProductFlavour() throws Exception {
        // Initialize the database
        virtualProductFlavourRepository.saveAndFlush(virtualProductFlavour);

        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();

        // Update the virtualProductFlavour
        VirtualProductFlavour updatedVirtualProductFlavour = virtualProductFlavourRepository.findById(virtualProductFlavour.getId()).get();
        // Disconnect from session so that the updates on updatedVirtualProductFlavour are not directly saved in db
        em.detach(updatedVirtualProductFlavour);
        updatedVirtualProductFlavour
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .condition(UPDATED_CONDITION)
            .defaultFlavour(UPDATED_DEFAULT_FLAVOUR)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .grandfathering(UPDATED_GRANDFATHERING)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .lineCode(UPDATED_LINE_CODE)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY)
            .rank(UPDATED_RANK);

        restVirtualProductFlavourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProductFlavour.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProductFlavour))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductFlavour testVirtualProductFlavour = virtualProductFlavourList.get(virtualProductFlavourList.size() - 1);
        assertThat(testVirtualProductFlavour.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductFlavour.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testVirtualProductFlavour.getDefaultFlavour()).isEqualTo(UPDATED_DEFAULT_FLAVOUR);
        assertThat(testVirtualProductFlavour.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testVirtualProductFlavour.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testVirtualProductFlavour.getGrandfathering()).isEqualTo(UPDATED_GRANDFATHERING);
        assertThat(testVirtualProductFlavour.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getLineCode()).isEqualTo(UPDATED_LINE_CODE);
        assertThat(testVirtualProductFlavour.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductFlavour.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testVirtualProductFlavour.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();
        virtualProductFlavour.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductFlavourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProductFlavour.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();
        virtualProductFlavour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductFlavourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();
        virtualProductFlavour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductFlavourMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductFlavourWithPatch() throws Exception {
        // Initialize the database
        virtualProductFlavourRepository.saveAndFlush(virtualProductFlavour);

        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();

        // Update the virtualProductFlavour using partial update
        VirtualProductFlavour partialUpdatedVirtualProductFlavour = new VirtualProductFlavour();
        partialUpdatedVirtualProductFlavour.setId(virtualProductFlavour.getId());

        partialUpdatedVirtualProductFlavour
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .defaultFlavour(UPDATED_DEFAULT_FLAVOUR)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .priority(UPDATED_PRIORITY);

        restVirtualProductFlavourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductFlavour.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductFlavour))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductFlavour testVirtualProductFlavour = virtualProductFlavourList.get(virtualProductFlavourList.size() - 1);
        assertThat(testVirtualProductFlavour.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVirtualProductFlavour.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testVirtualProductFlavour.getDefaultFlavour()).isEqualTo(UPDATED_DEFAULT_FLAVOUR);
        assertThat(testVirtualProductFlavour.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testVirtualProductFlavour.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testVirtualProductFlavour.getGrandfathering()).isEqualTo(DEFAULT_GRANDFATHERING);
        assertThat(testVirtualProductFlavour.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getLineCode()).isEqualTo(DEFAULT_LINE_CODE);
        assertThat(testVirtualProductFlavour.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVirtualProductFlavour.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testVirtualProductFlavour.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductFlavourWithPatch() throws Exception {
        // Initialize the database
        virtualProductFlavourRepository.saveAndFlush(virtualProductFlavour);

        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();

        // Update the virtualProductFlavour using partial update
        VirtualProductFlavour partialUpdatedVirtualProductFlavour = new VirtualProductFlavour();
        partialUpdatedVirtualProductFlavour.setId(virtualProductFlavour.getId());

        partialUpdatedVirtualProductFlavour
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .code(UPDATED_CODE)
            .condition(UPDATED_CONDITION)
            .defaultFlavour(UPDATED_DEFAULT_FLAVOUR)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .grandfathering(UPDATED_GRANDFATHERING)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .lineCode(UPDATED_LINE_CODE)
            .name(UPDATED_NAME)
            .priority(UPDATED_PRIORITY)
            .rank(UPDATED_RANK);

        restVirtualProductFlavourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductFlavour.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductFlavour))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductFlavour testVirtualProductFlavour = virtualProductFlavourList.get(virtualProductFlavourList.size() - 1);
        assertThat(testVirtualProductFlavour.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVirtualProductFlavour.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testVirtualProductFlavour.getDefaultFlavour()).isEqualTo(UPDATED_DEFAULT_FLAVOUR);
        assertThat(testVirtualProductFlavour.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testVirtualProductFlavour.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testVirtualProductFlavour.getGrandfathering()).isEqualTo(UPDATED_GRANDFATHERING);
        assertThat(testVirtualProductFlavour.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductFlavour.getLineCode()).isEqualTo(UPDATED_LINE_CODE);
        assertThat(testVirtualProductFlavour.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVirtualProductFlavour.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testVirtualProductFlavour.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();
        virtualProductFlavour.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductFlavourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProductFlavour.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();
        virtualProductFlavour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductFlavourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProductFlavour() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductFlavourRepository.findAll().size();
        virtualProductFlavour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductFlavourMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductFlavour))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductFlavour in the database
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProductFlavour() throws Exception {
        // Initialize the database
        virtualProductFlavourRepository.saveAndFlush(virtualProductFlavour);

        int databaseSizeBeforeDelete = virtualProductFlavourRepository.findAll().size();

        // Delete the virtualProductFlavour
        restVirtualProductFlavourMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProductFlavour.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProductFlavour> virtualProductFlavourList = virtualProductFlavourRepository.findAll();
        assertThat(virtualProductFlavourList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
