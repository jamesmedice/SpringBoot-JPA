package com.medici.app.gtw.service;

import com.medici.app.gtw.service.dto.JobsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Jobs.
 */
public interface JobsService {

    /**
     * Save a jobs.
     *
     * @param jobsDTO the entity to save
     * @return the persisted entity
     */
    JobsDTO save(JobsDTO jobsDTO);

    /**
     * Get all the jobs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<JobsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" jobs.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<JobsDTO> findOne(Long id);

    /**
     * Delete the "id" jobs.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
