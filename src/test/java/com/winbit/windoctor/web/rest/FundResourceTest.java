package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Fund;
import com.winbit.windoctor.repository.FundRepository;
import com.winbit.windoctor.repository.search.FundSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FundResource REST controller.
 *
 * @see FundResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FundResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    @Inject
    private FundRepository fundRepository;

    @Inject
    private FundSearchRepository fundSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restFundMockMvc;

    private Fund fund;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FundResource fundResource = new FundResource();
        ReflectionTestUtils.setField(fundResource, "fundRepository", fundRepository);
        ReflectionTestUtils.setField(fundResource, "fundSearchRepository", fundSearchRepository);
        this.restFundMockMvc = MockMvcBuilders.standaloneSetup(fundResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fund = new Fund();
        fund.setDescription(DEFAULT_DESCRIPTION);
        fund.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createFund() throws Exception {
        int databaseSizeBeforeCreate = fundRepository.findAll().size();

        // Create the Fund

        restFundMockMvc.perform(post("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund)))
                .andExpect(status().isCreated());

        // Validate the Fund in the database
        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeCreate + 1);
        Fund testFund = funds.get(funds.size() - 1);
        assertThat(testFund.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFund.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fundRepository.findAll().size();
        // set the field null
        fund.setDescription(null);

        // Create the Fund, which fails.

        restFundMockMvc.perform(post("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund)))
                .andExpect(status().isBadRequest());

        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = fundRepository.findAll().size();
        // set the field null
        fund.setAmount(null);

        // Create the Fund, which fails.

        restFundMockMvc.perform(post("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund)))
                .andExpect(status().isBadRequest());

        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFunds() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        // Get all the funds
        restFundMockMvc.perform(get("/api/funds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fund.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        // Get the fund
        restFundMockMvc.perform(get("/api/funds/{id}", fund.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fund.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFund() throws Exception {
        // Get the fund
        restFundMockMvc.perform(get("/api/funds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

		int databaseSizeBeforeUpdate = fundRepository.findAll().size();

        // Update the fund
        fund.setDescription(UPDATED_DESCRIPTION);
        fund.setAmount(UPDATED_AMOUNT);
        

        restFundMockMvc.perform(put("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund)))
                .andExpect(status().isOk());

        // Validate the Fund in the database
        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeUpdate);
        Fund testFund = funds.get(funds.size() - 1);
        assertThat(testFund.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFund.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

		int databaseSizeBeforeDelete = fundRepository.findAll().size();

        // Get the fund
        restFundMockMvc.perform(delete("/api/funds/{id}", fund.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fund> funds = fundRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
