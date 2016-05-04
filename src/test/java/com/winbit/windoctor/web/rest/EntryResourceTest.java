package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Entry;
import com.winbit.windoctor.repository.EntryRepository;
import com.winbit.windoctor.repository.search.EntrySearchRepository;

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
 * Test class for the EntryResource REST controller.
 *
 * @see EntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EntryResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final DateTime DEFAULT_CREATION_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATION_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATION_DATE);

    private static final DateTime DEFAULT_RELATIVE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_RELATIVE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_RELATIVE_DATE_STR = dateTimeFormatter.print(DEFAULT_RELATIVE_DATE);

    @Inject
    private EntryRepository entryRepository;

    @Inject
    private EntrySearchRepository entrySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEntryMockMvc;

    private Entry entry;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        EntryResource entryResource = new EntryResource();
        ReflectionTestUtils.setField(entryResource, "entryRepository", entryRepository);
        ReflectionTestUtils.setField(entryResource, "entrySearchRepository", entrySearchRepository);
        this.restEntryMockMvc = MockMvcBuilders.standaloneSetup(entryResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        /*entry = new Entry();
        entry.setPrice(DEFAULT_PRICE);
        entry.setAmount(DEFAULT_AMOUNT);
        entry.setCreation_date(DEFAULT_CREATION_DATE);
        entry.setRelative_date(DEFAULT_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void createEntry() throws Exception {
        /*int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry

        restEntryMockMvc.perform(post("/api/entrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeCreate + 1);
        Entry testEntry = entrys.get(entrys.size() - 1);
        assertThat(testEntry.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testEntry.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testEntry.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testEntry.getRelative_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setPrice(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setAmount(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkCreation_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setCreation_date(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkRelative_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = entryRepository.findAll().size();
        // set the field null
        entry.setRelative_date(null);

        // Create the Entry, which fails.

        restEntryMockMvc.perform(post("/api/entrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isBadRequest());

        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllEntrys() throws Exception {
        // Initialize the database
        /*entryRepository.saveAndFlush(entry);

        // Get all the entrys
        restEntryMockMvc.perform(get("/api/entrys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].relative_date").value(hasItem(DEFAULT_RELATIVE_DATE_STR)));*/
    }

    @Test
    @Transactional
    public void getEntry() throws Exception {
        // Initialize the database
        /*entryRepository.saveAndFlush(entry);

        // Get the entry
        restEntryMockMvc.perform(get("/api/entrys/{id}", entry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entry.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.relative_date").value(DEFAULT_RELATIVE_DATE_STR));*/
    }

    @Test
    @Transactional
    public void getNonExistingEntry() throws Exception {
        // Get the entry
        /*restEntryMockMvc.perform(get("/api/entrys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updateEntry() throws Exception {
        // Initialize the database
        /*entryRepository.saveAndFlush(entry);

		int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Update the entry
        entry.setPrice(UPDATED_PRICE);
        entry.setAmount(UPDATED_AMOUNT);
        entry.setCreation_date(UPDATED_CREATION_DATE);
        entry.setRelative_date(UPDATED_RELATIVE_DATE);


        restEntryMockMvc.perform(put("/api/entrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entry)))
                .andExpect(status().isOk());

        // Validate the Entry in the database
        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeUpdate);
        Entry testEntry = entrys.get(entrys.size() - 1);
        assertThat(testEntry.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEntry.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testEntry.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testEntry.getRelative_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void deleteEntry() throws Exception {
        // Initialize the database
        /*entryRepository.saveAndFlush(entry);

		int databaseSizeBeforeDelete = entryRepository.findAll().size();

        // Get the entry
        restEntryMockMvc.perform(delete("/api/entrys/{id}", entry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Entry> entrys = entryRepository.findAll();
        assertThat(entrys).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
