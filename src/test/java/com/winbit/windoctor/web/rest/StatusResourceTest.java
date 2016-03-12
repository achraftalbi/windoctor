package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Status;
import com.winbit.windoctor.repository.StatusRepository;
import com.winbit.windoctor.repository.search.StatusSearchRepository;

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
 * Test class for the StatusResource REST controller.
 *
 * @see StatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StatusResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private StatusRepository statusRepository;

    @Inject
    private StatusSearchRepository statusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restStatusMockMvc;

    private Status status;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatusResource statusResource = new StatusResource();
        ReflectionTestUtils.setField(statusResource, "statusRepository", statusRepository);
        ReflectionTestUtils.setField(statusResource, "statusSearchRepository", statusSearchRepository);
        this.restStatusMockMvc = MockMvcBuilders.standaloneSetup(statusResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        status = new Status();
        status.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createStatus() throws Exception {
        /*int databaseSizeBeforeCreate = statusRepository.findAll().size();

        // Create the Status

        restStatusMockMvc.perform(post("/api/statuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(status)))
                .andExpect(status().isCreated());

        // Validate the Status in the database
        List<Status> statuss = statusRepository.findAll();
        assertThat(statuss).hasSize(databaseSizeBeforeCreate + 1);
        Status testStatus = statuss.get(statuss.size() - 1);
        assertThat(testStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);*/
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = statusRepository.findAll().size();
        // set the field null
        status.setDescription(null);

        // Create the Status, which fails.

        restStatusMockMvc.perform(post("/api/statuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(status)))
                .andExpect(status().isBadRequest());

        List<Status> statuss = statusRepository.findAll();
        assertThat(statuss).hasSize(databaseSizeBeforeTest);*/
    }

    /*@Test
    @Transactional
    public void getAllStatuss() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        // Get all the statuss
        restStatusMockMvc.perform(get("/api/statuss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(status.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getStatus() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        // Get the status
        restStatusMockMvc.perform(get("/api/statuss/{id}", status.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(status.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStatus() throws Exception {
        // Get the status
        restStatusMockMvc.perform(get("/api/statuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatus() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

		int databaseSizeBeforeUpdate = statusRepository.findAll().size();

        // Update the status
        status.setDescription(UPDATED_DESCRIPTION);


        restStatusMockMvc.perform(put("/api/statuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(status)))
                .andExpect(status().isOk());

        // Validate the Status in the database
        List<Status> statuss = statusRepository.findAll();
        assertThat(statuss).hasSize(databaseSizeBeforeUpdate);
        Status testStatus = statuss.get(statuss.size() - 1);
        assertThat(testStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteStatus() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

		int databaseSizeBeforeDelete = statusRepository.findAll().size();

        // Get the status
        restStatusMockMvc.perform(delete("/api/statuss/{id}", status.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Status> statuss = statusRepository.findAll();
        assertThat(statuss).hasSize(databaseSizeBeforeDelete - 1);
    }
    */
}
