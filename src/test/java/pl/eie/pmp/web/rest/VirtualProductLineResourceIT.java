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
import pl.eie.pmp.domain.VirtualProductLine;
import pl.eie.pmp.repository.VirtualProductLineRepository;

/**
 * Integration tests for the {@link VirtualProductLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirtualProductLineResourceIT {

    private static final String DEFAULT_ANCESTOR_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTOR_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_IDENTIFIER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LINE_AVAILABLE = false;
    private static final Boolean UPDATED_LINE_AVAILABLE = true;

    private static final String DEFAULT_LINE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LINE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/virtual-product-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirtualProductLineRepository virtualProductLineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirtualProductLineMockMvc;

    private VirtualProductLine virtualProductLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductLine createEntity(EntityManager em) {
        VirtualProductLine virtualProductLine = new VirtualProductLine()
            .ancestorItemIdentifier(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(DEFAULT_ITEM_IDENTIFIER)
            .lineAvailable(DEFAULT_LINE_AVAILABLE)
            .lineCode(DEFAULT_LINE_CODE);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductLine.setVirtualProduct(virtualProduct);
        return virtualProductLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VirtualProductLine createUpdatedEntity(EntityManager em) {
        VirtualProductLine virtualProductLine = new VirtualProductLine()
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .lineAvailable(UPDATED_LINE_AVAILABLE)
            .lineCode(UPDATED_LINE_CODE);
        // Add required entity
        VirtualProduct virtualProduct;
        if (TestUtil.findAll(em, VirtualProduct.class).isEmpty()) {
            virtualProduct = VirtualProductResourceIT.createUpdatedEntity(em);
            em.persist(virtualProduct);
            em.flush();
        } else {
            virtualProduct = TestUtil.findAll(em, VirtualProduct.class).get(0);
        }
        virtualProductLine.setVirtualProduct(virtualProduct);
        return virtualProductLine;
    }

    @BeforeEach
    public void initTest() {
        virtualProductLine = createEntity(em);
    }

    @Test
    @Transactional
    void createVirtualProductLine() throws Exception {
        int databaseSizeBeforeCreate = virtualProductLineRepository.findAll().size();
        // Create the VirtualProductLine
        restVirtualProductLineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isCreated());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeCreate + 1);
        VirtualProductLine testVirtualProductLine = virtualProductLineList.get(virtualProductLineList.size() - 1);
        assertThat(testVirtualProductLine.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getItemIdentifier()).isEqualTo(DEFAULT_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getLineAvailable()).isEqualTo(DEFAULT_LINE_AVAILABLE);
        assertThat(testVirtualProductLine.getLineCode()).isEqualTo(DEFAULT_LINE_CODE);
    }

    @Test
    @Transactional
    void createVirtualProductLineWithExistingId() throws Exception {
        // Create the VirtualProductLine with an existing ID
        virtualProductLine.setId(1L);

        int databaseSizeBeforeCreate = virtualProductLineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirtualProductLineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirtualProductLines() throws Exception {
        // Initialize the database
        virtualProductLineRepository.saveAndFlush(virtualProductLine);

        // Get all the virtualProductLineList
        restVirtualProductLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virtualProductLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancestorItemIdentifier").value(hasItem(DEFAULT_ANCESTOR_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].itemIdentifier").value(hasItem(DEFAULT_ITEM_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].lineAvailable").value(hasItem(DEFAULT_LINE_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].lineCode").value(hasItem(DEFAULT_LINE_CODE)));
    }

    @Test
    @Transactional
    void getVirtualProductLine() throws Exception {
        // Initialize the database
        virtualProductLineRepository.saveAndFlush(virtualProductLine);

        // Get the virtualProductLine
        restVirtualProductLineMockMvc
            .perform(get(ENTITY_API_URL_ID, virtualProductLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virtualProductLine.getId().intValue()))
            .andExpect(jsonPath("$.ancestorItemIdentifier").value(DEFAULT_ANCESTOR_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.itemIdentifier").value(DEFAULT_ITEM_IDENTIFIER))
            .andExpect(jsonPath("$.lineAvailable").value(DEFAULT_LINE_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.lineCode").value(DEFAULT_LINE_CODE));
    }

    @Test
    @Transactional
    void getNonExistingVirtualProductLine() throws Exception {
        // Get the virtualProductLine
        restVirtualProductLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirtualProductLine() throws Exception {
        // Initialize the database
        virtualProductLineRepository.saveAndFlush(virtualProductLine);

        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();

        // Update the virtualProductLine
        VirtualProductLine updatedVirtualProductLine = virtualProductLineRepository.findById(virtualProductLine.getId()).get();
        // Disconnect from session so that the updates on updatedVirtualProductLine are not directly saved in db
        em.detach(updatedVirtualProductLine);
        updatedVirtualProductLine
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .lineAvailable(UPDATED_LINE_AVAILABLE)
            .lineCode(UPDATED_LINE_CODE);

        restVirtualProductLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVirtualProductLine.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVirtualProductLine))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductLine testVirtualProductLine = virtualProductLineList.get(virtualProductLineList.size() - 1);
        assertThat(testVirtualProductLine.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getLineAvailable()).isEqualTo(UPDATED_LINE_AVAILABLE);
        assertThat(testVirtualProductLine.getLineCode()).isEqualTo(UPDATED_LINE_CODE);
    }

    @Test
    @Transactional
    void putNonExistingVirtualProductLine() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();
        virtualProductLine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virtualProductLine.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirtualProductLine() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();
        virtualProductLine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirtualProductLine() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();
        virtualProductLine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductLineMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirtualProductLineWithPatch() throws Exception {
        // Initialize the database
        virtualProductLineRepository.saveAndFlush(virtualProductLine);

        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();

        // Update the virtualProductLine using partial update
        VirtualProductLine partialUpdatedVirtualProductLine = new VirtualProductLine();
        partialUpdatedVirtualProductLine.setId(virtualProductLine.getId());

        partialUpdatedVirtualProductLine.itemIdentifier(UPDATED_ITEM_IDENTIFIER);

        restVirtualProductLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductLine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductLine))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductLine testVirtualProductLine = virtualProductLineList.get(virtualProductLineList.size() - 1);
        assertThat(testVirtualProductLine.getAncestorItemIdentifier()).isEqualTo(DEFAULT_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getLineAvailable()).isEqualTo(DEFAULT_LINE_AVAILABLE);
        assertThat(testVirtualProductLine.getLineCode()).isEqualTo(DEFAULT_LINE_CODE);
    }

    @Test
    @Transactional
    void fullUpdateVirtualProductLineWithPatch() throws Exception {
        // Initialize the database
        virtualProductLineRepository.saveAndFlush(virtualProductLine);

        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();

        // Update the virtualProductLine using partial update
        VirtualProductLine partialUpdatedVirtualProductLine = new VirtualProductLine();
        partialUpdatedVirtualProductLine.setId(virtualProductLine.getId());

        partialUpdatedVirtualProductLine
            .ancestorItemIdentifier(UPDATED_ANCESTOR_ITEM_IDENTIFIER)
            .itemIdentifier(UPDATED_ITEM_IDENTIFIER)
            .lineAvailable(UPDATED_LINE_AVAILABLE)
            .lineCode(UPDATED_LINE_CODE);

        restVirtualProductLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirtualProductLine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirtualProductLine))
            )
            .andExpect(status().isOk());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
        VirtualProductLine testVirtualProductLine = virtualProductLineList.get(virtualProductLineList.size() - 1);
        assertThat(testVirtualProductLine.getAncestorItemIdentifier()).isEqualTo(UPDATED_ANCESTOR_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getItemIdentifier()).isEqualTo(UPDATED_ITEM_IDENTIFIER);
        assertThat(testVirtualProductLine.getLineAvailable()).isEqualTo(UPDATED_LINE_AVAILABLE);
        assertThat(testVirtualProductLine.getLineCode()).isEqualTo(UPDATED_LINE_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingVirtualProductLine() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();
        virtualProductLine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirtualProductLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virtualProductLine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirtualProductLine() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();
        virtualProductLine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirtualProductLine() throws Exception {
        int databaseSizeBeforeUpdate = virtualProductLineRepository.findAll().size();
        virtualProductLine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirtualProductLineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virtualProductLine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VirtualProductLine in the database
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirtualProductLine() throws Exception {
        // Initialize the database
        virtualProductLineRepository.saveAndFlush(virtualProductLine);

        int databaseSizeBeforeDelete = virtualProductLineRepository.findAll().size();

        // Delete the virtualProductLine
        restVirtualProductLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, virtualProductLine.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VirtualProductLine> virtualProductLineList = virtualProductLineRepository.findAll();
        assertThat(virtualProductLineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
