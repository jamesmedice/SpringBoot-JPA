package com.medici.app.gtw.web.rest;

import com.medici.app.gtw.MarketGatewayApp;

import com.medici.app.gtw.config.SecurityBeanOverrideConfiguration;

import com.medici.app.gtw.domain.Jobs;
import com.medici.app.gtw.repository.JobsRepository;
import com.medici.app.gtw.service.JobsService;
import com.medici.app.gtw.service.dto.JobsDTO;
import com.medici.app.gtw.service.mapper.JobsMapper;
import com.medici.app.gtw.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.medici.app.gtw.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobsResource REST controller.
 *
 * @see JobsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarketGatewayApp.class)
public class JobsResourceIntTest {

    private static final Long DEFAULT_JOB_ID = 1L;
    private static final Long UPDATED_JOB_ID = 2L;

    private static final String DEFAULT_JOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REGULAR_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_REGULAR_EXPRESSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENABLE = 1;
    private static final Integer UPDATED_ENABLE = 2;

    @Autowired
    private JobsRepository jobsRepository;


    @Autowired
    private JobsMapper jobsMapper;
    

    @Autowired
    private JobsService jobsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobsMockMvc;

    private Jobs jobs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobsResource jobsResource = new JobsResource(jobsService);
        this.restJobsMockMvc = MockMvcBuilders.standaloneSetup(jobsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jobs createEntity(EntityManager em) {
        Jobs jobs = new Jobs()
            .jobId(DEFAULT_JOB_ID)
            .jobName(DEFAULT_JOB_NAME)
            .regularExpression(DEFAULT_REGULAR_EXPRESSION)
            .enable(DEFAULT_ENABLE);
        return jobs;
    }

    @Before
    public void initTest() {
        jobs = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobs() throws Exception {
        int databaseSizeBeforeCreate = jobsRepository.findAll().size();

        // Create the Jobs
        JobsDTO jobsDTO = jobsMapper.toDto(jobs);
        restJobsMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobsDTO)))
            .andExpect(status().isCreated());

        // Validate the Jobs in the database
        List<Jobs> jobsList = jobsRepository.findAll();
        assertThat(jobsList).hasSize(databaseSizeBeforeCreate + 1);
        Jobs testJobs = jobsList.get(jobsList.size() - 1);
        assertThat(testJobs.getJobId()).isEqualTo(DEFAULT_JOB_ID);
        assertThat(testJobs.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
        assertThat(testJobs.getRegularExpression()).isEqualTo(DEFAULT_REGULAR_EXPRESSION);
        assertThat(testJobs.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    public void createJobsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobsRepository.findAll().size();

        // Create the Jobs with an existing ID
        jobs.setId(1L);
        JobsDTO jobsDTO = jobsMapper.toDto(jobs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobsMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Jobs in the database
        List<Jobs> jobsList = jobsRepository.findAll();
        assertThat(jobsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList
        restJobsMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobs.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].regularExpression").value(hasItem(DEFAULT_REGULAR_EXPRESSION.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE)));
    }
    

    @Test
    @Transactional
    public void getJobs() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get the jobs
        restJobsMockMvc.perform(get("/api/jobs/{id}", jobs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobs.getId().intValue()))
            .andExpect(jsonPath("$.jobId").value(DEFAULT_JOB_ID.intValue()))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME.toString()))
            .andExpect(jsonPath("$.regularExpression").value(DEFAULT_REGULAR_EXPRESSION.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE));
    }
    @Test
    @Transactional
    public void getNonExistingJobs() throws Exception {
        // Get the jobs
        restJobsMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobs() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        int databaseSizeBeforeUpdate = jobsRepository.findAll().size();

        // Update the jobs
        Jobs updatedJobs = jobsRepository.findById(jobs.getId()).get();
        // Disconnect from session so that the updates on updatedJobs are not directly saved in db
        em.detach(updatedJobs);
        updatedJobs
            .jobId(UPDATED_JOB_ID)
            .jobName(UPDATED_JOB_NAME)
            .regularExpression(UPDATED_REGULAR_EXPRESSION)
            .enable(UPDATED_ENABLE);
        JobsDTO jobsDTO = jobsMapper.toDto(updatedJobs);

        restJobsMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobsDTO)))
            .andExpect(status().isOk());

        // Validate the Jobs in the database
        List<Jobs> jobsList = jobsRepository.findAll();
        assertThat(jobsList).hasSize(databaseSizeBeforeUpdate);
        Jobs testJobs = jobsList.get(jobsList.size() - 1);
        assertThat(testJobs.getJobId()).isEqualTo(UPDATED_JOB_ID);
        assertThat(testJobs.getJobName()).isEqualTo(UPDATED_JOB_NAME);
        assertThat(testJobs.getRegularExpression()).isEqualTo(UPDATED_REGULAR_EXPRESSION);
        assertThat(testJobs.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    public void updateNonExistingJobs() throws Exception {
        int databaseSizeBeforeUpdate = jobsRepository.findAll().size();

        // Create the Jobs
        JobsDTO jobsDTO = jobsMapper.toDto(jobs);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobsMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Jobs in the database
        List<Jobs> jobsList = jobsRepository.findAll();
        assertThat(jobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobs() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        int databaseSizeBeforeDelete = jobsRepository.findAll().size();

        // Get the jobs
        restJobsMockMvc.perform(delete("/api/jobs/{id}", jobs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Jobs> jobsList = jobsRepository.findAll();
        assertThat(jobsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jobs.class);
        Jobs jobs1 = new Jobs();
        jobs1.setId(1L);
        Jobs jobs2 = new Jobs();
        jobs2.setId(jobs1.getId());
        assertThat(jobs1).isEqualTo(jobs2);
        jobs2.setId(2L);
        assertThat(jobs1).isNotEqualTo(jobs2);
        jobs1.setId(null);
        assertThat(jobs1).isNotEqualTo(jobs2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobsDTO.class);
        JobsDTO jobsDTO1 = new JobsDTO();
        jobsDTO1.setId(1L);
        JobsDTO jobsDTO2 = new JobsDTO();
        assertThat(jobsDTO1).isNotEqualTo(jobsDTO2);
        jobsDTO2.setId(jobsDTO1.getId());
        assertThat(jobsDTO1).isEqualTo(jobsDTO2);
        jobsDTO2.setId(2L);
        assertThat(jobsDTO1).isNotEqualTo(jobsDTO2);
        jobsDTO1.setId(null);
        assertThat(jobsDTO1).isNotEqualTo(jobsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobsMapper.fromId(null)).isNull();
    }
}
