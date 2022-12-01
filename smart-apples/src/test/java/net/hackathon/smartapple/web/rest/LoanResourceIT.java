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
import net.hackathon.smartapple.domain.Loan;
import net.hackathon.smartapple.domain.enumeration.CURRENCY;
import net.hackathon.smartapple.domain.enumeration.LoanStatus;
import net.hackathon.smartapple.repository.LoanRepository;
import net.hackathon.smartapple.service.dto.LoanDTO;
import net.hackathon.smartapple.service.mapper.LoanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LoanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoanResourceIT {

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOAN_AMT = 10000;
    private static final Integer UPDATED_LOAN_AMT = 10001;

    private static final Integer DEFAULT_AVAILABLE_AMT = 0;
    private static final Integer UPDATED_AVAILABLE_AMT = 1;

    private static final Integer DEFAULT_INTEREST_RATE = 0;
    private static final Integer UPDATED_INTEREST_RATE = 1;

    private static final Integer DEFAULT_TERMS = 2;
    private static final Integer UPDATED_TERMS = 3;

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CURRENCY DEFAULT_CURRENCY = CURRENCY.USD;
    private static final CURRENCY UPDATED_CURRENCY = CURRENCY.USD;

    private static final LoanStatus DEFAULT_STATUS = LoanStatus.PENDING;
    private static final LoanStatus UPDATED_STATUS = LoanStatus.ACTIVE;

    private static final String ENTITY_API_URL = "/api/loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanMockMvc;

    private Loan loan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createEntity(EntityManager em) {
        Loan loan = new Loan()
            .hash(DEFAULT_HASH)
            .loanAmt(DEFAULT_LOAN_AMT)
            .availableAmt(DEFAULT_AVAILABLE_AMT)
            .interestRate(DEFAULT_INTEREST_RATE)
            .terms(DEFAULT_TERMS)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .currency(DEFAULT_CURRENCY)
            .status(DEFAULT_STATUS);
        return loan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createUpdatedEntity(EntityManager em) {
        Loan loan = new Loan()
            .hash(UPDATED_HASH)
            .loanAmt(UPDATED_LOAN_AMT)
            .availableAmt(UPDATED_AVAILABLE_AMT)
            .interestRate(UPDATED_INTEREST_RATE)
            .terms(UPDATED_TERMS)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS);
        return loan;
    }

    @BeforeEach
    public void initTest() {
        loan = createEntity(em);
    }

    @Test
    @Transactional
    void createLoan() throws Exception {
        int databaseSizeBeforeCreate = loanRepository.findAll().size();
        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);
        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isCreated());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeCreate + 1);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testLoan.getLoanAmt()).isEqualTo(DEFAULT_LOAN_AMT);
        assertThat(testLoan.getAvailableAmt()).isEqualTo(DEFAULT_AVAILABLE_AMT);
        assertThat(testLoan.getInterestRate()).isEqualTo(DEFAULT_INTEREST_RATE);
        assertThat(testLoan.getTerms()).isEqualTo(DEFAULT_TERMS);
        assertThat(testLoan.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testLoan.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
        assertThat(testLoan.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testLoan.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createLoanWithExistingId() throws Exception {
        // Create the Loan with an existing ID
        loan.setId(1L);
        LoanDTO loanDTO = loanMapper.toDto(loan);

        int databaseSizeBeforeCreate = loanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setHash(null);

        // Create the Loan, which fails.
        LoanDTO loanDTO = loanMapper.toDto(loan);

        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanAmtIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setLoanAmt(null);

        // Create the Loan, which fails.
        LoanDTO loanDTO = loanMapper.toDto(loan);

        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableAmtIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setAvailableAmt(null);

        // Create the Loan, which fails.
        LoanDTO loanDTO = loanMapper.toDto(loan);

        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInterestRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setInterestRate(null);

        // Create the Loan, which fails.
        LoanDTO loanDTO = loanMapper.toDto(loan);

        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTermsIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setTerms(null);

        // Create the Loan, which fails.
        LoanDTO loanDTO = loanMapper.toDto(loan);

        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoans() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        // Get all the loanList
        restLoanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loan.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].loanAmt").value(hasItem(DEFAULT_LOAN_AMT)))
            .andExpect(jsonPath("$.[*].availableAmt").value(hasItem(DEFAULT_AVAILABLE_AMT)))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE)))
            .andExpect(jsonPath("$.[*].terms").value(hasItem(DEFAULT_TERMS)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        // Get the loan
        restLoanMockMvc
            .perform(get(ENTITY_API_URL_ID, loan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loan.getId().intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.loanAmt").value(DEFAULT_LOAN_AMT))
            .andExpect(jsonPath("$.availableAmt").value(DEFAULT_AVAILABLE_AMT))
            .andExpect(jsonPath("$.interestRate").value(DEFAULT_INTEREST_RATE))
            .andExpect(jsonPath("$.terms").value(DEFAULT_TERMS))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLoan() throws Exception {
        // Get the loan
        restLoanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Update the loan
        Loan updatedLoan = loanRepository.findById(loan.getId()).get();
        // Disconnect from session so that the updates on updatedLoan are not directly saved in db
        em.detach(updatedLoan);
        updatedLoan
            .hash(UPDATED_HASH)
            .loanAmt(UPDATED_LOAN_AMT)
            .availableAmt(UPDATED_AVAILABLE_AMT)
            .interestRate(UPDATED_INTEREST_RATE)
            .terms(UPDATED_TERMS)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS);
        LoanDTO loanDTO = loanMapper.toDto(updatedLoan);

        restLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDTO))
            )
            .andExpect(status().isOk());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testLoan.getLoanAmt()).isEqualTo(UPDATED_LOAN_AMT);
        assertThat(testLoan.getAvailableAmt()).isEqualTo(UPDATED_AVAILABLE_AMT);
        assertThat(testLoan.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLoan.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testLoan.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testLoan.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testLoan.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testLoan.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();
        loan.setId(count.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();
        loan.setId(count.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();
        loan.setId(count.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoanWithPatch() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Update the loan using partial update
        Loan partialUpdatedLoan = new Loan();
        partialUpdatedLoan.setId(loan.getId());

        partialUpdatedLoan.availableAmt(UPDATED_AVAILABLE_AMT).terms(UPDATED_TERMS).updateAt(UPDATED_UPDATE_AT).status(UPDATED_STATUS);

        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoan))
            )
            .andExpect(status().isOk());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testLoan.getLoanAmt()).isEqualTo(DEFAULT_LOAN_AMT);
        assertThat(testLoan.getAvailableAmt()).isEqualTo(UPDATED_AVAILABLE_AMT);
        assertThat(testLoan.getInterestRate()).isEqualTo(DEFAULT_INTEREST_RATE);
        assertThat(testLoan.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testLoan.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testLoan.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testLoan.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testLoan.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateLoanWithPatch() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Update the loan using partial update
        Loan partialUpdatedLoan = new Loan();
        partialUpdatedLoan.setId(loan.getId());

        partialUpdatedLoan
            .hash(UPDATED_HASH)
            .loanAmt(UPDATED_LOAN_AMT)
            .availableAmt(UPDATED_AVAILABLE_AMT)
            .interestRate(UPDATED_INTEREST_RATE)
            .terms(UPDATED_TERMS)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS);

        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoan))
            )
            .andExpect(status().isOk());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testLoan.getLoanAmt()).isEqualTo(UPDATED_LOAN_AMT);
        assertThat(testLoan.getAvailableAmt()).isEqualTo(UPDATED_AVAILABLE_AMT);
        assertThat(testLoan.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLoan.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testLoan.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testLoan.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testLoan.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testLoan.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();
        loan.setId(count.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();
        loan.setId(count.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();
        loan.setId(count.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        int databaseSizeBeforeDelete = loanRepository.findAll().size();

        // Delete the loan
        restLoanMockMvc
            .perform(delete(ENTITY_API_URL_ID, loan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
