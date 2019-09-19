package com.medici.app.spring.sevice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.medici.app.spring.model.Jobs;
 

/**
 * Service Interface for managing Jobs.
 */
public interface JobsService {

	/**
	 * Save a jobs.
	 *
	 * @param jobsDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	Jobs save(Jobs jobsDTO);

	/**
	 * Get all the jobs.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	Page<Jobs> findAll(Pageable pageable);

	/**
	 * Get the "id" jobs.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	Optional<Jobs> findOne(Long id);

	/**
	 * Delete the "id" jobs.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(Long id);
}
