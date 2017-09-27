package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Medication;
import com.winbit.windoctor.repository.MedicationRepository;
import com.winbit.windoctor.repository.search.MedicationSearchRepository;

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
 * Test class for the MedicationResource REST controller.
 *
 * @see MedicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MedicationResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_MAN_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_MAN_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_WOMAN_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_WOMAN_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_CHILD_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_CHILD_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private MedicationRepository medicationRepository;

    @Inject
    private MedicationSearchRepository medicationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restMedicationMockMvc;

    private Medication medication;

    @PostConstruct
    public void setup() {
        /*
        MockitoAnnotations.initMocks(this);
        MedicationResource medicationResource = new MedicationResource();
        ReflectionTestUtils.setField(medicationResource, "medicationRepository", medicationRepository);
        ReflectionTestUtils.setField(medicationResource, "medicationSearchRepository", medicationSearchRepository);
        this.restMedicationMockMvc = MockMvcBuilders.standaloneSetup(medicationResource).setMessageConverters(jacksonMessageConverter).build();
        */
    }

    @Before
    public void initTest() {
        /*
        medication = new Medication();
        medication.setName(DEFAULT_NAME);
        medication.setMan_description(DEFAULT_MAN_DESCRIPTION);
        medication.setWoman_description(DEFAULT_WOMAN_DESCRIPTION);
        medication.setChild_description(DEFAULT_CHILD_DESCRIPTION);
        */
    }

    @Test
    @Transactional
    public void createMedication() throws Exception {
        /*
        int databaseSizeBeforeCreate = medicationRepository.findAll().size();

        // Create the Medication

        restMedicationMockMvc.perform(post("/api/medications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medication)))
                .andExpect(status().isCreated());

        // Validate the Medication in the database
        List<Medication> medications = medicationRepository.findAll();
        assertThat(medications).hasSize(databaseSizeBeforeCreate + 1);
        Medication testMedication = medications.get(medications.size() - 1);
        assertThat(testMedication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedication.getMan_description()).isEqualTo(DEFAULT_MAN_DESCRIPTION);
        assertThat(testMedication.getWoman_description()).isEqualTo(DEFAULT_WOMAN_DESCRIPTION);
        assertThat(testMedication.getChild_description()).isEqualTo(DEFAULT_CHILD_DESCRIPTION);
        */
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        /*
        int databaseSizeBeforeTest = medicationRepository.findAll().size();
        // set the field null
        medication.setName(null);

        // Create the Medication, which fails.

        restMedicationMockMvc.perform(post("/api/medications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medication)))
                .andExpect(status().isBadRequest());

        List<Medication> medications = medicationRepository.findAll();
        assertThat(medications).hasSize(databaseSizeBeforeTest);
        */
    }

    @Test
    @Transactional
    public void getAllMedications() throws Exception {
        /*
        // Initialize the database
        medicationRepository.saveAndFlush(medication);

        // Get all the medications
        restMedicationMockMvc.perform(get("/api/medications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(medication.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].man_description").value(hasItem(DEFAULT_MAN_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].woman_description").value(hasItem(DEFAULT_WOMAN_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].child_description").value(hasItem(DEFAULT_CHILD_DESCRIPTION.toString())));
        */
    }

    @Test
    @Transactional
    public void getMedication() throws Exception {
        /*
        // Initialize the database
        medicationRepository.saveAndFlush(medication);

        // Get the medication
        restMedicationMockMvc.perform(get("/api/medications/{id}", medication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(medication.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.man_description").value(DEFAULT_MAN_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.woman_description").value(DEFAULT_WOMAN_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.child_description").value(DEFAULT_CHILD_DESCRIPTION.toString()));
        */
    }

    @Test
    @Transactional
    public void getNonExistingMedication() throws Exception {
        /*
        // Get the medication
        restMedicationMockMvc.perform(get("/api/medications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        */
    }

    @Test
    @Transactional
    public void updateMedication() throws Exception {
        /*
        // Initialize the database
        medicationRepository.saveAndFlush(medication);

		int databaseSizeBeforeUpdate = medicationRepository.findAll().size();

        // Update the medication
        medication.setName(UPDATED_NAME);
        medication.setMan_description(UPDATED_MAN_DESCRIPTION);
        medication.setWoman_description(UPDATED_WOMAN_DESCRIPTION);
        medication.setChild_description(UPDATED_CHILD_DESCRIPTION);


        restMedicationMockMvc.perform(put("/api/medications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medication)))
                .andExpect(status().isOk());

        // Validate the Medication in the database
        List<Medication> medications = medicationRepository.findAll();
        assertThat(medications).hasSize(databaseSizeBeforeUpdate);
        Medication testMedication = medications.get(medications.size() - 1);
        assertThat(testMedication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedication.getMan_description()).isEqualTo(UPDATED_MAN_DESCRIPTION);
        assertThat(testMedication.getWoman_description()).isEqualTo(UPDATED_WOMAN_DESCRIPTION);
        assertThat(testMedication.getChild_description()).isEqualTo(UPDATED_CHILD_DESCRIPTION);
        */
    }

    @Test
    @Transactional
    public void deleteMedication() throws Exception {
        /*
        // Initialize the database
        medicationRepository.saveAndFlush(medication);

		int databaseSizeBeforeDelete = medicationRepository.findAll().size();

        // Get the medication
        restMedicationMockMvc.perform(delete("/api/medications/{id}", medication.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Medication> medications = medicationRepository.findAll();
        assertThat(medications).hasSize(databaseSizeBeforeDelete - 1);
        */
    }
}
