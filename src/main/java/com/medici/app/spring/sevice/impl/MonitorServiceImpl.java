package com.medici.app.spring.sevice.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medici.app.spring.model.Monitor;
import com.medici.app.spring.repository.MonitorRepository;
import com.medici.app.spring.sevice.MonitorService;

/**
 * Service Implementation for managing Monitor.
 */
@Service
@Transactional
public class MonitorServiceImpl implements MonitorService {

	private final Logger log = LoggerFactory.getLogger(MonitorServiceImpl.class);

	private final MonitorRepository monitorRepository;

	public MonitorServiceImpl(MonitorRepository monitorRepository) {
		this.monitorRepository = monitorRepository;
	}

	/**
	 * Save a monitor.
	 *
	 * @param monitorDTO
	 *            the entity to save
	 * @return the persisted entity
	 */

	@Override
	public Monitor save(Monitor entity) {
		return monitorRepository.save(entity);
	}

	/**
	 * Get all the monitors.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Monitor> findAll(Pageable pageable) {
		log.debug("Request to get all Monitors");
		return monitorRepository.findAll(pageable);
	}

	/**
	 * Get one monitor by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<Monitor> findOne(Long id) {
		log.debug("Request to get Monitor : {}", id);
		return monitorRepository.findById(id);
	}

	/**
	 * Delete the monitor by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Monitor : {}", id);
		monitorRepository.deleteById(id);
	}

}
