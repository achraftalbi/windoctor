package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.EntityTest1;
import com.winbit.windoctor.repository.EntityTest1Repository;
import com.winbit.windoctor.repository.search.EntityTest1SearchRepository;

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
 * Test class for the EntityTest1Resource REST controller.
 *
 * @see EntityTest1Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EntityTest1ResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private EntityTest1Repository entityTest1Repository;

    @Inject
    private EntityTest1SearchRepository entityTest1SearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEntityTest1MockMvc;

    private EntityTest1 entityTest1;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityTest1Resource entityTest1Resource = new EntityTest1Resource();
        ReflectionTestUtils.setField(entityTest1Resource, "entityTest1Repository", entityTest1Repository);
        ReflectionTestUtils.setField(entityTest1Resource, "entityTest1SearchRepository", entityTest1SearchRepository);
        this.restEntityTest1MockMvc = MockMvcBuilders.standaloneSetup(entityTest1Resource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entityTest1 = new EntityTest1();
        entityTest1.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEntityTest1() throws Exception {
        int databaseSizeBeforeCreate = entityTest1Repository.findAll().size();

        // Create the EntityTest1

        restEntityTest1MockMvc.perform(post("/api/entityTest1s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entityTest1)))
                .andExpect(status().isCreated());

        // Validate the EntityTest1 in the database
        List<EntityTest1> entityTest1s = entityTest1Repository.findAll();
        assertThat(entityTest1s).hasSize(databaseSizeBeforeCreate + 1);
        EntityTest1 testEntityTest1 = entityTest1s.get(entityTest1s.size() - 1);
        assertThat(testEntityTest1.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEntityTest1s() throws Exception {
        // Initialize the database
        entityTest1Repository.saveAndFlush(entityTest1);

        // Get all the entityTest1s
        restEntityTest1MockMvc.perform(get("/api/entityTest1s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entityTest1.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEntityTest1() throws Exception {
        // Initialize the database
        entityTest1Repository.saveAndFlush(entityTest1);

        // Get the entityTest1
        restEntityTest1MockMvc.perform(get("/api/entityTest1s/{id}", entityTest1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entityTest1.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntityTest1() throws Exception {
        // Get the entityTest1
        restEntityTest1MockMvc.perform(get("/api/entityTest1s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityTest1() throws Exception {
        // Initialize the database
        entityTest1Repository.saveAndFlush(entityTest1);

		int databaseSizeBeforeUpdate = entityTest1Repository.findAll().size();

        // Update the entityTest1
        entityTest1.setDescription(UPDATED_DESCRIPTION);
        

        restEntityTest1MockMvc.perform(put("/api/entityTest1s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entityTest1)))
                .andExpect(status().isOk());

        // Validate the EntityTest1 in the database
        List<EntityTest1> entityTest1s = entityTest1Repository.findAll();
        assertThat(entityTest1s).hasSize(databaseSizeBeforeUpdate);
        EntityTest1 testEntityTest1 = entityTest1s.get(entityTest1s.size() - 1);
        assertThat(testEntityTest1.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteEntityTest1() throws Exception {
        // Initialize the database
        entityTest1Repository.saveAndFlush(entityTest1);

		int databaseSizeBeforeDelete = entityTest1Repository.findAll().size();

        // Get the entityTest1
        restEntityTest1MockMvc.perform(delete("/api/entityTest1s/{id}", entityTest1.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityTest1> entityTest1s = entityTest1Repository.findAll();
        assertThat(entityTest1s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
