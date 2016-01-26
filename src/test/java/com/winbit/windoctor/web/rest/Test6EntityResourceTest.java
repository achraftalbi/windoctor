package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Test6Entity;
import com.winbit.windoctor.repository.Test6EntityRepository;
import com.winbit.windoctor.repository.search.Test6EntitySearchRepository;

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
 * Test class for the Test6EntityResource REST controller.
 *
 * @see Test6EntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Test6EntityResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private Test6EntityRepository test6EntityRepository;

    @Inject
    private Test6EntitySearchRepository test6EntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTest6EntityMockMvc;

    private Test6Entity test6Entity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Test6EntityResource test6EntityResource = new Test6EntityResource();
        ReflectionTestUtils.setField(test6EntityResource, "test6EntityRepository", test6EntityRepository);
        ReflectionTestUtils.setField(test6EntityResource, "test6EntitySearchRepository", test6EntitySearchRepository);
        this.restTest6EntityMockMvc = MockMvcBuilders.standaloneSetup(test6EntityResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        test6Entity = new Test6Entity();
        test6Entity.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTest6Entity() throws Exception {
        int databaseSizeBeforeCreate = test6EntityRepository.findAll().size();

        // Create the Test6Entity

        restTest6EntityMockMvc.perform(post("/api/test6Entitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(test6Entity)))
                .andExpect(status().isCreated());

        // Validate the Test6Entity in the database
        List<Test6Entity> test6Entitys = test6EntityRepository.findAll();
        assertThat(test6Entitys).hasSize(databaseSizeBeforeCreate + 1);
        Test6Entity testTest6Entity = test6Entitys.get(test6Entitys.size() - 1);
        assertThat(testTest6Entity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = test6EntityRepository.findAll().size();
        // set the field null
        test6Entity.setDescription(null);

        // Create the Test6Entity, which fails.

        restTest6EntityMockMvc.perform(post("/api/test6Entitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(test6Entity)))
                .andExpect(status().isBadRequest());

        List<Test6Entity> test6Entitys = test6EntityRepository.findAll();
        assertThat(test6Entitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTest6Entitys() throws Exception {
        // Initialize the database
        test6EntityRepository.saveAndFlush(test6Entity);

        // Get all the test6Entitys
        restTest6EntityMockMvc.perform(get("/api/test6Entitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(test6Entity.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTest6Entity() throws Exception {
        // Initialize the database
        test6EntityRepository.saveAndFlush(test6Entity);

        // Get the test6Entity
        restTest6EntityMockMvc.perform(get("/api/test6Entitys/{id}", test6Entity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(test6Entity.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTest6Entity() throws Exception {
        // Get the test6Entity
        restTest6EntityMockMvc.perform(get("/api/test6Entitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTest6Entity() throws Exception {
        // Initialize the database
        test6EntityRepository.saveAndFlush(test6Entity);

		int databaseSizeBeforeUpdate = test6EntityRepository.findAll().size();

        // Update the test6Entity
        test6Entity.setDescription(UPDATED_DESCRIPTION);
        

        restTest6EntityMockMvc.perform(put("/api/test6Entitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(test6Entity)))
                .andExpect(status().isOk());

        // Validate the Test6Entity in the database
        List<Test6Entity> test6Entitys = test6EntityRepository.findAll();
        assertThat(test6Entitys).hasSize(databaseSizeBeforeUpdate);
        Test6Entity testTest6Entity = test6Entitys.get(test6Entitys.size() - 1);
        assertThat(testTest6Entity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteTest6Entity() throws Exception {
        // Initialize the database
        test6EntityRepository.saveAndFlush(test6Entity);

		int databaseSizeBeforeDelete = test6EntityRepository.findAll().size();

        // Get the test6Entity
        restTest6EntityMockMvc.perform(delete("/api/test6Entitys/{id}", test6Entity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Test6Entity> test6Entitys = test6EntityRepository.findAll();
        assertThat(test6Entitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
