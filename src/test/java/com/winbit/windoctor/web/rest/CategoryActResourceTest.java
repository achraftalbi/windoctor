package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.CategoryAct;
import com.winbit.windoctor.repository.CategoryActRepository;
import com.winbit.windoctor.repository.search.CategoryActSearchRepository;

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
 * Test class for the CategoryActResource REST controller.
 *
 * @see CategoryActResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CategoryActResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private CategoryActRepository categoryActRepository;

    @Inject
    private CategoryActSearchRepository categoryActSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restCategoryActMockMvc;

    private CategoryAct categoryAct;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        CategoryActResource categoryActResource = new CategoryActResource();
        ReflectionTestUtils.setField(categoryActResource, "categoryActRepository", categoryActRepository);
        ReflectionTestUtils.setField(categoryActResource, "categoryActSearchRepository", categoryActSearchRepository);
        this.restCategoryActMockMvc = MockMvcBuilders.standaloneSetup(categoryActResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        /*categoryAct = new CategoryAct();
        categoryAct.setName(DEFAULT_NAME);*/
    }

    @Test
    @Transactional
    public void createCategoryAct() throws Exception {
        /*int databaseSizeBeforeCreate = categoryActRepository.findAll().size();

        // Create the CategoryAct

        restCategoryActMockMvc.perform(post("/api/categoryActs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryAct)))
                .andExpect(status().isCreated());

        // Validate the CategoryAct in the database
        List<CategoryAct> categoryActs = categoryActRepository.findAll();
        assertThat(categoryActs).hasSize(databaseSizeBeforeCreate + 1);
        CategoryAct testCategoryAct = categoryActs.get(categoryActs.size() - 1);
        assertThat(testCategoryAct.getName()).isEqualTo(DEFAULT_NAME);*/
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = categoryActRepository.findAll().size();
        // set the field null
        categoryAct.setName(null);

        // Create the CategoryAct, which fails.

        restCategoryActMockMvc.perform(post("/api/categoryActs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryAct)))
                .andExpect(status().isBadRequest());

        List<CategoryAct> categoryActs = categoryActRepository.findAll();
        assertThat(categoryActs).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllCategoryActs() throws Exception {
        // Initialize the database
        /*categoryActRepository.saveAndFlush(categoryAct);

        // Get all the categoryActs
        restCategoryActMockMvc.perform(get("/api/categoryActs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categoryAct.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));*/
    }

    @Test
    @Transactional
    public void getCategoryAct() throws Exception {
        // Initialize the database
        /*categoryActRepository.saveAndFlush(categoryAct);

        // Get the categoryAct
        restCategoryActMockMvc.perform(get("/api/categoryActs/{id}", categoryAct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categoryAct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));*/
    }

    @Test
    @Transactional
    public void getNonExistingCategoryAct() throws Exception {
        // Get the categoryAct
        /*restCategoryActMockMvc.perform(get("/api/categoryActs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updateCategoryAct() throws Exception {
        // Initialize the database
        /*categoryActRepository.saveAndFlush(categoryAct);

		int databaseSizeBeforeUpdate = categoryActRepository.findAll().size();

        // Update the categoryAct
        categoryAct.setName(UPDATED_NAME);


        restCategoryActMockMvc.perform(put("/api/categoryActs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryAct)))
                .andExpect(status().isOk());

        // Validate the CategoryAct in the database
        List<CategoryAct> categoryActs = categoryActRepository.findAll();
        assertThat(categoryActs).hasSize(databaseSizeBeforeUpdate);
        CategoryAct testCategoryAct = categoryActs.get(categoryActs.size() - 1);
        assertThat(testCategoryAct.getName()).isEqualTo(UPDATED_NAME);*/
    }

    @Test
    @Transactional
    public void deleteCategoryAct() throws Exception {
        // Initialize the database
        /*categoryActRepository.saveAndFlush(categoryAct);

		int databaseSizeBeforeDelete = categoryActRepository.findAll().size();

        // Get the categoryAct
        restCategoryActMockMvc.perform(delete("/api/categoryActs/{id}", categoryAct.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoryAct> categoryActs = categoryActRepository.findAll();
        assertThat(categoryActs).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
