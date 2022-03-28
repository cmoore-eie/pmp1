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
import pl.eie.pmp.domain.Contract;
import pl.eie.pmp.domain.ContractVersion;
import pl.eie.pmp.repository.ContractVersionRepository;

/**
 * Integration tests for the {@link ContractVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractVersionResourceIT {

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_HIDDEN_CONTRACT = false;
    private static final Boolean UPDATED_HIDDEN_CONTRACT = true;

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/contract-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractVersionRepository contractVersionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractVersionMockMvc;

    private ContractVersion contractVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractVersion createEntity(EntityManager em) {
        ContractVersion contractVersion = new ContractVersion()
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .hiddenContract(DEFAULT_HIDDEN_CONTRACT)
            .versionNumber(DEFAULT_VERSION_NUMBER);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createEntity(em);
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        contractVersion.setContract(contract);
        return contractVersion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractVersion createUpdatedEntity(EntityManager em) {
        ContractVersion contractVersion = new ContractVersion()
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .hiddenContract(UPDATED_HIDDEN_CONTRACT)
            .versionNumber(UPDATED_VERSION_NUMBER);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createUpdatedEntity(em);
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        contractVersion.setContract(contract);
        return contractVersion;
    }

    @BeforeEach
    public void initTest() {
        contractVersion = createEntity(em);
    }

    @Test
    @Transactional
    void createContractVersion() throws Exception {
        int databaseSizeBeforeCreate = contractVersionRepository.findAll().size();
        // Create the ContractVersion
        restContractVersionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isCreated());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeCreate + 1);
        ContractVersion testContractVersion = contractVersionList.get(contractVersionList.size() - 1);
        assertThat(testContractVersion.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testContractVersion.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testContractVersion.getHiddenContract()).isEqualTo(DEFAULT_HIDDEN_CONTRACT);
        assertThat(testContractVersion.getVersionNumber()).isEqualTo(DEFAULT_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void createContractVersionWithExistingId() throws Exception {
        // Create the ContractVersion with an existing ID
        contractVersion.setId(1L);

        int databaseSizeBeforeCreate = contractVersionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractVersionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContractVersions() throws Exception {
        // Initialize the database
        contractVersionRepository.saveAndFlush(contractVersion);

        // Get all the contractVersionList
        restContractVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].hiddenContract").value(hasItem(DEFAULT_HIDDEN_CONTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)));
    }

    @Test
    @Transactional
    void getContractVersion() throws Exception {
        // Initialize the database
        contractVersionRepository.saveAndFlush(contractVersion);

        // Get the contractVersion
        restContractVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, contractVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractVersion.getId().intValue()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.hiddenContract").value(DEFAULT_HIDDEN_CONTRACT.booleanValue()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingContractVersion() throws Exception {
        // Get the contractVersion
        restContractVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContractVersion() throws Exception {
        // Initialize the database
        contractVersionRepository.saveAndFlush(contractVersion);

        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();

        // Update the contractVersion
        ContractVersion updatedContractVersion = contractVersionRepository.findById(contractVersion.getId()).get();
        // Disconnect from session so that the updates on updatedContractVersion are not directly saved in db
        em.detach(updatedContractVersion);
        updatedContractVersion
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .hiddenContract(UPDATED_HIDDEN_CONTRACT)
            .versionNumber(UPDATED_VERSION_NUMBER);

        restContractVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContractVersion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContractVersion))
            )
            .andExpect(status().isOk());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
        ContractVersion testContractVersion = contractVersionList.get(contractVersionList.size() - 1);
        assertThat(testContractVersion.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testContractVersion.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testContractVersion.getHiddenContract()).isEqualTo(UPDATED_HIDDEN_CONTRACT);
        assertThat(testContractVersion.getVersionNumber()).isEqualTo(UPDATED_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingContractVersion() throws Exception {
        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();
        contractVersion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractVersion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractVersion() throws Exception {
        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();
        contractVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractVersion() throws Exception {
        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();
        contractVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractVersionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractVersionWithPatch() throws Exception {
        // Initialize the database
        contractVersionRepository.saveAndFlush(contractVersion);

        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();

        // Update the contractVersion using partial update
        ContractVersion partialUpdatedContractVersion = new ContractVersion();
        partialUpdatedContractVersion.setId(contractVersion.getId());

        partialUpdatedContractVersion.expirationDate(UPDATED_EXPIRATION_DATE);

        restContractVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractVersion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractVersion))
            )
            .andExpect(status().isOk());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
        ContractVersion testContractVersion = contractVersionList.get(contractVersionList.size() - 1);
        assertThat(testContractVersion.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testContractVersion.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testContractVersion.getHiddenContract()).isEqualTo(DEFAULT_HIDDEN_CONTRACT);
        assertThat(testContractVersion.getVersionNumber()).isEqualTo(DEFAULT_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateContractVersionWithPatch() throws Exception {
        // Initialize the database
        contractVersionRepository.saveAndFlush(contractVersion);

        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();

        // Update the contractVersion using partial update
        ContractVersion partialUpdatedContractVersion = new ContractVersion();
        partialUpdatedContractVersion.setId(contractVersion.getId());

        partialUpdatedContractVersion
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .hiddenContract(UPDATED_HIDDEN_CONTRACT)
            .versionNumber(UPDATED_VERSION_NUMBER);

        restContractVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractVersion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractVersion))
            )
            .andExpect(status().isOk());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
        ContractVersion testContractVersion = contractVersionList.get(contractVersionList.size() - 1);
        assertThat(testContractVersion.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testContractVersion.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testContractVersion.getHiddenContract()).isEqualTo(UPDATED_HIDDEN_CONTRACT);
        assertThat(testContractVersion.getVersionNumber()).isEqualTo(UPDATED_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingContractVersion() throws Exception {
        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();
        contractVersion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractVersion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractVersion() throws Exception {
        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();
        contractVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractVersion() throws Exception {
        int databaseSizeBeforeUpdate = contractVersionRepository.findAll().size();
        contractVersion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractVersionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractVersion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractVersion in the database
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractVersion() throws Exception {
        // Initialize the database
        contractVersionRepository.saveAndFlush(contractVersion);

        int databaseSizeBeforeDelete = contractVersionRepository.findAll().size();

        // Delete the contractVersion
        restContractVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractVersion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContractVersion> contractVersionList = contractVersionRepository.findAll();
        assertThat(contractVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
