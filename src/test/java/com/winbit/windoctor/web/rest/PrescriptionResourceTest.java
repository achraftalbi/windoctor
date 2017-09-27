package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Prescription;
import com.winbit.windoctor.repository.PrescriptionRepository;
import com.winbit.windoctor.repository.search.PrescriptionSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PrescriptionResource REST controller.
 *
 * @see PrescriptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrescriptionResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ARCHIVED = false;
    private static final Boolean UPDATED_ARCHIVED = true;

    private static final Boolean DEFAULT_MEDICATION_PERSIST = false;
    private static final Boolean UPDATED_MEDICATION_PERSIST = true;

    private static final DateTime DEFAULT_CREATION_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATION_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATION_DATE);

    private static final DateTime DEFAULT_UPDATE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_UPDATE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_UPDATE_DATE_STR = dateTimeFormatter.print(DEFAULT_UPDATE_DATE);

    @Inject
    private PrescriptionRepository prescriptionRepository;

    @Inject
    private PrescriptionSearchRepository prescriptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPrescriptionMockMvc;

    private Prescription prescription;

    @PostConstruct
    public void setup() {
        /*
        MockitoAnnotations.initMocks(this);
        PrescriptionResource prescriptionResource = new PrescriptionResource();
        ReflectionTestUtils.setField(prescriptionResource, "prescriptionRepository", prescriptionRepository);
        ReflectionTestUtils.setField(prescriptionResource, "prescriptionSearchRepository", prescriptionSearchRepository);
        this.restPrescriptionMockMvc = MockMvcBuilders.standaloneSetup(prescriptionResource).setMessageConverters(jacksonMessageConverter).build();
        */
    }

    @Before
    public void initTest() {
        /*
        prescription = new Prescription();
        prescription.setDescription(DEFAULT_DESCRIPTION);
        prescription.setArchived(DEFAULT_ARCHIVED);
        prescription.setMedication_persist(DEFAULT_MEDICATION_PERSIST);
        prescription.setCreation_date(DEFAULT_CREATION_DATE);
        prescription.setUpdate_date(DEFAULT_UPDATE_DATE);
        */
    }

    @Test
    @Transactional
    public void createPrescription() throws Exception {
        /*
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // Create the Prescription

        restPrescriptionMockMvc.perform(post("/api/prescriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription)))
                .andExpect(status().isCreated());

        // Validate the Prescription in the database
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(databaseSizeBeforeCreate + 1);
        Prescription testPrescription = prescriptions.get(prescriptions.size() - 1);
        assertThat(testPrescription.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrescription.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
        assertThat(testPrescription.getMedication_persist()).isEqualTo(DEFAULT_MEDICATION_PERSIST);
        assertThat(testPrescription.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPrescription.getUpdate_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_UPDATE_DATE);
        */
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        /*
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setDescription(null);

        // Create the Prescription, which fails.

        restPrescriptionMockMvc.perform(post("/api/prescriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription)))
                .andExpect(status().isBadRequest());

        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(databaseSizeBeforeTest);
        */
    }

    @Test
    @Transactional
    public void checkCreation_dateIsRequired() throws Exception {
        /*
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setCreation_date(null);

        // Create the Prescription, which fails.

        restPrescriptionMockMvc.perform(post("/api/prescriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription)))
                .andExpect(status().isBadRequest());

        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(databaseSizeBeforeTest);
        */
    }

    @Test
    @Transactional
    public void checkUpdate_dateIsRequired() throws Exception {
        /*
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setUpdate_date(null);

        // Create the Prescription, which fails.

        restPrescriptionMockMvc.perform(post("/api/prescriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription)))
                .andExpect(status().isBadRequest());

        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(databaseSizeBeforeTest);
        */
    }

    @Test
    @Transactional
    public void getAllPrescriptions() throws Exception {
        /*
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptions
        restPrescriptionMockMvc.perform(get("/api/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.booleanValue())))
                .andExpect(jsonPath("$.[*].medication_persist").value(hasItem(DEFAULT_MEDICATION_PERSIST.booleanValue())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].update_date").value(hasItem(DEFAULT_UPDATE_DATE_STR)));
        */
    }

    @Test
    @Transactional
    public void getPrescription() throws Exception {
        /*
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get the prescription
        restPrescriptionMockMvc.perform(get("/api/prescriptions/{id}", prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prescription.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.booleanValue()))
            .andExpect(jsonPath("$.medication_persist").value(DEFAULT_MEDICATION_PERSIST.booleanValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.update_date").value(DEFAULT_UPDATE_DATE_STR));
        */
    }

    @Test
    @Transactional
    public void getNonExistingPrescription() throws Exception {
        /*
        // Get the prescription
        restPrescriptionMockMvc.perform(get("/api/prescriptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        */
    }

    @Test
    @Transactional
    public void updatePrescription() throws Exception {
        /*
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

		int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription
        prescription.setDescription(UPDATED_DESCRIPTION);
        prescription.setArchived(UPDATED_ARCHIVED);
        prescription.setMedication_persist(UPDATED_MEDICATION_PERSIST);
        prescription.setCreation_date(UPDATED_CREATION_DATE);
        prescription.setUpdate_date(UPDATED_UPDATE_DATE);


        restPrescriptionMockMvc.perform(put("/api/prescriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription)))
                .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptions.get(prescriptions.size() - 1);
        assertThat(testPrescription.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrescription.getArchived()).isEqualTo(UPDATED_ARCHIVED);
        assertThat(testPrescription.getMedication_persist()).isEqualTo(UPDATED_MEDICATION_PERSIST);
        assertThat(testPrescription.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPrescription.getUpdate_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_UPDATE_DATE);
        */
    }

    @Test
    @Transactional
    public void deletePrescription() throws Exception {
        /*
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

		int databaseSizeBeforeDelete = prescriptionRepository.findAll().size();

        // Get the prescription
        restPrescriptionMockMvc.perform(delete("/api/prescriptions/{id}", prescription.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(databaseSizeBeforeDelete - 1);
        */
    }
}
