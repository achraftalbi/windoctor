package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.TestEntity2;
import com.winbit.windoctor.repository.TestEntity2Repository;
import com.winbit.windoctor.repository.search.TestEntity2SearchRepository;

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
 * Test class for the TestEntity2Resource REST controller.
 *
 * @see TestEntity2Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestEntity2ResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private TestEntity2Repository testEntity2Repository;

    @Inject
    private TestEntity2SearchRepository testEntity2SearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTestEntity2MockMvc;

    private TestEntity2 testEntity2;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestEntity2Resource testEntity2Resource = new TestEntity2Resource();
        ReflectionTestUtils.setField(testEntity2Resource, "testEntity2Repository", testEntity2Repository);
        ReflectionTestUtils.setField(testEntity2Resource, "testEntity2SearchRepository", testEntity2SearchRepository);
        this.restTestEntity2MockMvc = MockMvcBuilders.standaloneSetup(testEntity2Resource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testEntity2 = new TestEntity2();
        testEntity2.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTestEntity2() throws Exception {
        int databaseSizeBeforeCreate = testEntity2Repository.findAll().size();

        // Create the TestEntity2

        restTestEntity2MockMvc.perform(post("/api/testEntity2s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity2)))
                .andExpect(status().isCreated());

        // Validate the TestEntity2 in the database
        List<TestEntity2> testEntity2s = testEntity2Repository.findAll();
        assertThat(testEntity2s).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity2 testTestEntity2 = testEntity2s.get(testEntity2s.size() - 1);
        assertThat(testTestEntity2.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = testEntity2Repository.findAll().size();
        // set the field null
        testEntity2.setDescription(null);

        // Create the TestEntity2, which fails.

        restTestEntity2MockMvc.perform(post("/api/testEntity2s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity2)))
                .andExpect(status().isBadRequest());

        List<TestEntity2> testEntity2s = testEntity2Repository.findAll();
        assertThat(testEntity2s).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestEntity2s() throws Exception {
        // Initialize the database
        testEntity2Repository.saveAndFlush(testEntity2);

        // Get all the testEntity2s
        restTestEntity2MockMvc.perform(get("/api/testEntity2s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity2.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTestEntity2() throws Exception {
        // Initialize the database
        testEntity2Repository.saveAndFlush(testEntity2);

        // Get the testEntity2
        restTestEntity2MockMvc.perform(get("/api/testEntity2s/{id}", testEntity2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testEntity2.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestEntity2() throws Exception {
        // Get the testEntity2
        restTestEntity2MockMvc.perform(get("/api/testEntity2s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestEntity2() throws Exception {
        // Initialize the database
        testEntity2Repository.saveAndFlush(testEntity2);

		int databaseSizeBeforeUpdate = testEntity2Repository.findAll().size();

        // Update the testEntity2
        testEntity2.setDescription(UPDATED_DESCRIPTION);
        

        restTestEntity2MockMvc.perform(put("/api/testEntity2s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity2)))
                .andExpect(status().isOk());

        // Validate the TestEntity2 in the database
        List<TestEntity2> testEntity2s = testEntity2Repository.findAll();
        assertThat(testEntity2s).hasSize(databaseSizeBeforeUpdate);
        TestEntity2 testTestEntity2 = testEntity2s.get(testEntity2s.size() - 1);
        assertThat(testTestEntity2.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTestEntity2() throws Exception {
        // Initialize the database
        testEntity2Repository.saveAndFlush(testEntity2);

		int databaseSizeBeforeDelete = testEntity2Repository.findAll().size();

        // Get the testEntity2
        restTestEntity2MockMvc.perform(delete("/api/testEntity2s/{id}", testEntity2.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestEntity2> testEntity2s = testEntity2Repository.findAll();
        assertThat(testEntity2s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
