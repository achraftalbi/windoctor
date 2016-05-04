package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Purchase;
import com.winbit.windoctor.repository.PurchaseRepository;
import com.winbit.windoctor.repository.search.PurchaseSearchRepository;

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
 * Test class for the PurchaseResource REST controller.
 *
 * @see PurchaseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PurchaseResourceTest {

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
    private PurchaseRepository purchaseRepository;

    @Inject
    private PurchaseSearchRepository purchaseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        PurchaseResource purchaseResource = new PurchaseResource();
        ReflectionTestUtils.setField(purchaseResource, "purchaseRepository", purchaseRepository);
        ReflectionTestUtils.setField(purchaseResource, "purchaseSearchRepository", purchaseSearchRepository);
        this.restPurchaseMockMvc = MockMvcBuilders.standaloneSetup(purchaseResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        /*purchase = new Purchase();
        purchase.setPrice(DEFAULT_PRICE);
        purchase.setAmount(DEFAULT_AMOUNT);
        purchase.setCreation_date(DEFAULT_CREATION_DATE);
        purchase.setRelative_date(DEFAULT_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void createPurchase() throws Exception {
        /*int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isCreated());

        // Validate the Purchase in the database
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeCreate + 1);
        Purchase testPurchase = purchases.get(purchases.size() - 1);
        assertThat(testPurchase.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPurchase.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPurchase.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPurchase.getRelative_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setPrice(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isBadRequest());

        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setAmount(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isBadRequest());

        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkCreation_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setCreation_date(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isBadRequest());

        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkRelative_dateIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setRelative_date(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isBadRequest());

        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllPurchases() throws Exception {
        // Initialize the database
        /*purchaseRepository.saveAndFlush(purchase);

        // Get all the purchases
        restPurchaseMockMvc.perform(get("/api/purchases"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].relative_date").value(hasItem(DEFAULT_RELATIVE_DATE_STR)));*/
    }

    @Test
    @Transactional
    public void getPurchase() throws Exception {
        // Initialize the database
        /*purchaseRepository.saveAndFlush(purchase);

        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(purchase.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.relative_date").value(DEFAULT_RELATIVE_DATE_STR));*/
    }

    @Test
    @Transactional
    public void getNonExistingPurchase() throws Exception {
        // Get the purchase
        /*restPurchaseMockMvc.perform(get("/api/purchases/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updatePurchase() throws Exception {
        // Initialize the database
        /*purchaseRepository.saveAndFlush(purchase);

		int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Update the purchase
        purchase.setPrice(UPDATED_PRICE);
        purchase.setAmount(UPDATED_AMOUNT);
        purchase.setCreation_date(UPDATED_CREATION_DATE);
        purchase.setRelative_date(UPDATED_RELATIVE_DATE);


        restPurchaseMockMvc.perform(put("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isOk());

        // Validate the Purchase in the database
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeUpdate);
        Purchase testPurchase = purchases.get(purchases.size() - 1);
        assertThat(testPurchase.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPurchase.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPurchase.getCreation_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPurchase.getRelative_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RELATIVE_DATE);*/
    }

    @Test
    @Transactional
    public void deletePurchase() throws Exception {
        // Initialize the database
        /*purchaseRepository.saveAndFlush(purchase);

		int databaseSizeBeforeDelete = purchaseRepository.findAll().size();

        // Get the purchase
        restPurchaseMockMvc.perform(delete("/api/purchases/{id}", purchase.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
