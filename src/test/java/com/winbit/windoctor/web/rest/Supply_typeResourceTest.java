package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Supply_type;
import com.winbit.windoctor.repository.Supply_typeRepository;
import com.winbit.windoctor.repository.search.Supply_typeSearchRepository;

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
 * Test class for the Supply_typeResource REST controller.
 *
 * @see Supply_typeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Supply_typeResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private Supply_typeRepository supply_typeRepository;

    @Inject
    private Supply_typeSearchRepository supply_typeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restSupply_typeMockMvc;

    private Supply_type supply_type;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Supply_typeResource supply_typeResource = new Supply_typeResource();
        ReflectionTestUtils.setField(supply_typeResource, "supply_typeRepository", supply_typeRepository);
        ReflectionTestUtils.setField(supply_typeResource, "supply_typeSearchRepository", supply_typeSearchRepository);
        this.restSupply_typeMockMvc = MockMvcBuilders.standaloneSetup(supply_typeResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        supply_type = new Supply_type();
        supply_type.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSupply_type() throws Exception {
        int databaseSizeBeforeCreate = supply_typeRepository.findAll().size();

        // Create the Supply_type

        restSupply_typeMockMvc.perform(post("/api/supply_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supply_type)))
                .andExpect(status().isCreated());

        // Validate the Supply_type in the database
        List<Supply_type> supply_types = supply_typeRepository.findAll();
        assertThat(supply_types).hasSize(databaseSizeBeforeCreate + 1);
        Supply_type testSupply_type = supply_types.get(supply_types.size() - 1);
        assertThat(testSupply_type.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = supply_typeRepository.findAll().size();
        // set the field null
        supply_type.setDescription(null);

        // Create the Supply_type, which fails.

        restSupply_typeMockMvc.perform(post("/api/supply_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supply_type)))
                .andExpect(status().isBadRequest());

        List<Supply_type> supply_types = supply_typeRepository.findAll();
        assertThat(supply_types).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupply_types() throws Exception {
        // Initialize the database
        supply_typeRepository.saveAndFlush(supply_type);

        // Get all the supply_types
        restSupply_typeMockMvc.perform(get("/api/supply_types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supply_type.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSupply_type() throws Exception {
        // Initialize the database
        supply_typeRepository.saveAndFlush(supply_type);

        // Get the supply_type
        restSupply_typeMockMvc.perform(get("/api/supply_types/{id}", supply_type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(supply_type.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupply_type() throws Exception {
        // Get the supply_type
        restSupply_typeMockMvc.perform(get("/api/supply_types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupply_type() throws Exception {
        // Initialize the database
        supply_typeRepository.saveAndFlush(supply_type);

		int databaseSizeBeforeUpdate = supply_typeRepository.findAll().size();

        // Update the supply_type
        supply_type.setDescription(UPDATED_DESCRIPTION);
        

        restSupply_typeMockMvc.perform(put("/api/supply_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supply_type)))
                .andExpect(status().isOk());

        // Validate the Supply_type in the database
        List<Supply_type> supply_types = supply_typeRepository.findAll();
        assertThat(supply_types).hasSize(databaseSizeBeforeUpdate);
        Supply_type testSupply_type = supply_types.get(supply_types.size() - 1);
        assertThat(testSupply_type.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteSupply_type() throws Exception {
        // Initialize the database
        supply_typeRepository.saveAndFlush(supply_type);

		int databaseSizeBeforeDelete = supply_typeRepository.findAll().size();

        // Get the supply_type
        restSupply_typeMockMvc.perform(delete("/api/supply_types/{id}", supply_type.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Supply_type> supply_types = supply_typeRepository.findAll();
        assertThat(supply_types).hasSize(databaseSizeBeforeDelete - 1);
    }
}
