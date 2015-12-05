package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Fund_history;
import com.winbit.windoctor.repository.Fund_historyRepository;
import com.winbit.windoctor.repository.search.Fund_historySearchRepository;

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
 * Test class for the Fund_historyResource REST controller.
 *
 * @see Fund_historyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Fund_historyResourceTest {


    private static final BigDecimal DEFAULT_OLD_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_OLD_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_NEW_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_NEW_AMOUNT = new BigDecimal(1);

    private static final Boolean DEFAULT_TYPE_OPERATION = false;
    private static final Boolean UPDATED_TYPE_OPERATION = true;

    private static final BigDecimal DEFAULT_AMOUNT_MOVEMENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_MOVEMENT = new BigDecimal(1);

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private Fund_historySearchRepository fund_historySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restFund_historyMockMvc;

    private Fund_history fund_history;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Fund_historyResource fund_historyResource = new Fund_historyResource();
        ReflectionTestUtils.setField(fund_historyResource, "fund_historyRepository", fund_historyRepository);
        ReflectionTestUtils.setField(fund_historyResource, "fund_historySearchRepository", fund_historySearchRepository);
        this.restFund_historyMockMvc = MockMvcBuilders.standaloneSetup(fund_historyResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fund_history = new Fund_history();
        fund_history.setOld_amount(DEFAULT_OLD_AMOUNT);
        fund_history.setNew_amount(DEFAULT_NEW_AMOUNT);
        fund_history.setType_operation(DEFAULT_TYPE_OPERATION);
        fund_history.setAmount_movement(DEFAULT_AMOUNT_MOVEMENT);
    }

    @Test
    @Transactional
    public void createFund_history() throws Exception {
        int databaseSizeBeforeCreate = fund_historyRepository.findAll().size();

        // Create the Fund_history

        restFund_historyMockMvc.perform(post("/api/fund_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund_history)))
                .andExpect(status().isCreated());

        // Validate the Fund_history in the database
        List<Fund_history> fund_historys = fund_historyRepository.findAll();
        assertThat(fund_historys).hasSize(databaseSizeBeforeCreate + 1);
        Fund_history testFund_history = fund_historys.get(fund_historys.size() - 1);
        assertThat(testFund_history.getOld_amount()).isEqualTo(DEFAULT_OLD_AMOUNT);
        assertThat(testFund_history.getNew_amount()).isEqualTo(DEFAULT_NEW_AMOUNT);
        assertThat(testFund_history.getType_operation()).isEqualTo(DEFAULT_TYPE_OPERATION);
        assertThat(testFund_history.getAmount_movement()).isEqualTo(DEFAULT_AMOUNT_MOVEMENT);
    }

    @Test
    @Transactional
    public void checkOld_amountIsRequired() throws Exception {
        int databaseSizeBeforeTest = fund_historyRepository.findAll().size();
        // set the field null
        fund_history.setOld_amount(null);

        // Create the Fund_history, which fails.

        restFund_historyMockMvc.perform(post("/api/fund_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund_history)))
                .andExpect(status().isBadRequest());

        List<Fund_history> fund_historys = fund_historyRepository.findAll();
        assertThat(fund_historys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNew_amountIsRequired() throws Exception {
        int databaseSizeBeforeTest = fund_historyRepository.findAll().size();
        // set the field null
        fund_history.setNew_amount(null);

        // Create the Fund_history, which fails.

        restFund_historyMockMvc.perform(post("/api/fund_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund_history)))
                .andExpect(status().isBadRequest());

        List<Fund_history> fund_historys = fund_historyRepository.findAll();
        assertThat(fund_historys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmount_movementIsRequired() throws Exception {
        int databaseSizeBeforeTest = fund_historyRepository.findAll().size();
        // set the field null
        fund_history.setAmount_movement(null);

        // Create the Fund_history, which fails.

        restFund_historyMockMvc.perform(post("/api/fund_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund_history)))
                .andExpect(status().isBadRequest());

        List<Fund_history> fund_historys = fund_historyRepository.findAll();
        assertThat(fund_historys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFund_historys() throws Exception {
        // Initialize the database
        fund_historyRepository.saveAndFlush(fund_history);

        // Get all the fund_historys
        restFund_historyMockMvc.perform(get("/api/fund_historys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fund_history.getId().intValue())))
                .andExpect(jsonPath("$.[*].old_amount").value(hasItem(DEFAULT_OLD_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].new_amount").value(hasItem(DEFAULT_NEW_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].type_operation").value(hasItem(DEFAULT_TYPE_OPERATION.booleanValue())))
                .andExpect(jsonPath("$.[*].amount_movement").value(hasItem(DEFAULT_AMOUNT_MOVEMENT.intValue())));
    }

    @Test
    @Transactional
    public void getFund_history() throws Exception {
        // Initialize the database
        fund_historyRepository.saveAndFlush(fund_history);

        // Get the fund_history
        restFund_historyMockMvc.perform(get("/api/fund_historys/{id}", fund_history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fund_history.getId().intValue()))
            .andExpect(jsonPath("$.old_amount").value(DEFAULT_OLD_AMOUNT.intValue()))
            .andExpect(jsonPath("$.new_amount").value(DEFAULT_NEW_AMOUNT.intValue()))
            .andExpect(jsonPath("$.type_operation").value(DEFAULT_TYPE_OPERATION.booleanValue()))
            .andExpect(jsonPath("$.amount_movement").value(DEFAULT_AMOUNT_MOVEMENT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFund_history() throws Exception {
        // Get the fund_history
        restFund_historyMockMvc.perform(get("/api/fund_historys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFund_history() throws Exception {
        // Initialize the database
        fund_historyRepository.saveAndFlush(fund_history);

		int databaseSizeBeforeUpdate = fund_historyRepository.findAll().size();

        // Update the fund_history
        fund_history.setOld_amount(UPDATED_OLD_AMOUNT);
        fund_history.setNew_amount(UPDATED_NEW_AMOUNT);
        fund_history.setType_operation(UPDATED_TYPE_OPERATION);
        fund_history.setAmount_movement(UPDATED_AMOUNT_MOVEMENT);
        

        restFund_historyMockMvc.perform(put("/api/fund_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fund_history)))
                .andExpect(status().isOk());

        // Validate the Fund_history in the database
        List<Fund_history> fund_historys = fund_historyRepository.findAll();
        assertThat(fund_historys).hasSize(databaseSizeBeforeUpdate);
        Fund_history testFund_history = fund_historys.get(fund_historys.size() - 1);
        assertThat(testFund_history.getOld_amount()).isEqualTo(UPDATED_OLD_AMOUNT);
        assertThat(testFund_history.getNew_amount()).isEqualTo(UPDATED_NEW_AMOUNT);
        assertThat(testFund_history.getType_operation()).isEqualTo(UPDATED_TYPE_OPERATION);
        assertThat(testFund_history.getAmount_movement()).isEqualTo(UPDATED_AMOUNT_MOVEMENT);
    }

    @Test
    @Transactional
    public void deleteFund_history() throws Exception {
        // Initialize the database
        fund_historyRepository.saveAndFlush(fund_history);

		int databaseSizeBeforeDelete = fund_historyRepository.findAll().size();

        // Get the fund_history
        restFund_historyMockMvc.perform(delete("/api/fund_historys/{id}", fund_history.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fund_history> fund_historys = fund_historyRepository.findAll();
        assertThat(fund_historys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
