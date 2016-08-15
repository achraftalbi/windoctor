package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Type_structure;
import com.winbit.windoctor.repository.Type_structureRepository;
import com.winbit.windoctor.repository.search.Type_structureSearchRepository;

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
 * Test class for the Type_structureResource REST controller.
 *
 * @see Type_structureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Type_structureResourceTest {

    private static final String DEFAULT_DESCRIPTION_EN = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION_EN = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION_FR = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION_FR = "UPDATED_TEXT";

    @Inject
    private Type_structureRepository type_structureRepository;

    @Inject
    private Type_structureSearchRepository type_structureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restType_structureMockMvc;

    private Type_structure type_structure;

    @PostConstruct
    public void setup() {
        /*
        MockitoAnnotations.initMocks(this);
        Type_structureResource type_structureResource = new Type_structureResource();
        ReflectionTestUtils.setField(type_structureResource, "type_structureRepository", type_structureRepository);
        ReflectionTestUtils.setField(type_structureResource, "type_structureSearchRepository", type_structureSearchRepository);
        this.restType_structureMockMvc = MockMvcBuilders.standaloneSetup(type_structureResource).setMessageConverters(jacksonMessageConverter).build();
        */
    }

    @Before
    public void initTest() {
        /*
        type_structure = new Type_structure();
        type_structure.setDescription_en(DEFAULT_DESCRIPTION_EN);
        type_structure.setDescription_fr(DEFAULT_DESCRIPTION_FR);
        */
    }

    @Test
    @Transactional
    public void createType_structure() throws Exception {
        /*
        int databaseSizeBeforeCreate = type_structureRepository.findAll().size();

        // Create the Type_structure

        restType_structureMockMvc.perform(post("/api/type_structures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(type_structure)))
                .andExpect(status().isCreated());

        // Validate the Type_structure in the database
        List<Type_structure> type_structures = type_structureRepository.findAll();
        assertThat(type_structures).hasSize(databaseSizeBeforeCreate + 1);
        Type_structure testType_structure = type_structures.get(type_structures.size() - 1);
        assertThat(testType_structure.getDescription_en()).isEqualTo(DEFAULT_DESCRIPTION_EN);
        assertThat(testType_structure.getDescription_fr()).isEqualTo(DEFAULT_DESCRIPTION_FR);
        */
    }

    @Test
    @Transactional
    public void checkDescription_enIsRequired() throws Exception {
        /*
        int databaseSizeBeforeTest = type_structureRepository.findAll().size();
        // set the field null
        type_structure.setDescription_en(null);

        // Create the Type_structure, which fails.

        restType_structureMockMvc.perform(post("/api/type_structures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(type_structure)))
                .andExpect(status().isBadRequest());

        List<Type_structure> type_structures = type_structureRepository.findAll();
        assertThat(type_structures).hasSize(databaseSizeBeforeTest);
        */
    }

    @Test
    @Transactional
    public void checkDescription_frIsRequired() throws Exception {
         /*
       int databaseSizeBeforeTest = type_structureRepository.findAll().size();
        // set the field null
        type_structure.setDescription_fr(null);

        // Create the Type_structure, which fails.

        restType_structureMockMvc.perform(post("/api/type_structures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(type_structure)))
                .andExpect(status().isBadRequest());

        List<Type_structure> type_structures = type_structureRepository.findAll();
        assertThat(type_structures).hasSize(databaseSizeBeforeTest);
        */
    }

    @Test
    @Transactional
    public void getAllType_structures() throws Exception {
        // Initialize the database
         /*
       type_structureRepository.saveAndFlush(type_structure);

        // Get all the type_structures
        restType_structureMockMvc.perform(get("/api/type_structures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(type_structure.getId().intValue())))
                .andExpect(jsonPath("$.[*].description_en").value(hasItem(DEFAULT_DESCRIPTION_EN.toString())))
                .andExpect(jsonPath("$.[*].description_fr").value(hasItem(DEFAULT_DESCRIPTION_FR.toString())));
        */
    }

    @Test
    @Transactional
    public void getType_structure() throws Exception {
        // Initialize the database
         /*
       type_structureRepository.saveAndFlush(type_structure);

        // Get the type_structure
        restType_structureMockMvc.perform(get("/api/type_structures/{id}", type_structure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(type_structure.getId().intValue()))
            .andExpect(jsonPath("$.description_en").value(DEFAULT_DESCRIPTION_EN.toString()))
            .andExpect(jsonPath("$.description_fr").value(DEFAULT_DESCRIPTION_FR.toString()));
         */
    }

    @Test
    @Transactional
    public void getNonExistingType_structure() throws Exception {
        // Get the type_structure
        /*
        restType_structureMockMvc.perform(get("/api/type_structures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        */
    }

    @Test
    @Transactional
    public void updateType_structure() throws Exception {
        // Initialize the database
        /*
        type_structureRepository.saveAndFlush(type_structure);

		int databaseSizeBeforeUpdate = type_structureRepository.findAll().size();

        // Update the type_structure
        type_structure.setDescription_en(UPDATED_DESCRIPTION_EN);
        type_structure.setDescription_fr(UPDATED_DESCRIPTION_FR);


        restType_structureMockMvc.perform(put("/api/type_structures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(type_structure)))
                .andExpect(status().isOk());

        // Validate the Type_structure in the database
        List<Type_structure> type_structures = type_structureRepository.findAll();
        assertThat(type_structures).hasSize(databaseSizeBeforeUpdate);
        Type_structure testType_structure = type_structures.get(type_structures.size() - 1);
        assertThat(testType_structure.getDescription_en()).isEqualTo(UPDATED_DESCRIPTION_EN);
        assertThat(testType_structure.getDescription_fr()).isEqualTo(UPDATED_DESCRIPTION_FR);
        */
    }

    @Test
    @Transactional
    public void deleteType_structure() throws Exception {
        // Initialize the database
         /*
       type_structureRepository.saveAndFlush(type_structure);

		int databaseSizeBeforeDelete = type_structureRepository.findAll().size();

        // Get the type_structure
        restType_structureMockMvc.perform(delete("/api/type_structures/{id}", type_structure.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Type_structure> type_structures = type_structureRepository.findAll();
        assertThat(type_structures).hasSize(databaseSizeBeforeDelete - 1);
        */
    }
}
