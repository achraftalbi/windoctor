package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.repository.search.StructureSearchRepository;

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
 * Test class for the StructureResource REST controller.
 *
 * @see StructureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StructureResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private StructureRepository structureRepository;

    @Inject
    private StructureSearchRepository structureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restStructureMockMvc;

    private Structure structure;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StructureResource structureResource = new StructureResource();
        ReflectionTestUtils.setField(structureResource, "structureRepository", structureRepository);
        ReflectionTestUtils.setField(structureResource, "structureSearchRepository", structureSearchRepository);
        this.restStructureMockMvc = MockMvcBuilders.standaloneSetup(structureResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        structure = new Structure();
        structure.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createStructure() throws Exception {
        /*int databaseSizeBeforeCreate = structureRepository.findAll().size();

        // Create the Structure

        restStructureMockMvc.perform(post("/api/structures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(structure)))
                .andExpect(status().isCreated());

        // Validate the Structure in the database
        List<Structure> structures = structureRepository.findAll();
        assertThat(structures).hasSize(databaseSizeBeforeCreate + 1);
        Structure testStructure = structures.get(structures.size() - 1);
        assertThat(testStructure.getName()).isEqualTo(DEFAULT_NAME);*/
    }
/*
    @Test
    @Transactional
    public void getAllStructures() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        // Get all the structures
        restStructureMockMvc.perform(get("/api/structures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(structure.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        // Get the structure
        restStructureMockMvc.perform(get("/api/structures/{id}", structure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(structure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStructure() throws Exception {
        // Get the structure
        restStructureMockMvc.perform(get("/api/structures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

		int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure
        structure.setName(UPDATED_NAME);


        restStructureMockMvc.perform(put("/api/structures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(structure)))
                .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structures = structureRepository.findAll();
        assertThat(structures).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structures.get(structures.size() - 1);
        assertThat(testStructure.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

		int databaseSizeBeforeDelete = structureRepository.findAll().size();

        // Get the structure
        restStructureMockMvc.perform(delete("/api/structures/{id}", structure.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Structure> structures = structureRepository.findAll();
        assertThat(structures).hasSize(databaseSizeBeforeDelete - 1);
    }
    */
}
