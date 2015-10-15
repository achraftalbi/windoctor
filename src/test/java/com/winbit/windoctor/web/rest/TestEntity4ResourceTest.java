package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.TestEntity4;
import com.winbit.windoctor.repository.TestEntity4Repository;
import com.winbit.windoctor.repository.search.TestEntity4SearchRepository;

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
 * Test class for the TestEntity4Resource REST controller.
 *
 * @see TestEntity4Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestEntity4ResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private TestEntity4Repository testEntity4Repository;

    @Inject
    private TestEntity4SearchRepository testEntity4SearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTestEntity4MockMvc;

    private TestEntity4 testEntity4;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestEntity4Resource testEntity4Resource = new TestEntity4Resource();
        ReflectionTestUtils.setField(testEntity4Resource, "testEntity4Repository", testEntity4Repository);
        ReflectionTestUtils.setField(testEntity4Resource, "testEntity4SearchRepository", testEntity4SearchRepository);
        this.restTestEntity4MockMvc = MockMvcBuilders.standaloneSetup(testEntity4Resource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testEntity4 = new TestEntity4();
        testEntity4.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTestEntity4() throws Exception {
        int databaseSizeBeforeCreate = testEntity4Repository.findAll().size();

        // Create the TestEntity4

        restTestEntity4MockMvc.perform(post("/api/testEntity4s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity4)))
                .andExpect(status().isCreated());

        // Validate the TestEntity4 in the database
        List<TestEntity4> testEntity4s = testEntity4Repository.findAll();
        assertThat(testEntity4s).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity4 testTestEntity4 = testEntity4s.get(testEntity4s.size() - 1);
        assertThat(testTestEntity4.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTestEntity4s() throws Exception {
        // Initialize the database
        testEntity4Repository.saveAndFlush(testEntity4);

        // Get all the testEntity4s
        restTestEntity4MockMvc.perform(get("/api/testEntity4s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity4.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTestEntity4() throws Exception {
        // Initialize the database
        testEntity4Repository.saveAndFlush(testEntity4);

        // Get the testEntity4
        restTestEntity4MockMvc.perform(get("/api/testEntity4s/{id}", testEntity4.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testEntity4.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestEntity4() throws Exception {
        // Get the testEntity4
        restTestEntity4MockMvc.perform(get("/api/testEntity4s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestEntity4() throws Exception {
        // Initialize the database
        testEntity4Repository.saveAndFlush(testEntity4);

		int databaseSizeBeforeUpdate = testEntity4Repository.findAll().size();

        // Update the testEntity4
        testEntity4.setDescription(UPDATED_DESCRIPTION);
        

        restTestEntity4MockMvc.perform(put("/api/testEntity4s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity4)))
                .andExpect(status().isOk());

        // Validate the TestEntity4 in the database
        List<TestEntity4> testEntity4s = testEntity4Repository.findAll();
        assertThat(testEntity4s).hasSize(databaseSizeBeforeUpdate);
        TestEntity4 testTestEntity4 = testEntity4s.get(testEntity4s.size() - 1);
        assertThat(testTestEntity4.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTestEntity4() throws Exception {
        // Initialize the database
        testEntity4Repository.saveAndFlush(testEntity4);

		int databaseSizeBeforeDelete = testEntity4Repository.findAll().size();

        // Get the testEntity4
        restTestEntity4MockMvc.perform(delete("/api/testEntity4s/{id}", testEntity4.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestEntity4> testEntity4s = testEntity4Repository.findAll();
        assertThat(testEntity4s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
