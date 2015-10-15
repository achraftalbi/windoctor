package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.TestEntity3;
import com.winbit.windoctor.repository.TestEntity3Repository;
import com.winbit.windoctor.repository.search.TestEntity3SearchRepository;

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
 * Test class for the TestEntity3Resource REST controller.
 *
 * @see TestEntity3Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestEntity3ResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private TestEntity3Repository testEntity3Repository;

    @Inject
    private TestEntity3SearchRepository testEntity3SearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTestEntity3MockMvc;

    private TestEntity3 testEntity3;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestEntity3Resource testEntity3Resource = new TestEntity3Resource();
        ReflectionTestUtils.setField(testEntity3Resource, "testEntity3Repository", testEntity3Repository);
        ReflectionTestUtils.setField(testEntity3Resource, "testEntity3SearchRepository", testEntity3SearchRepository);
        this.restTestEntity3MockMvc = MockMvcBuilders.standaloneSetup(testEntity3Resource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testEntity3 = new TestEntity3();
        testEntity3.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTestEntity3() throws Exception {
        int databaseSizeBeforeCreate = testEntity3Repository.findAll().size();

        // Create the TestEntity3

        restTestEntity3MockMvc.perform(post("/api/testEntity3s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity3)))
                .andExpect(status().isCreated());

        // Validate the TestEntity3 in the database
        List<TestEntity3> testEntity3s = testEntity3Repository.findAll();
        assertThat(testEntity3s).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity3 testTestEntity3 = testEntity3s.get(testEntity3s.size() - 1);
        assertThat(testTestEntity3.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = testEntity3Repository.findAll().size();
        // set the field null
        testEntity3.setDescription(null);

        // Create the TestEntity3, which fails.

        restTestEntity3MockMvc.perform(post("/api/testEntity3s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity3)))
                .andExpect(status().isBadRequest());

        List<TestEntity3> testEntity3s = testEntity3Repository.findAll();
        assertThat(testEntity3s).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestEntity3s() throws Exception {
        // Initialize the database
        testEntity3Repository.saveAndFlush(testEntity3);

        // Get all the testEntity3s
        restTestEntity3MockMvc.perform(get("/api/testEntity3s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity3.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTestEntity3() throws Exception {
        // Initialize the database
        testEntity3Repository.saveAndFlush(testEntity3);

        // Get the testEntity3
        restTestEntity3MockMvc.perform(get("/api/testEntity3s/{id}", testEntity3.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testEntity3.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestEntity3() throws Exception {
        // Get the testEntity3
        restTestEntity3MockMvc.perform(get("/api/testEntity3s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestEntity3() throws Exception {
        // Initialize the database
        testEntity3Repository.saveAndFlush(testEntity3);

		int databaseSizeBeforeUpdate = testEntity3Repository.findAll().size();

        // Update the testEntity3
        testEntity3.setDescription(UPDATED_DESCRIPTION);
        

        restTestEntity3MockMvc.perform(put("/api/testEntity3s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity3)))
                .andExpect(status().isOk());

        // Validate the TestEntity3 in the database
        List<TestEntity3> testEntity3s = testEntity3Repository.findAll();
        assertThat(testEntity3s).hasSize(databaseSizeBeforeUpdate);
        TestEntity3 testTestEntity3 = testEntity3s.get(testEntity3s.size() - 1);
        assertThat(testTestEntity3.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTestEntity3() throws Exception {
        // Initialize the database
        testEntity3Repository.saveAndFlush(testEntity3);

		int databaseSizeBeforeDelete = testEntity3Repository.findAll().size();

        // Get the testEntity3
        restTestEntity3MockMvc.perform(delete("/api/testEntity3s/{id}", testEntity3.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestEntity3> testEntity3s = testEntity3Repository.findAll();
        assertThat(testEntity3s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
