package com.medici.app.spring.sevice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.medici.app.spring.model.Monitor;
 

/**
 * Service Interface for managing Monitor.
 */
public interface MonitorService {

	/**
	 * Save a monitor.
	 *
	 * @param monitorDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	Monitor save(Monitor m);

	/**
	 * Get all the monitors.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	Page<Monitor> findAll(Pageable pageable);

	/**
	 * Get the "id" monitor.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	Optional<Monitor> findOne(Long id);

	/**
	 * Delete the "id" monitor.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(Long id);
}
