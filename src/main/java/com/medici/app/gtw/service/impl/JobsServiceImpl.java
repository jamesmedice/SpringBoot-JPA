package com.medici.app.gtw.service.impl;

import com.medici.app.gtw.service.JobsService;
import com.medici.app.gtw.domain.Jobs;
import com.medici.app.gtw.repository.JobsRepository;
import com.medici.app.gtw.service.dto.JobsDTO;
import com.medici.app.gtw.service.mapper.JobsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Jobs.
 */
@Service
@Transactional
public class JobsServiceImpl implements JobsService {

    private final Logger log = LoggerFactory.getLogger(JobsServiceImpl.class);

    private final JobsRepository jobsRepository;

    private final JobsMapper jobsMapper;

    public JobsServiceImpl(JobsRepository jobsRepository, JobsMapper jobsMapper) {
        this.jobsRepository = jobsRepository;
        this.jobsMapper = jobsMapper;
    }

    /**
     * Save a jobs.
     *
     * @param jobsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobsDTO save(JobsDTO jobsDTO) {
        log.debug("Request to save Jobs : {}", jobsDTO);
        Jobs jobs = jobsMapper.toEntity(jobsDTO);
        jobs = jobsRepository.save(jobs);
        return jobsMapper.toDto(jobs);
    }

    /**
     * Get all the jobs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        return jobsRepository.findAll(pageable)
            .map(jobsMapper::toDto);
    }


    /**
     * Get one jobs by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<JobsDTO> findOne(Long id) {
        log.debug("Request to get Jobs : {}", id);
        return jobsRepository.findById(id)
            .map(jobsMapper::toDto);
    }

    /**
     * Delete the jobs by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Jobs : {}", id);
        jobsRepository.deleteById(id);
    }
}
