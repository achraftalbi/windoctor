package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Prescription_medication;
import com.winbit.windoctor.repository.Prescription_medicationRepository;
import com.winbit.windoctor.repository.search.Prescription_medicationSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the Prescription_medicationResource REST controller.
 *
 * @see Prescription_medicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Prescription_medicationResourceTest {

    private static final String DEFAULT_MEDICATION_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_MEDICATION_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_MAN_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_MAN_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_WOMAN_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_WOMAN_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_CHILD_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_CHILD_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private Prescription_medicationRepository prescription_medicationRepository;

    @Inject
    private Prescription_medicationSearchRepository prescription_medicationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPrescription_medicationMockMvc;

    private Prescription_medication prescription_medication;

    @PostConstruct
    public void setup() {
        /*
        MockitoAnnotations.initMocks(this);
        Prescription_medicationResource prescription_medicationResource = new Prescription_medicationResource();
        ReflectionTestUtils.setField(prescription_medicationResource, "prescription_medicationRepository", prescription_medicationRepository);
        ReflectionTestUtils.setField(prescription_medicationResource, "prescription_medicationSearchRepository", prescription_medicationSearchRepository);
        this.restPrescription_medicationMockMvc = MockMvcBuilders.standaloneSetup(prescription_medicationResource).setMessageConverters(jacksonMessageConverter).build();
        */
    }

    @Before
    public void initTest() {
        /*
        prescription_medication = new Prescription_medication();
        prescription_medication.setMedication_name(DEFAULT_MEDICATION_NAME);
        prescription_medication.setMan_description(DEFAULT_MAN_DESCRIPTION);
        prescription_medication.setWoman_description(DEFAULT_WOMAN_DESCRIPTION);
        prescription_medication.setChild_description(DEFAULT_CHILD_DESCRIPTION);
        */
    }

    @Test
    @Transactional
    public void createPrescription_medication() throws Exception {
        /*
        int databaseSizeBeforeCreate = prescription_medicationRepository.findAll().size();

        // Create the Prescription_medication

        restPrescription_medicationMockMvc.perform(post("/api/prescription_medications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription_medication)))
                .andExpect(status().isCreated());

        // Validate the Prescription_medication in the database
        List<Prescription_medication> prescription_medications = prescription_medicationRepository.findAll();
        assertThat(prescription_medications).hasSize(databaseSizeBeforeCreate + 1);
        Prescription_medication testPrescription_medication = prescription_medications.get(prescription_medications.size() - 1);
        assertThat(testPrescription_medication.getMedication_name()).isEqualTo(DEFAULT_MEDICATION_NAME);
        assertThat(testPrescription_medication.getMan_description()).isEqualTo(DEFAULT_MAN_DESCRIPTION);
        assertThat(testPrescription_medication.getWoman_description()).isEqualTo(DEFAULT_WOMAN_DESCRIPTION);
        assertThat(testPrescription_medication.getChild_description()).isEqualTo(DEFAULT_CHILD_DESCRIPTION);
        */
    }

    @Test
    @Transactional
    public void getAllPrescription_medications() throws Exception {
        /*
        // Initialize the database
        prescription_medicationRepository.saveAndFlush(prescription_medication);

        // Get all the prescription_medications
        restPrescription_medicationMockMvc.perform(get("/api/prescription_medications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prescription_medication.getId().intValue())))
                .andExpect(jsonPath("$.[*].medication_name").value(hasItem(DEFAULT_MEDICATION_NAME.toString())))
                .andExpect(jsonPath("$.[*].man_description").value(hasItem(DEFAULT_MAN_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].woman_description").value(hasItem(DEFAULT_WOMAN_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].child_description").value(hasItem(DEFAULT_CHILD_DESCRIPTION.toString())));
        */
    }

    @Test
    @Transactional
    public void getPrescription_medication() throws Exception {
        /*
        // Initialize the database
        prescription_medicationRepository.saveAndFlush(prescription_medication);

        // Get the prescription_medication
        restPrescription_medicationMockMvc.perform(get("/api/prescription_medications/{id}", prescription_medication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prescription_medication.getId().intValue()))
            .andExpect(jsonPath("$.medication_name").value(DEFAULT_MEDICATION_NAME.toString()))
            .andExpect(jsonPath("$.man_description").value(DEFAULT_MAN_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.woman_description").value(DEFAULT_WOMAN_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.child_description").value(DEFAULT_CHILD_DESCRIPTION.toString()));
        */
    }

    @Test
    @Transactional
    public void getNonExistingPrescription_medication() throws Exception {
        /*
        // Get the prescription_medication
        restPrescription_medicationMockMvc.perform(get("/api/prescription_medications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        */
    }

    @Test
    @Transactional
    public void updatePrescription_medication() throws Exception {
        /*
        // Initialize the database
        prescription_medicationRepository.saveAndFlush(prescription_medication);

		int databaseSizeBeforeUpdate = prescription_medicationRepository.findAll().size();

        // Update the prescription_medication
        prescription_medication.setMedication_name(UPDATED_MEDICATION_NAME);
        prescription_medication.setMan_description(UPDATED_MAN_DESCRIPTION);
        prescription_medication.setWoman_description(UPDATED_WOMAN_DESCRIPTION);
        prescription_medication.setChild_description(UPDATED_CHILD_DESCRIPTION);


        restPrescription_medicationMockMvc.perform(put("/api/prescription_medications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prescription_medication)))
                .andExpect(status().isOk());

        // Validate the Prescription_medication in the database
        List<Prescription_medication> prescription_medications = prescription_medicationRepository.findAll();
        assertThat(prescription_medications).hasSize(databaseSizeBeforeUpdate);
        Prescription_medication testPrescription_medication = prescription_medications.get(prescription_medications.size() - 1);
        assertThat(testPrescription_medication.getMedication_name()).isEqualTo(UPDATED_MEDICATION_NAME);
        assertThat(testPrescription_medication.getMan_description()).isEqualTo(UPDATED_MAN_DESCRIPTION);
        assertThat(testPrescription_medication.getWoman_description()).isEqualTo(UPDATED_WOMAN_DESCRIPTION);
        assertThat(testPrescription_medication.getChild_description()).isEqualTo(UPDATED_CHILD_DESCRIPTION);
        */
    }

    @Test
    @Transactional
    public void deletePrescription_medication() throws Exception {
        /*
        // Initialize the database
        prescription_medicationRepository.saveAndFlush(prescription_medication);

		int databaseSizeBeforeDelete = prescription_medicationRepository.findAll().size();

        // Get the prescription_medication
        restPrescription_medicationMockMvc.perform(delete("/api/prescription_medications/{id}", prescription_medication.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prescription_medication> prescription_medications = prescription_medicationRepository.findAll();
        assertThat(prescription_medications).hasSize(databaseSizeBeforeDelete - 1);
        */
    }
}
