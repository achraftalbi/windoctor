package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Plan;
import com.winbit.windoctor.repository.PlanRepository;
import com.winbit.windoctor.repository.search.PlanSearchRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PlanResource REST controller.
 *
 * @see PlanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlanResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_NUMBER = new BigDecimal(1);
    private static final BigDecimal UPDATED_NUMBER = new BigDecimal(2);

    private static final Boolean DEFAULT_ARCHIVE = false;
    private static final Boolean UPDATED_ARCHIVE = true;

    private static final DateTime DEFAULT_CREATION_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATION_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATION_DATE);

    @Inject
    private PlanRepository planRepository;

    @Inject
    private PlanSearchRepository planSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPlanMockMvc;

    private Plan plan;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        PlanResource planResource = new PlanResource();
        ReflectionTestUtils.setField(planResource, "planRepository", planRepository);
        ReflectionTestUtils.setField(planResource, "planSearchRepository", planSearchRepository);
        this.restPlanMockMvc = MockMvcBuilders.standaloneSetup(planResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        plan = new Plan();
        plan.setName(DEFAULT_NAME);
        plan.setNumber(DEFAULT_NUMBER);
        plan.setArchive(DEFAULT_ARCHIVE);
        plan.setCreation_date(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createPlan() throws Exception {
        /*int databaseSizeBeforeCreate = planRepository.findAll().size();

        // Create the Plan

        restPlanMockMvc.perform(post("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isCreated());

        // Validate the Plan in the database
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeCreate + 1);
        Plan testPlan = plans.get(plans.size() - 1);
        assertThat(testPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlan.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPlan.getArchive()).isEqualTo(DEFAULT_ARCHIVE);
        assertThat(testPlan.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATION_DATE);*/
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setNumber(null);

        // Create the Plan, which fails.

        restPlanMockMvc.perform(post("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isBadRequest());

        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkCreation_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setCreation_date(null);

        // Create the Plan, which fails.

        restPlanMockMvc.perform(post("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isBadRequest());

        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllPlans() throws Exception {
        // Initialize the database
        /*planRepository.saveAndFlush(plan);

        // Get all the plans
        restPlanMockMvc.perform(get("/api/plans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
                .andExpect(jsonPath("$.[*].archive").value(hasItem(DEFAULT_ARCHIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE_STR)));*/
    }

    @Test
    @Transactional
    public void getPlan() throws Exception {
        // Initialize the database
        /*planRepository.saveAndFlush(plan);

        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", plan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(plan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.archive").value(DEFAULT_ARCHIVE.booleanValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE_STR));*/
    }

    @Test
    @Transactional
    public void getNonExistingPlan() throws Exception {
        // Get the plan
        /*restPlanMockMvc.perform(get("/api/plans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updatePlan() throws Exception {
        // Initialize the database
        /*planRepository.saveAndFlush(plan);

		int databaseSizeBeforeUpdate = planRepository.findAll().size();

        // Update the plan
        plan.setName(UPDATED_NAME);
        plan.setNumber(UPDATED_NUMBER);
        plan.setArchive(UPDATED_ARCHIVE);
        plan.setCreation_date(UPDATED_CREATION_DATE);
        

        restPlanMockMvc.perform(put("/api/plans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plan)))
                .andExpect(status().isOk());

        // Validate the Plan in the database
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeUpdate);
        Plan testPlan = plans.get(plans.size() - 1);
        assertThat(testPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlan.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPlan.getArchive()).isEqualTo(UPDATED_ARCHIVE);
        assertThat(testPlan.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATION_DATE);*/
    }

    @Test
    @Transactional
    public void deletePlan() throws Exception {
        // Initialize the database
        /*planRepository.saveAndFlush(plan);

		int databaseSizeBeforeDelete = planRepository.findAll().size();

        // Get the plan
        restPlanMockMvc.perform(delete("/api/plans/{id}", plan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Plan> plans = planRepository.findAll();
        assertThat(plans).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
