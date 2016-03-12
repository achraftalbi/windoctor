package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Doctor;
import com.winbit.windoctor.repository.DoctorRepository;
import com.winbit.windoctor.repository.search.DoctorSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DoctorResource REST controller.
 *
 * @see DoctorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DoctorResourceTest {

    private static final String DEFAULT_LOGIN = "SAMPLE_TEXT";
    private static final String UPDATED_LOGIN = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD = "UPDATED_TEXT";
    private static final String DEFAULT_FIRST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_FIRST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LAST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_LAST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Boolean DEFAULT_BLOCKED = false;
    private static final Boolean UPDATED_BLOCKED = true;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1000000, "1");

    @Inject
    private DoctorRepository doctorRepository;

    @Inject
    private DoctorSearchRepository doctorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        DoctorResource doctorResource = new DoctorResource();
        ReflectionTestUtils.setField(doctorResource, "doctorRepository", doctorRepository);
        ReflectionTestUtils.setField(doctorResource, "doctorSearchRepository", doctorSearchRepository);
        this.restDoctorMockMvc = MockMvcBuilders.standaloneSetup(doctorResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        /*doctor = new Doctor();
        doctor.setLogin(DEFAULT_LOGIN);
        doctor.setPassword(DEFAULT_PASSWORD);
        doctor.setFirstName(DEFAULT_FIRST_NAME);
        doctor.setLastName(DEFAULT_LAST_NAME);
        doctor.setEmail(DEFAULT_EMAIL);
        doctor.setActivated(DEFAULT_ACTIVATED);
        doctor.setBlocked(DEFAULT_BLOCKED);
        doctor.setPicture(DEFAULT_PICTURE);*/
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        /*int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor

        restDoctorMockMvc.perform(post("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctor)))
                .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctors.get(doctors.size() - 1);
        assertThat(testDoctor.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testDoctor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testDoctor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDoctor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDoctor.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testDoctor.getBlocked()).isEqualTo(DEFAULT_BLOCKED);
        assertThat(testDoctor.getPicture()).isEqualTo(DEFAULT_PICTURE);*/
    }
/*
    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setLogin(null);

        // Create the Doctor, which fails.

        restDoctorMockMvc.perform(post("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctor)))
                .andExpect(status().isBadRequest());

        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setPassword(null);

        // Create the Doctor, which fails.

        restDoctorMockMvc.perform(post("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctor)))
                .andExpect(status().isBadRequest());

        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctors
        restDoctorMockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
                .andExpect(jsonPath("$.[*].blocked").value(hasItem(DEFAULT_BLOCKED.booleanValue())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.blocked").value(DEFAULT_BLOCKED.booleanValue()))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

		int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        doctor.setLogin(UPDATED_LOGIN);
        doctor.setPassword(UPDATED_PASSWORD);
        doctor.setFirstName(UPDATED_FIRST_NAME);
        doctor.setLastName(UPDATED_LAST_NAME);
        doctor.setEmail(UPDATED_EMAIL);
        doctor.setActivated(UPDATED_ACTIVATED);
        doctor.setBlocked(UPDATED_BLOCKED);
        doctor.setPicture(UPDATED_PICTURE);


        restDoctorMockMvc.perform(put("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctor)))
                .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctors.get(doctors.size() - 1);
        assertThat(testDoctor.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testDoctor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDoctor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDoctor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDoctor.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testDoctor.getBlocked()).isEqualTo(UPDATED_BLOCKED);
        assertThat(testDoctor.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

		int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Get the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeDelete - 1);
    }
    */
}
