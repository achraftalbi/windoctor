package com.winbit.windoctor.web.rest;

import com.winbit.windoctor.Application;
import com.winbit.windoctor.domain.Charge;
import com.winbit.windoctor.repository.ChargeRepository;
import com.winbit.windoctor.repository.search.ChargeSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ChargeResource REST controller.
 *
 * @see ChargeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChargeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(250000, "1");

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    @Inject
    private ChargeRepository chargeRepository;

    @Inject
    private ChargeSearchRepository chargeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restChargeMockMvc;

    private Charge charge;

    @PostConstruct
    public void setup() {
        /*MockitoAnnotations.initMocks(this);
        ChargeResource chargeResource = new ChargeResource();
        ReflectionTestUtils.setField(chargeResource, "chargeRepository", chargeRepository);
        ReflectionTestUtils.setField(chargeResource, "chargeSearchRepository", chargeSearchRepository);
        this.restChargeMockMvc = MockMvcBuilders.standaloneSetup(chargeResource).setMessageConverters(jacksonMessageConverter).build();*/
    }

    @Before
    public void initTest() {
        /*charge = new Charge();
        charge.setName(DEFAULT_NAME);
        charge.setImage(DEFAULT_IMAGE);
        charge.setPrice(DEFAULT_PRICE);
        charge.setAmount(DEFAULT_AMOUNT);*/
    }

    @Test
    @Transactional
    public void createCharge() throws Exception {
        /*int databaseSizeBeforeCreate = chargeRepository.findAll().size();

        // Create the Charge

        restChargeMockMvc.perform(post("/api/charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(charge)))
                .andExpect(status().isCreated());

        // Validate the Charge in the database
        List<Charge> charges = chargeRepository.findAll();
        assertThat(charges).hasSize(databaseSizeBeforeCreate + 1);
        Charge testCharge = charges.get(charges.size() - 1);
        assertThat(testCharge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharge.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCharge.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCharge.getAmount()).isEqualTo(DEFAULT_AMOUNT);*/
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setName(null);

        // Create the Charge, which fails.

        restChargeMockMvc.perform(post("/api/charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(charge)))
                .andExpect(status().isBadRequest());

        List<Charge> charges = chargeRepository.findAll();
        assertThat(charges).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setPrice(null);

        // Create the Charge, which fails.

        restChargeMockMvc.perform(post("/api/charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(charge)))
                .andExpect(status().isBadRequest());

        List<Charge> charges = chargeRepository.findAll();
        assertThat(charges).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        /*int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setAmount(null);

        // Create the Charge, which fails.

        restChargeMockMvc.perform(post("/api/charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(charge)))
                .andExpect(status().isBadRequest());

        List<Charge> charges = chargeRepository.findAll();
        assertThat(charges).hasSize(databaseSizeBeforeTest);*/
    }

    @Test
    @Transactional
    public void getAllCharges() throws Exception {
        // Initialize the database
        /*chargeRepository.saveAndFlush(charge);

        // Get all the charges
        restChargeMockMvc.perform(get("/api/charges"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(charge.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));*/
    }

    @Test
    @Transactional
    public void getCharge() throws Exception {
        // Initialize the database
        /*chargeRepository.saveAndFlush(charge);

        // Get the charge
        restChargeMockMvc.perform(get("/api/charges/{id}", charge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(charge.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));*/
    }

    @Test
    @Transactional
    public void getNonExistingCharge() throws Exception {
        // Get the charge
        /*restChargeMockMvc.perform(get("/api/charges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());*/
    }

    @Test
    @Transactional
    public void updateCharge() throws Exception {
        // Initialize the database
        /*chargeRepository.saveAndFlush(charge);

		int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Update the charge
        charge.setName(UPDATED_NAME);
        charge.setImage(UPDATED_IMAGE);
        charge.setPrice(UPDATED_PRICE);
        charge.setAmount(UPDATED_AMOUNT);


        restChargeMockMvc.perform(put("/api/charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(charge)))
                .andExpect(status().isOk());

        // Validate the Charge in the database
        List<Charge> charges = chargeRepository.findAll();
        assertThat(charges).hasSize(databaseSizeBeforeUpdate);
        Charge testCharge = charges.get(charges.size() - 1);
        assertThat(testCharge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharge.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCharge.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCharge.getAmount()).isEqualTo(UPDATED_AMOUNT);*/
    }

    @Test
    @Transactional
    public void deleteCharge() throws Exception {
        // Initialize the database
        /*chargeRepository.saveAndFlush(charge);

		int databaseSizeBeforeDelete = chargeRepository.findAll().size();

        // Get the charge
        restChargeMockMvc.perform(delete("/api/charges/{id}", charge.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Charge> charges = chargeRepository.findAll();
        assertThat(charges).hasSize(databaseSizeBeforeDelete - 1);*/
    }
}
