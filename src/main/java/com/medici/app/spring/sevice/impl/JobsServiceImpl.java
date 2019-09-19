package com.medici.app.spring.sevice.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medici.app.spring.model.Jobs;
import com.medici.app.spring.repository.JobsRepository;
import com.medici.app.spring.sevice.JobsService;

/**
 * Service Implementation for managing Jobs.
 */
@Service
@Transactional
public class JobsServiceImpl implements JobsService {

	private final Logger log = LoggerFactory.getLogger(JobsServiceImpl.class);

	private final JobsRepository jobsRepository;

	public JobsServiceImpl(JobsRepository jobsRepository) {
		this.jobsRepository = jobsRepository;
	}

	/**
	 * Save a jobs.
	 *
	 * @param jobsDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public Jobs save(Jobs jobs) {
		log.debug("Request to save Jobs : {}", jobs);
		return jobsRepository.save(jobs);
	}

	/**
	 * Get all the jobs.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Jobs> findAll(Pageable pageable) {
		log.debug("Request to get all Jobs");
		return jobsRepository.findAll(pageable);
	}

	/**
	 * Get one jobs by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<Jobs> findOne(Long id) {
		log.debug("Request to get Jobs : {}", id);
		return jobsRepository.findById(id);
	}

	/**
	 * Delete the jobs by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Jobs : {}", id);
		jobsRepository.deleteById(id);
	}
}
