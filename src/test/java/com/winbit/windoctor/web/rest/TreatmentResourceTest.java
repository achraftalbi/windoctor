package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Treatment;
import com.winbit.windoctor.repository.TreatmentRepository;
import com.winbit.windoctor.repository.search.TreatmentSearchRepository;

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
 * Test class for the TreatmentResource REST controller.
 *
 * @see TreatmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TreatmentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_TREATMENT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TREATMENT_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TREATMENT_DATE_STR = dateTimeFormatter.print(DEFAULT_TREATMENT_DATE);
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PAID_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PAID_PRICE = new BigDecimal(1);

    @Inject
    private TreatmentRepository treatmentRepository;

    @Inject
    private TreatmentSearchRepository treatmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTreatmentMockMvc;

    private Treatment treatment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TreatmentResource treatmentResource = new TreatmentResource();
        ReflectionTestUtils.setField(treatmentResource, "treatmentRepository", treatmentRepository);
        ReflectionTestUtils.setField(treatmentResource, "treatmentSearchRepository", treatmentSearchRepository);
        this.restTreatmentMockMvc = MockMvcBuilders.standaloneSetup(treatmentResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        treatment = new Treatment();
        treatment.setTreatment_date(DEFAULT_TREATMENT_DATE);
        treatment.setDescription(DEFAULT_DESCRIPTION);
        treatment.setPrice(DEFAULT_PRICE);
        treatment.setPaid_price(DEFAULT_PAID_PRICE);
    }

    @Test
    @Transactional
    public void createTreatment() throws Exception {
        /*int databaseSizeBeforeCreate = treatmentRepository.findAll().size();

        // Create the Treatment

        restTreatmentMockMvc.perform(post("/api/treatments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(treatment)))
                .andExpect(status().isCreated());

        // Validate the Treatment in the database
        List<Treatment> treatments = treatmentRepository.findAll();
        assertThat(treatments).hasSize(databaseSizeBeforeCreate + 1);
        Treatment testTreatment = treatments.get(treatments.size() - 1);
        assertThat(testTreatment.getTreatment_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TREATMENT_DATE);
        assertThat(testTreatment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTreatment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTreatment.getPaid_price()).isEqualTo(DEFAULT_PAID_PRICE);*/
    }
/*
    @Test
    @Transactional
    public void checkTreatment_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = treatmentRepository.findAll().size();
        // set the field null
        treatment.setTreatment_date(null);

        // Create the Treatment, which fails.

        restTreatmentMockMvc.perform(post("/api/treatments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(treatment)))
                .andExpect(status().isBadRequest());

        List<Treatment> treatments = treatmentRepository.findAll();
        assertThat(treatments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = treatmentRepository.findAll().size();
        // set the field null
        treatment.setPrice(null);

        // Create the Treatment, which fails.

        restTreatmentMockMvc.perform(post("/api/treatments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(treatment)))
                .andExpect(status().isBadRequest());

        List<Treatment> treatments = treatmentRepository.findAll();
        assertThat(treatments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaid_priceIsRequired() throws Exception {
        int databaseSizeBeforeTest = treatmentRepository.findAll().size();
        // set the field null
        treatment.setPaid_price(null);

        // Create the Treatment, which fails.

        restTreatmentMockMvc.perform(post("/api/treatments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(treatment)))
                .andExpect(status().isBadRequest());

        List<Treatment> treatments = treatmentRepository.findAll();
        assertThat(treatments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTreatments() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get all the treatments
        restTreatmentMockMvc.perform(get("/api/treatments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(treatment.getId().intValue())))
                .andExpect(jsonPath("$.[*].treatment_date").value(hasItem(DEFAULT_TREATMENT_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].paid_price").value(hasItem(DEFAULT_PAID_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getTreatment() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

        // Get the treatment
        restTreatmentMockMvc.perform(get("/api/treatments/{id}", treatment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(treatment.getId().intValue()))
            .andExpect(jsonPath("$.treatment_date").value(DEFAULT_TREATMENT_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.paid_price").value(DEFAULT_PAID_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTreatment() throws Exception {
        // Get the treatment
        restTreatmentMockMvc.perform(get("/api/treatments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTreatment() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

		int databaseSizeBeforeUpdate = treatmentRepository.findAll().size();

        // Update the treatment
        treatment.setTreatment_date(UPDATED_TREATMENT_DATE);
        treatment.setDescription(UPDATED_DESCRIPTION);
        treatment.setPrice(UPDATED_PRICE);
        treatment.setPaid_price(UPDATED_PAID_PRICE);


        restTreatmentMockMvc.perform(put("/api/treatments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(treatment)))
                .andExpect(status().isOk());

        // Validate the Treatment in the database
        List<Treatment> treatments = treatmentRepository.findAll();
        assertThat(treatments).hasSize(databaseSizeBeforeUpdate);
        Treatment testTreatment = treatments.get(treatments.size() - 1);
        assertThat(testTreatment.getTreatment_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TREATMENT_DATE);
        assertThat(testTreatment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTreatment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTreatment.getPaid_price()).isEqualTo(UPDATED_PAID_PRICE);
    }

    @Test
    @Transactional
    public void deleteTreatment() throws Exception {
        // Initialize the database
        treatmentRepository.saveAndFlush(treatment);

		int databaseSizeBeforeDelete = treatmentRepository.findAll().size();

        // Get the treatment
        restTreatmentMockMvc.perform(delete("/api/treatments/{id}", treatment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Treatment> treatments = treatmentRepository.findAll();
        assertThat(treatments).hasSize(databaseSizeBeforeDelete - 1);
    }
    */
}
