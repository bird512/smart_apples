package net.hackathon.smartapple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import net.hackathon.smartapple.IntegrationTest;
import net.hackathon.smartapple.domain.LoanSubscription;
import net.hackathon.smartapple.repository.LoanSubscriptionRepository;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;
import net.hackathon.smartapple.service.mapper.LoanSubscriptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LoanSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoanSubscriptionResourceIT {

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUB_AMT = 0;
    private static final Integer UPDATED_SUB_AMT = 1;

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/loan-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanSubscriptionRepository loanSubscriptionRepository;

    @Autowired
    private LoanSubscriptionMapper loanSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanSubscriptionMockMvc;

    private LoanSubscription loanSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanSubscription createEntity(EntityManager em) {
        LoanSubscription loanSubscription = new LoanSubscription()
            .hash(DEFAULT_HASH)
            .subAmt(DEFAULT_SUB_AMT)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return loanSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanSubscription createUpdatedEntity(EntityManager em) {
        LoanSubscription loanSubscription = new LoanSubscription()
            .hash(UPDATED_HASH)
            .subAmt(UPDATED_SUB_AMT)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        return loanSubscription;
    }

    @BeforeEach
    public void initTest() {
        loanSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanSubscription() throws Exception {
        int databaseSizeBeforeCreate = loanSubscriptionRepository.findAll().size();
        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);
        restLoanSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        LoanSubscription testLoanSubscription = loanSubscriptionList.get(loanSubscriptionList.size() - 1);
        assertThat(testLoanSubscription.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testLoanSubscription.getSubAmt()).isEqualTo(DEFAULT_SUB_AMT);
        assertThat(testLoanSubscription.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testLoanSubscription.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void createLoanSubscriptionWithExistingId() throws Exception {
        // Create the LoanSubscription with an existing ID
        loanSubscription.setId(1L);
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        int databaseSizeBeforeCreate = loanSubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanSubscriptionRepository.findAll().size();
        // set the field null
        loanSubscription.setHash(null);

        // Create the LoanSubscription, which fails.
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        restLoanSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubAmtIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanSubscriptionRepository.findAll().size();
        // set the field null
        loanSubscription.setSubAmt(null);

        // Create the LoanSubscription, which fails.
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        restLoanSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanSubscriptions() throws Exception {
        // Initialize the database
        loanSubscriptionRepository.saveAndFlush(loanSubscription);

        // Get all the loanSubscriptionList
        restLoanSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].subAmt").value(hasItem(DEFAULT_SUB_AMT)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getLoanSubscription() throws Exception {
        // Initialize the database
        loanSubscriptionRepository.saveAndFlush(loanSubscription);

        // Get the loanSubscription
        restLoanSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, loanSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanSubscription.getId().intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.subAmt").value(DEFAULT_SUB_AMT))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLoanSubscription() throws Exception {
        // Get the loanSubscription
        restLoanSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoanSubscription() throws Exception {
        // Initialize the database
        loanSubscriptionRepository.saveAndFlush(loanSubscription);

        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();

        // Update the loanSubscription
        LoanSubscription updatedLoanSubscription = loanSubscriptionRepository.findById(loanSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedLoanSubscription are not directly saved in db
        em.detach(updatedLoanSubscription);
        updatedLoanSubscription.hash(UPDATED_HASH).subAmt(UPDATED_SUB_AMT).createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT);
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(updatedLoanSubscription);

        restLoanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        LoanSubscription testLoanSubscription = loanSubscriptionList.get(loanSubscriptionList.size() - 1);
        assertThat(testLoanSubscription.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testLoanSubscription.getSubAmt()).isEqualTo(UPDATED_SUB_AMT);
        assertThat(testLoanSubscription.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testLoanSubscription.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void putNonExistingLoanSubscription() throws Exception {
        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();
        loanSubscription.setId(count.incrementAndGet());

        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanSubscription() throws Exception {
        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();
        loanSubscription.setId(count.incrementAndGet());

        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanSubscription() throws Exception {
        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();
        loanSubscription.setId(count.incrementAndGet());

        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoanSubscriptionWithPatch() throws Exception {
        // Initialize the database
        loanSubscriptionRepository.saveAndFlush(loanSubscription);

        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();

        // Update the loanSubscription using partial update
        LoanSubscription partialUpdatedLoanSubscription = new LoanSubscription();
        partialUpdatedLoanSubscription.setId(loanSubscription.getId());

        partialUpdatedLoanSubscription.hash(UPDATED_HASH).subAmt(UPDATED_SUB_AMT).createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT);

        restLoanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanSubscription))
            )
            .andExpect(status().isOk());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        LoanSubscription testLoanSubscription = loanSubscriptionList.get(loanSubscriptionList.size() - 1);
        assertThat(testLoanSubscription.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testLoanSubscription.getSubAmt()).isEqualTo(UPDATED_SUB_AMT);
        assertThat(testLoanSubscription.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testLoanSubscription.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateLoanSubscriptionWithPatch() throws Exception {
        // Initialize the database
        loanSubscriptionRepository.saveAndFlush(loanSubscription);

        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();

        // Update the loanSubscription using partial update
        LoanSubscription partialUpdatedLoanSubscription = new LoanSubscription();
        partialUpdatedLoanSubscription.setId(loanSubscription.getId());

        partialUpdatedLoanSubscription.hash(UPDATED_HASH).subAmt(UPDATED_SUB_AMT).createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT);

        restLoanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanSubscription))
            )
            .andExpect(status().isOk());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        LoanSubscription testLoanSubscription = loanSubscriptionList.get(loanSubscriptionList.size() - 1);
        assertThat(testLoanSubscription.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testLoanSubscription.getSubAmt()).isEqualTo(UPDATED_SUB_AMT);
        assertThat(testLoanSubscription.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testLoanSubscription.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void patchNonExistingLoanSubscription() throws Exception {
        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();
        loanSubscription.setId(count.incrementAndGet());

        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanSubscription() throws Exception {
        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();
        loanSubscription.setId(count.incrementAndGet());

        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanSubscription() throws Exception {
        int databaseSizeBeforeUpdate = loanSubscriptionRepository.findAll().size();
        loanSubscription.setId(count.incrementAndGet());

        // Create the LoanSubscription
        LoanSubscriptionDTO loanSubscriptionDTO = loanSubscriptionMapper.toDto(loanSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanSubscription in the database
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoanSubscription() throws Exception {
        // Initialize the database
        loanSubscriptionRepository.saveAndFlush(loanSubscription);

        int databaseSizeBeforeDelete = loanSubscriptionRepository.findAll().size();

        // Delete the loanSubscription
        restLoanSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanSubscription> loanSubscriptionList = loanSubscriptionRepository.findAll();
        assertThat(loanSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
