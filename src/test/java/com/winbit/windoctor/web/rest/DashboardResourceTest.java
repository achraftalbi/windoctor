package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Dashboard;
import com.winbit.windoctor.repository.DashboardRepository;
import com.winbit.windoctor.repository.search.DashboardSearchRepository;

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
 * Test class for the DashboardResource REST controller.
 *
 * @see DashboardResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DashboardResourceTest {


    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;
    private static final String DEFAULT_DESCRIPTION_EN = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION_EN = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION_FR = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION_FR = "UPDATED_TEXT";

    @Inject
    private DashboardRepository dashboardRepository;

    @Inject
    private DashboardSearchRepository dashboardSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DashboardResource dashboardResource = new DashboardResource();
        ReflectionTestUtils.setField(dashboardResource, "dashboardRepository", dashboardRepository);
        ReflectionTestUtils.setField(dashboardResource, "dashboardSearchRepository", dashboardSearchRepository);
        this.restDashboardMockMvc = MockMvcBuilders.standaloneSetup(dashboardResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dashboard = new Dashboard();
        dashboard.setValue(DEFAULT_VALUE);
        dashboard.setDescriptionEn(DEFAULT_DESCRIPTION_EN);
        dashboard.setDescriptionFr(DEFAULT_DESCRIPTION_FR);
    }

    @Test
    @Transactional
    public void createDashboard() throws Exception {
        /*int databaseSizeBeforeCreate = dashboardRepository.findAll().size();

        // Create the Dashboard

        restDashboardMockMvc.perform(post("/api/dashboards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dashboard)))
                .andExpect(status().isCreated());

        // Validate the Dashboard in the database
        List<Dashboard> dashboards = dashboardRepository.findAll();
        assertThat(dashboards).hasSize(databaseSizeBeforeCreate + 1);
        Dashboard testDashboard = dashboards.get(dashboards.size() - 1);
        assertThat(testDashboard.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testDashboard.getDescriptionEn()).isEqualTo(DEFAULT_DESCRIPTION_EN);
        assertThat(testDashboard.getDescriptionFr()).isEqualTo(DEFAULT_DESCRIPTION_FR);*/
    }
/*
    @Test
    @Transactional
    public void getAllDashboards() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboards
        restDashboardMockMvc.perform(get("/api/dashboards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].descriptionEn").value(hasItem(DEFAULT_DESCRIPTION_EN.toString())))
                .andExpect(jsonPath("$.[*].descriptionFr").value(hasItem(DEFAULT_DESCRIPTION_FR.toString())));
    }

    @Test
    @Transactional
    public void getDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get the dashboard
        restDashboardMockMvc.perform(get("/api/dashboards/{id}", dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dashboard.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.descriptionEn").value(DEFAULT_DESCRIPTION_EN.toString()))
            .andExpect(jsonPath("$.descriptionFr").value(DEFAULT_DESCRIPTION_FR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get("/api/dashboards/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

		int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard
        dashboard.setValue(UPDATED_VALUE);
        dashboard.setDescriptionEn(UPDATED_DESCRIPTION_EN);
        dashboard.setDescriptionFr(UPDATED_DESCRIPTION_FR);


        restDashboardMockMvc.perform(put("/api/dashboards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dashboard)))
                .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboards = dashboardRepository.findAll();
        assertThat(dashboards).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboards.get(dashboards.size() - 1);
        assertThat(testDashboard.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDashboard.getDescriptionEn()).isEqualTo(UPDATED_DESCRIPTION_EN);
        assertThat(testDashboard.getDescriptionFr()).isEqualTo(UPDATED_DESCRIPTION_FR);
    }

    @Test
    @Transactional
    public void deleteDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

		int databaseSizeBeforeDelete = dashboardRepository.findAll().size();

        // Get the dashboard
        restDashboardMockMvc.perform(delete("/api/dashboards/{id}", dashboard.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dashboard> dashboards = dashboardRepository.findAll();
        assertThat(dashboards).hasSize(databaseSizeBeforeDelete - 1);
    }
    */
}
