package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Consumption;
import com.winbit.windoctor.repository.ConsumptionRepository;
import com.winbit.windoctor.repository.search.ConsumptionSearchRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ConsumptionResource REST controller.
 *
 * @see ConsumptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ConsumptionResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final DateTime DEFAULT_CREATION_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATION_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATION_DATE);

    private static final DateTime DEFAULT_RELATIVE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_RELATIVE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_RELATIVE_DATE_STR = dateTimeFormatter.print(DEFAULT_RELATIVE_DATE);

    @Inject
    private ConsumptionRepository consumptionRepository;

    @Inject
    private ConsumptionSearchRepository consumptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restConsumptionMockMvc;

    private Consumption consumption;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        ConsumptionResource consumptionResource = new ConsumptionResource();
        ReflectionTestUtils.setField(consumptionResource, "consumptionRepository", consumptionRepository);
        ReflectionTestUtils.setField(consumptionResource, "consumptionSearchRepository", consumptionSearchRepository);
        this.restConsumptionMockMvc = MockMvcBuilders.standaloneSetup(consumptionResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        consumption = new Consumption();
        consumption.setAmount(DEFAULT_AMOUNT);
        consumption.setCreation_date(DEFAULT_CREATION_DATE);
        consumption.setRelative_date(DEFAULT_RELATIVE_DATE);
    }

    @Test
    @Transactional
    public void createConsumption() throws Exception {
        /*int databaseSizeBeforeCreate = consumptionRepository.findAll().size();

        // Create the Consumption

        restConsumptionMockMvc.perform(post("/api/consumptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consumption)))
                .andExpect(status().isCreated());

        // Validate the Consumption in the database
        List<Consumption> consumptions = consumptionRepository.findAll();
        assertThat(consumptions).hasSize(databaseSizeBeforeCreate + 1);
        Consumption testConsumption = consumptions.get(consumptions.size() - 1);
        assertThat(testConsumption.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testConsumption.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testConsumption.getRelative_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = consumptionRepository.findAll().size();
        // set the field null
        consumption.setAmount(null);

        // Create the Consumption, which fails.

        restConsumptionMockMvc.perform(post("/api/consumptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consumption)))
                .andExpect(status().isBadRequest());

        List<Consumption> consumptions = consumptionRepository.findAll();
        assertThat(consumptions).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkCreation_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = consumptionRepository.findAll().size();
        // set the field null
        consumption.setCreation_date(null);

        // Create the Consumption, which fails.

        restConsumptionMockMvc.perform(post("/api/consumptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consumption)))
                .andExpect(status().isBadRequest());

        List<Consumption> consumptions = consumptionRepository.findAll();
        assertThat(consumptions).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkRelative_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = consumptionRepository.findAll().size();
        // set the field null
        consumption.setRelative_date(null);

        // Create the Consumption, which fails.

        restConsumptionMockMvc.perform(post("/api/consumptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consumption)))
                .andExpect(status().isBadRequest());

        List<Consumption> consumptions = consumptionRepository.findAll();
        assertThat(consumptions).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllConsumptions() throws Exception {
        // Initialize the database
        /*consumptionRepository.saveAndFlush(consumption);

        // Get all the consumptions
        restConsumptionMockMvc.perform(get("/api/consumptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(consumption.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].relative_date").value(hasItem(DEFAULT_RELATIVE_DATE_STR)));*/
    }

    @Test
    @Transactional
    public void getConsumption() throws Exception {
        // Initialize the database
        /*consumptionRepository.saveAndFlush(consumption);

        // Get the consumption
        restConsumptionMockMvc.perform(get("/api/consumptions/{id}", consumption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(consumption.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.relative_date").value(DEFAULT_RELATIVE_DATE_STR));*/
    }

    @Test
    @Transactional
    public void getNonExistingConsumption() throws Exception {
        // Get the consumption
        /*restConsumptionMockMvc.perform(get("/api/consumptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updateConsumption() throws Exception {
        // Initialize the database
        /*consumptionRepository.saveAndFlush(consumption);

		int databaseSizeBeforeUpdate = consumptionRepository.findAll().size();

        // Update the consumption
        consumption.setAmount(UPDATED_AMOUNT);
        consumption.setCreation_date(UPDATED_CREATION_DATE);
        consumption.setRelative_date(UPDATED_RELATIVE_DATE);


        restConsumptionMockMvc.perform(put("/api/consumptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consumption)))
                .andExpect(status().isOk());

        // Validate the Consumption in the database
        List<Consumption> consumptions = consumptionRepository.findAll();
        assertThat(consumptions).hasSize(databaseSizeBeforeUpdate);
        Consumption testConsumption = consumptions.get(consumptions.size() - 1);
        assertThat(testConsumption.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testConsumption.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testConsumption.getRelative_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void deleteConsumption() throws Exception {
        // Initialize the database
        /*consumptionRepository.saveAndFlush(consumption);

		int databaseSizeBeforeDelete = consumptionRepository.findAll().size();

        // Get the consumption
        restConsumptionMockMvc.perform(delete("/api/consumptions/{id}", consumption.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Consumption> consumptions = consumptionRepository.findAll();
        assertThat(consumptions).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
