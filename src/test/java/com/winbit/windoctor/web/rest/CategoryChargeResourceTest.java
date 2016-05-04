package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.CategoryCharge;
import com.winbit.windoctor.repository.CategoryChargeRepository;
import com.winbit.windoctor.repository.search.CategoryChargeSearchRepository;

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
 * Test class for the CategoryChargeResource REST controller.
 *
 * @see CategoryChargeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CategoryChargeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private CategoryChargeRepository categoryChargeRepository;

    @Inject
    private CategoryChargeSearchRepository categoryChargeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restCategoryChargeMockMvc;

    private CategoryCharge categoryCharge;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        CategoryChargeResource categoryChargeResource = new CategoryChargeResource();
        ReflectionTestUtils.setField(categoryChargeResource, "categoryChargeRepository", categoryChargeRepository);
        ReflectionTestUtils.setField(categoryChargeResource, "categoryChargeSearchRepository", categoryChargeSearchRepository);
        this.restCategoryChargeMockMvc = MockMvcBuilders.standaloneSetup(categoryChargeResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        /*categoryCharge = new CategoryCharge();
        categoryCharge.setName(DEFAULT_NAME);*/
    }

    @Test
    @Transactional
    public void createCategoryCharge() throws Exception {
        /*int databaseSizeBeforeCreate = categoryChargeRepository.findAll().size();

        // Create the CategoryCharge

        restCategoryChargeMockMvc.perform(post("/api/categoryCharges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryCharge)))
                .andExpect(status().isCreated());

        // Validate the CategoryCharge in the database
        List<CategoryCharge> categoryCharges = categoryChargeRepository.findAll();
        assertThat(categoryCharges).hasSize(databaseSizeBeforeCreate + 1);
        CategoryCharge testCategoryCharge = categoryCharges.get(categoryCharges.size() - 1);
        assertThat(testCategoryCharge.getName()).isEqualTo(DEFAULT_NAME);*/
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = categoryChargeRepository.findAll().size();
        // set the field null
        categoryCharge.setName(null);

        // Create the CategoryCharge, which fails.

        restCategoryChargeMockMvc.perform(post("/api/categoryCharges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryCharge)))
                .andExpect(status().isBadRequest());

        List<CategoryCharge> categoryCharges = categoryChargeRepository.findAll();
        assertThat(categoryCharges).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllCategoryCharges() throws Exception {
        // Initialize the database
        /*categoryChargeRepository.saveAndFlush(categoryCharge);

        // Get all the categoryCharges
        restCategoryChargeMockMvc.perform(get("/api/categoryCharges"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categoryCharge.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));*/
    }

    @Test
    @Transactional
    public void getCategoryCharge() throws Exception {
        // Initialize the database
        /*categoryChargeRepository.saveAndFlush(categoryCharge);

        // Get the categoryCharge
        restCategoryChargeMockMvc.perform(get("/api/categoryCharges/{id}", categoryCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categoryCharge.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));*/
    }

    @Test
    @Transactional
    public void getNonExistingCategoryCharge() throws Exception {
        // Get the categoryCharge
        /*restCategoryChargeMockMvc.perform(get("/api/categoryCharges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updateCategoryCharge() throws Exception {
        // Initialize the database
        /*categoryChargeRepository.saveAndFlush(categoryCharge);

		int databaseSizeBeforeUpdate = categoryChargeRepository.findAll().size();

        // Update the categoryCharge
        categoryCharge.setName(UPDATED_NAME);


        restCategoryChargeMockMvc.perform(put("/api/categoryCharges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryCharge)))
                .andExpect(status().isOk());

        // Validate the CategoryCharge in the database
        List<CategoryCharge> categoryCharges = categoryChargeRepository.findAll();
        assertThat(categoryCharges).hasSize(databaseSizeBeforeUpdate);
        CategoryCharge testCategoryCharge = categoryCharges.get(categoryCharges.size() - 1);
        assertThat(testCategoryCharge.getName()).isEqualTo(UPDATED_NAME);*/
    }

    @Test
    @Transactional
    public void deleteCategoryCharge() throws Exception {
        // Initialize the database
        /*categoryChargeRepository.saveAndFlush(categoryCharge);

		int databaseSizeBeforeDelete = categoryChargeRepository.findAll().size();

        // Get the categoryCharge
        restCategoryChargeMockMvc.perform(delete("/api/categoryCharges/{id}", categoryCharge.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoryCharge> categoryCharges = categoryChargeRepository.findAll();
        assertThat(categoryCharges).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
