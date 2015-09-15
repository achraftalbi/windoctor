package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Event_reason;
import com.winbit.windoctor.repository.Event_reasonRepository;
import com.winbit.windoctor.repository.search.Event_reasonSearchRepository;

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
 * Test class for the Event_reasonResource REST controller.
 *
 * @see Event_reasonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Event_reasonResourceTest {

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private Event_reasonRepository event_reasonRepository;

    @Inject
    private Event_reasonSearchRepository event_reasonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEvent_reasonMockMvc;

    private Event_reason event_reason;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Event_reasonResource event_reasonResource = new Event_reasonResource();
        ReflectionTestUtils.setField(event_reasonResource, "event_reasonRepository", event_reasonRepository);
        ReflectionTestUtils.setField(event_reasonResource, "event_reasonSearchRepository", event_reasonSearchRepository);
        this.restEvent_reasonMockMvc = MockMvcBuilders.standaloneSetup(event_reasonResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        event_reason = new Event_reason();
        event_reason.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEvent_reason() throws Exception {
        int databaseSizeBeforeCreate = event_reasonRepository.findAll().size();

        // Create the Event_reason

        restEvent_reasonMockMvc.perform(post("/api/event_reasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event_reason)))
                .andExpect(status().isCreated());

        // Validate the Event_reason in the database
        List<Event_reason> event_reasons = event_reasonRepository.findAll();
        assertThat(event_reasons).hasSize(databaseSizeBeforeCreate + 1);
        Event_reason testEvent_reason = event_reasons.get(event_reasons.size() - 1);
        assertThat(testEvent_reason.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = event_reasonRepository.findAll().size();
        // set the field null
        event_reason.setDescription(null);

        // Create the Event_reason, which fails.

        restEvent_reasonMockMvc.perform(post("/api/event_reasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event_reason)))
                .andExpect(status().isBadRequest());

        List<Event_reason> event_reasons = event_reasonRepository.findAll();
        assertThat(event_reasons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvent_reasons() throws Exception {
        // Initialize the database
        event_reasonRepository.saveAndFlush(event_reason);

        // Get all the event_reasons
        restEvent_reasonMockMvc.perform(get("/api/event_reasons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(event_reason.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEvent_reason() throws Exception {
        // Initialize the database
        event_reasonRepository.saveAndFlush(event_reason);

        // Get the event_reason
        restEvent_reasonMockMvc.perform(get("/api/event_reasons/{id}", event_reason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(event_reason.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvent_reason() throws Exception {
        // Get the event_reason
        restEvent_reasonMockMvc.perform(get("/api/event_reasons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent_reason() throws Exception {
        // Initialize the database
        event_reasonRepository.saveAndFlush(event_reason);

		int databaseSizeBeforeUpdate = event_reasonRepository.findAll().size();

        // Update the event_reason
        event_reason.setDescription(UPDATED_DESCRIPTION);
        

        restEvent_reasonMockMvc.perform(put("/api/event_reasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event_reason)))
                .andExpect(status().isOk());

        // Validate the Event_reason in the database
        List<Event_reason> event_reasons = event_reasonRepository.findAll();
        assertThat(event_reasons).hasSize(databaseSizeBeforeUpdate);
        Event_reason testEvent_reason = event_reasons.get(event_reasons.size() - 1);
        assertThat(testEvent_reason.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteEvent_reason() throws Exception {
        // Initialize the database
        event_reasonRepository.saveAndFlush(event_reason);

		int databaseSizeBeforeDelete = event_reasonRepository.findAll().size();

        // Get the event_reason
        restEvent_reasonMockMvc.perform(delete("/api/event_reasons/{id}", event_reason.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Event_reason> event_reasons = event_reasonRepository.findAll();
        assertThat(event_reasons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
