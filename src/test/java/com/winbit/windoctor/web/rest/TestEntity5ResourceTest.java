package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.TestEntity5;
import com.winbit.windoctor.repository.TestEntity5Repository;
import com.winbit.windoctor.repository.search.TestEntity5SearchRepository;

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
 * Test class for the TestEntity5Resource REST controller.
 *
 * @see TestEntity5Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestEntity5ResourceTest {

    private static final String DEFAULT_DESCRIPTION5 = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION5 = "UPDATED_TEXT";

    @Inject
    private TestEntity5Repository testEntity5Repository;

    @Inject
    private TestEntity5SearchRepository testEntity5SearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTestEntity5MockMvc;

    private TestEntity5 testEntity5;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestEntity5Resource testEntity5Resource = new TestEntity5Resource();
        ReflectionTestUtils.setField(testEntity5Resource, "testEntity5Repository", testEntity5Repository);
        ReflectionTestUtils.setField(testEntity5Resource, "testEntity5SearchRepository", testEntity5SearchRepository);
        this.restTestEntity5MockMvc = MockMvcBuilders.standaloneSetup(testEntity5Resource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testEntity5 = new TestEntity5();
        testEntity5.setDescription5(DEFAULT_DESCRIPTION5);
    }

    @Test
    @Transactional
    public void createTestEntity5() throws Exception {
        int databaseSizeBeforeCreate = testEntity5Repository.findAll().size();

        // Create the TestEntity5

        restTestEntity5MockMvc.perform(post("/api/testEntity5s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity5)))
                .andExpect(status().isCreated());

        // Validate the TestEntity5 in the database
        List<TestEntity5> testEntity5s = testEntity5Repository.findAll();
        assertThat(testEntity5s).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity5 testTestEntity5 = testEntity5s.get(testEntity5s.size() - 1);
        assertThat(testTestEntity5.getDescription5()).isEqualTo(DEFAULT_DESCRIPTION5);
    }

    @Test
    @Transactional
    public void getAllTestEntity5s() throws Exception {
        // Initialize the database
        testEntity5Repository.saveAndFlush(testEntity5);

        // Get all the testEntity5s
        restTestEntity5MockMvc.perform(get("/api/testEntity5s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity5.getId().intValue())))
                .andExpect(jsonPath("$.[*].description5").value(hasItem(DEFAULT_DESCRIPTION5.toString())));
    }

    @Test
    @Transactional
    public void getTestEntity5() throws Exception {
        // Initialize the database
        testEntity5Repository.saveAndFlush(testEntity5);

        // Get the testEntity5
        restTestEntity5MockMvc.perform(get("/api/testEntity5s/{id}", testEntity5.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testEntity5.getId().intValue()))
            .andExpect(jsonPath("$.description5").value(DEFAULT_DESCRIPTION5.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestEntity5() throws Exception {
        // Get the testEntity5
        restTestEntity5MockMvc.perform(get("/api/testEntity5s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestEntity5() throws Exception {
        // Initialize the database
        testEntity5Repository.saveAndFlush(testEntity5);

		int databaseSizeBeforeUpdate = testEntity5Repository.findAll().size();

        // Update the testEntity5
        testEntity5.setDescription5(UPDATED_DESCRIPTION5);
        

        restTestEntity5MockMvc.perform(put("/api/testEntity5s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity5)))
                .andExpect(status().isOk());

        // Validate the TestEntity5 in the database
        List<TestEntity5> testEntity5s = testEntity5Repository.findAll();
        assertThat(testEntity5s).hasSize(databaseSizeBeforeUpdate);
        TestEntity5 testTestEntity5 = testEntity5s.get(testEntity5s.size() - 1);
        assertThat(testTestEntity5.getDescription5()).isEqualTo(UPDATED_DESCRIPTION5);
    }

    @Test
    @Transactional
    public void deleteTestEntity5() throws Exception {
        // Initialize the database
        testEntity5Repository.saveAndFlush(testEntity5);

		int databaseSizeBeforeDelete = testEntity5Repository.findAll().size();

        // Get the testEntity5
        restTestEntity5MockMvc.perform(delete("/api/testEntity5s/{id}", testEntity5.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestEntity5> testEntity5s = testEntity5Repository.findAll();
        assertThat(testEntity5s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
