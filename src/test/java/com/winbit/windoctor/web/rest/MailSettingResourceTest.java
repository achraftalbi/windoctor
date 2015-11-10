package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.MailSetting;
import com.winbit.windoctor.repository.MailSettingRepository;
import com.winbit.windoctor.repository.search.MailSettingSearchRepository;

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
 * Test class for the MailSettingResource REST controller.
 *
 * @see MailSettingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailSettingResourceTest {


    private static final Boolean DEFAULT_MAIL_ON_EVENT_CREATION = false;
    private static final Boolean UPDATED_MAIL_ON_EVENT_CREATION = true;

    private static final Boolean DEFAULT_MAIL_ON_EVENT_CANCELATION = false;
    private static final Boolean UPDATED_MAIL_ON_EVENT_CANCELATION = true;

    private static final Boolean DEFAULT_MAIL_ON_EVENT_EDITION = false;
    private static final Boolean UPDATED_MAIL_ON_EVENT_EDITION = true;

    private static final Boolean DEFAULT_REMAIDING_BEFORE_EVENT_MAIL = false;
    private static final Boolean UPDATED_REMAIDING_BEFORE_EVENT_MAIL = true;

    private static final Boolean DEFAULT_REMAIDING_AFTER_EVENT_MAIL = false;
    private static final Boolean UPDATED_REMAIDING_AFTER_EVENT_MAIL = true;

    private static final Boolean DEFAULT_PATIENT_CREATION_ACCOUNT_MAIL = false;
    private static final Boolean UPDATED_PATIENT_CREATION_ACCOUNT_MAIL = true;

    @Inject
    private MailSettingRepository mailSettingRepository;

    @Inject
    private MailSettingSearchRepository mailSettingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMailSettingMockMvc;

    private MailSetting mailSetting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MailSettingResource mailSettingResource = new MailSettingResource();
        ReflectionTestUtils.setField(mailSettingResource, "mailSettingRepository", mailSettingRepository);
        ReflectionTestUtils.setField(mailSettingResource, "mailSettingSearchRepository", mailSettingSearchRepository);
        this.restMailSettingMockMvc = MockMvcBuilders.standaloneSetup(mailSettingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mailSetting = new MailSetting();
        mailSetting.setMailOnEventCreation(DEFAULT_MAIL_ON_EVENT_CREATION);
        mailSetting.setMailOnEventCancelation(DEFAULT_MAIL_ON_EVENT_CANCELATION);
        mailSetting.setMailOnEventEdition(DEFAULT_MAIL_ON_EVENT_EDITION);
        mailSetting.setRemaidingBeforeEventMail(DEFAULT_REMAIDING_BEFORE_EVENT_MAIL);
        mailSetting.setRemaidingAfterEventMail(DEFAULT_REMAIDING_AFTER_EVENT_MAIL);
        mailSetting.setPatientCreationAccountMail(DEFAULT_PATIENT_CREATION_ACCOUNT_MAIL);
    }

    @Test
    @Transactional
    public void createMailSetting() throws Exception {
        int databaseSizeBeforeCreate = mailSettingRepository.findAll().size();

        // Create the MailSetting

        restMailSettingMockMvc.perform(post("/api/mailSettings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailSetting)))
                .andExpect(status().isCreated());

        // Validate the MailSetting in the database
        List<MailSetting> mailSettings = mailSettingRepository.findAll();
        assertThat(mailSettings).hasSize(databaseSizeBeforeCreate + 1);
        MailSetting testMailSetting = mailSettings.get(mailSettings.size() - 1);
        assertThat(testMailSetting.getMailOnEventCreation()).isEqualTo(DEFAULT_MAIL_ON_EVENT_CREATION);
        assertThat(testMailSetting.getMailOnEventCancelation()).isEqualTo(DEFAULT_MAIL_ON_EVENT_CANCELATION);
        assertThat(testMailSetting.getMailOnEventEdition()).isEqualTo(DEFAULT_MAIL_ON_EVENT_EDITION);
        assertThat(testMailSetting.getRemaidingBeforeEventMail()).isEqualTo(DEFAULT_REMAIDING_BEFORE_EVENT_MAIL);
        assertThat(testMailSetting.getRemaidingAfterEventMail()).isEqualTo(DEFAULT_REMAIDING_AFTER_EVENT_MAIL);
        assertThat(testMailSetting.getPatientCreationAccountMail()).isEqualTo(DEFAULT_PATIENT_CREATION_ACCOUNT_MAIL);
    }

    @Test
    @Transactional
    public void getAllMailSettings() throws Exception {
        // Initialize the database
        mailSettingRepository.saveAndFlush(mailSetting);

        // Get all the mailSettings
        restMailSettingMockMvc.perform(get("/api/mailSettings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mailSetting.getId().intValue())))
                .andExpect(jsonPath("$.[*].mailOnEventCreation").value(hasItem(DEFAULT_MAIL_ON_EVENT_CREATION.booleanValue())))
                .andExpect(jsonPath("$.[*].mailOnEventCancelation").value(hasItem(DEFAULT_MAIL_ON_EVENT_CANCELATION.booleanValue())))
                .andExpect(jsonPath("$.[*].mailOnEventEdition").value(hasItem(DEFAULT_MAIL_ON_EVENT_EDITION.booleanValue())))
                .andExpect(jsonPath("$.[*].remaidingBeforeEventMail").value(hasItem(DEFAULT_REMAIDING_BEFORE_EVENT_MAIL.booleanValue())))
                .andExpect(jsonPath("$.[*].remaidingAfterEventMail").value(hasItem(DEFAULT_REMAIDING_AFTER_EVENT_MAIL.booleanValue())))
                .andExpect(jsonPath("$.[*].patientCreationAccountMail").value(hasItem(DEFAULT_PATIENT_CREATION_ACCOUNT_MAIL.booleanValue())));
    }

    @Test
    @Transactional
    public void getMailSetting() throws Exception {
        // Initialize the database
        mailSettingRepository.saveAndFlush(mailSetting);

        // Get the mailSetting
        restMailSettingMockMvc.perform(get("/api/mailSettings/{id}", mailSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mailSetting.getId().intValue()))
            .andExpect(jsonPath("$.mailOnEventCreation").value(DEFAULT_MAIL_ON_EVENT_CREATION.booleanValue()))
            .andExpect(jsonPath("$.mailOnEventCancelation").value(DEFAULT_MAIL_ON_EVENT_CANCELATION.booleanValue()))
            .andExpect(jsonPath("$.mailOnEventEdition").value(DEFAULT_MAIL_ON_EVENT_EDITION.booleanValue()))
            .andExpect(jsonPath("$.remaidingBeforeEventMail").value(DEFAULT_REMAIDING_BEFORE_EVENT_MAIL.booleanValue()))
            .andExpect(jsonPath("$.remaidingAfterEventMail").value(DEFAULT_REMAIDING_AFTER_EVENT_MAIL.booleanValue()))
            .andExpect(jsonPath("$.patientCreationAccountMail").value(DEFAULT_PATIENT_CREATION_ACCOUNT_MAIL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMailSetting() throws Exception {
        // Get the mailSetting
        restMailSettingMockMvc.perform(get("/api/mailSettings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailSetting() throws Exception {
        // Initialize the database
        mailSettingRepository.saveAndFlush(mailSetting);

		int databaseSizeBeforeUpdate = mailSettingRepository.findAll().size();

        // Update the mailSetting
        mailSetting.setMailOnEventCreation(UPDATED_MAIL_ON_EVENT_CREATION);
        mailSetting.setMailOnEventCancelation(UPDATED_MAIL_ON_EVENT_CANCELATION);
        mailSetting.setMailOnEventEdition(UPDATED_MAIL_ON_EVENT_EDITION);
        mailSetting.setRemaidingBeforeEventMail(UPDATED_REMAIDING_BEFORE_EVENT_MAIL);
        mailSetting.setRemaidingAfterEventMail(UPDATED_REMAIDING_AFTER_EVENT_MAIL);
        mailSetting.setPatientCreationAccountMail(UPDATED_PATIENT_CREATION_ACCOUNT_MAIL);

        restMailSettingMockMvc.perform(put("/api/mailSettings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailSetting)))
                .andExpect(status().isOk());

        // Validate the MailSetting in the database
        List<MailSetting> mailSettings = mailSettingRepository.findAll();
        assertThat(mailSettings).hasSize(databaseSizeBeforeUpdate);
        MailSetting testMailSetting = mailSettings.get(mailSettings.size() - 1);
        assertThat(testMailSetting.getMailOnEventCreation()).isEqualTo(UPDATED_MAIL_ON_EVENT_CREATION);
        assertThat(testMailSetting.getMailOnEventCancelation()).isEqualTo(UPDATED_MAIL_ON_EVENT_CANCELATION);
        assertThat(testMailSetting.getMailOnEventEdition()).isEqualTo(UPDATED_MAIL_ON_EVENT_EDITION);
        assertThat(testMailSetting.getRemaidingBeforeEventMail()).isEqualTo(UPDATED_REMAIDING_BEFORE_EVENT_MAIL);
        assertThat(testMailSetting.getRemaidingAfterEventMail()).isEqualTo(UPDATED_REMAIDING_AFTER_EVENT_MAIL);
        assertThat(testMailSetting.getPatientCreationAccountMail()).isEqualTo(UPDATED_PATIENT_CREATION_ACCOUNT_MAIL);
    }

    @Test
    @Transactional
    public void deleteMailSetting() throws Exception {
        // Initialize the database
        mailSettingRepository.saveAndFlush(mailSetting);

		int databaseSizeBeforeDelete = mailSettingRepository.findAll().size();

        // Get the mailSetting
        restMailSettingMockMvc.perform(delete("/api/mailSettings/{id}", mailSetting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MailSetting> mailSettings = mailSettingRepository.findAll();
        assertThat(mailSettings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
