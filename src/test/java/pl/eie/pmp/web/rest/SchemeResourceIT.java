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
import pl.eie.pmp.domain.Scheme;
import pl.eie.pmp.repository.SchemeRepository;

/**
 * Integration tests for the {@link SchemeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchemeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/schemes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchemeMockMvc;

    private Scheme scheme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scheme createEntity(EntityManager em) {
        Scheme scheme = new Scheme().code(DEFAULT_CODE).name(DEFAULT_NAME);
        return scheme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scheme createUpdatedEntity(EntityManager em) {
        Scheme scheme = new Scheme().code(UPDATED_CODE).name(UPDATED_NAME);
        return scheme;
    }

    @BeforeEach
    public void initTest() {
        scheme = createEntity(em);
    }

    @Test
    @Transactional
    void createScheme() throws Exception {
        int databaseSizeBeforeCreate = schemeRepository.findAll().size();
        // Create the Scheme
        restSchemeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isCreated());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeCreate + 1);
        Scheme testScheme = schemeList.get(schemeList.size() - 1);
        assertThat(testScheme.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testScheme.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSchemeWithExistingId() throws Exception {
        // Create the Scheme with an existing ID
        scheme.setId(1L);

        int databaseSizeBeforeCreate = schemeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchemeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSchemes() throws Exception {
        // Initialize the database
        schemeRepository.saveAndFlush(scheme);

        // Get all the schemeList
        restSchemeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheme.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getScheme() throws Exception {
        // Initialize the database
        schemeRepository.saveAndFlush(scheme);

        // Get the scheme
        restSchemeMockMvc
            .perform(get(ENTITY_API_URL_ID, scheme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheme.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingScheme() throws Exception {
        // Get the scheme
        restSchemeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheme() throws Exception {
        // Initialize the database
        schemeRepository.saveAndFlush(scheme);

        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();

        // Update the scheme
        Scheme updatedScheme = schemeRepository.findById(scheme.getId()).get();
        // Disconnect from session so that the updates on updatedScheme are not directly saved in db
        em.detach(updatedScheme);
        updatedScheme.code(UPDATED_CODE).name(UPDATED_NAME);

        restSchemeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheme.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheme))
            )
            .andExpect(status().isOk());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
        Scheme testScheme = schemeList.get(schemeList.size() - 1);
        assertThat(testScheme.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testScheme.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingScheme() throws Exception {
        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();
        scheme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchemeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheme.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheme() throws Exception {
        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();
        scheme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchemeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheme() throws Exception {
        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();
        scheme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchemeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchemeWithPatch() throws Exception {
        // Initialize the database
        schemeRepository.saveAndFlush(scheme);

        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();

        // Update the scheme using partial update
        Scheme partialUpdatedScheme = new Scheme();
        partialUpdatedScheme.setId(scheme.getId());

        partialUpdatedScheme.code(UPDATED_CODE).name(UPDATED_NAME);

        restSchemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheme))
            )
            .andExpect(status().isOk());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
        Scheme testScheme = schemeList.get(schemeList.size() - 1);
        assertThat(testScheme.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testScheme.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSchemeWithPatch() throws Exception {
        // Initialize the database
        schemeRepository.saveAndFlush(scheme);

        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();

        // Update the scheme using partial update
        Scheme partialUpdatedScheme = new Scheme();
        partialUpdatedScheme.setId(scheme.getId());

        partialUpdatedScheme.code(UPDATED_CODE).name(UPDATED_NAME);

        restSchemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheme))
            )
            .andExpect(status().isOk());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
        Scheme testScheme = schemeList.get(schemeList.size() - 1);
        assertThat(testScheme.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testScheme.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingScheme() throws Exception {
        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();
        scheme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheme() throws Exception {
        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();
        scheme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheme() throws Exception {
        int databaseSizeBeforeUpdate = schemeRepository.findAll().size();
        scheme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchemeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheme))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scheme in the database
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheme() throws Exception {
        // Initialize the database
        schemeRepository.saveAndFlush(scheme);

        int databaseSizeBeforeDelete = schemeRepository.findAll().size();

        // Delete the scheme
        restSchemeMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheme.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scheme> schemeList = schemeRepository.findAll();
        assertThat(schemeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
