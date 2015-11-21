package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.MailType;
import com.winbit.windoctor.repository.MailTypeRepository;
import com.winbit.windoctor.repository.search.MailTypeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
 * Test class for the MailTypeResource REST controller.
 *
 * @see MailTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailTypeResourceTest {

    private static final String DEFAULT_LABEL = "AAAAA";
    private static final String UPDATED_LABEL = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    @Inject
    private MailTypeRepository mailTypeRepository;

    @Inject
    private MailTypeSearchRepository mailTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMailTypeMockMvc;

    private MailType mailType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MailTypeResource mailTypeResource = new MailTypeResource();
        ReflectionTestUtils.setField(mailTypeResource, "mailTypeRepository", mailTypeRepository);
        ReflectionTestUtils.setField(mailTypeResource, "mailTypeSearchRepository", mailTypeSearchRepository);
        this.restMailTypeMockMvc = MockMvcBuilders.standaloneSetup(mailTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mailType = new MailType();
        mailType.setLabel(DEFAULT_LABEL);
        mailType.setDescription(DEFAULT_DESCRIPTION);
        mailType.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createMailType() throws Exception {
        int databaseSizeBeforeCreate = mailTypeRepository.findAll().size();

        // Create the MailType

        restMailTypeMockMvc.perform(post("/api/mailTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailType)))
                .andExpect(status().isCreated());

        // Validate the MailType in the database
        List<MailType> mailTypes = mailTypeRepository.findAll();
        assertThat(mailTypes).hasSize(databaseSizeBeforeCreate + 1);
        MailType testMailType = mailTypes.get(mailTypes.size() - 1);
        assertThat(testMailType.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testMailType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMailType.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailTypeRepository.findAll().size();
        // set the field null
        mailType.setLabel(null);

        // Create the MailType, which fails.

        restMailTypeMockMvc.perform(post("/api/mailTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailType)))
                .andExpect(status().isBadRequest());

        List<MailType> mailTypes = mailTypeRepository.findAll();
        assertThat(mailTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailTypeRepository.findAll().size();
        // set the field null
        mailType.setDescription(null);

        // Create the MailType, which fails.

        restMailTypeMockMvc.perform(post("/api/mailTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailType)))
                .andExpect(status().isBadRequest());

        List<MailType> mailTypes = mailTypeRepository.findAll();
        assertThat(mailTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMailTypes() throws Exception {
        // Initialize the database
        mailTypeRepository.saveAndFlush(mailType);

        // Get all the mailTypes
        restMailTypeMockMvc.perform(get("/api/mailTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mailType.getId().intValue())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getMailType() throws Exception {
        // Initialize the database
        mailTypeRepository.saveAndFlush(mailType);

        // Get the mailType
        restMailTypeMockMvc.perform(get("/api/mailTypes/{id}", mailType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mailType.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMailType() throws Exception {
        // Get the mailType
        restMailTypeMockMvc.perform(get("/api/mailTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailType() throws Exception {
        // Initialize the database
        mailTypeRepository.saveAndFlush(mailType);

		int databaseSizeBeforeUpdate = mailTypeRepository.findAll().size();

        // Update the mailType
        mailType.setLabel(UPDATED_LABEL);
        mailType.setDescription(UPDATED_DESCRIPTION);
        mailType.setContent(UPDATED_CONTENT);

        restMailTypeMockMvc.perform(put("/api/mailTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailType)))
                .andExpect(status().isOk());

        // Validate the MailType in the database
        List<MailType> mailTypes = mailTypeRepository.findAll();
        assertThat(mailTypes).hasSize(databaseSizeBeforeUpdate);
        MailType testMailType = mailTypes.get(mailTypes.size() - 1);
        assertThat(testMailType.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testMailType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMailType.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteMailType() throws Exception {
        // Initialize the database
        mailTypeRepository.saveAndFlush(mailType);

		int databaseSizeBeforeDelete = mailTypeRepository.findAll().size();

        // Get the mailType
        restMailTypeMockMvc.perform(delete("/api/mailTypes/{id}", mailType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MailType> mailTypes = mailTypeRepository.findAll();
        assertThat(mailTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
