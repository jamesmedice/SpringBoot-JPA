package com.medici.app.spring.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medici.app.spring.model.Monitor;
import com.medici.app.spring.rest.util.HeaderUtil;
import com.medici.app.spring.rest.util.PaginationUtil;
import com.medici.app.spring.sevice.MonitorService;

/**
 * REST controller for managing Monitor.
 */
@RestController
@RequestMapping("/api")
public class MonitorResource {

	private final Logger log = LoggerFactory.getLogger(MonitorResource.class);

	private static final String ENTITY_NAME = "monitor";

	private final MonitorService monitorService;

	public MonitorResource(MonitorService monitorService) {
		this.monitorService = monitorService;
	}

	/**
	 * POST /monitors : Create a new monitor.
	 *
	 * @param monitor
	 *            the monitor to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new monitor, or with status 400 (Bad Request) if the monitor
	 *         has already an ID
	 * @throws Exception
	 */
	@PostMapping("/monitors")

	public ResponseEntity<Monitor> createMonitor(@RequestBody Monitor monitor) throws Exception {
		log.debug("REST request to save Monitor : {}", monitor);
		if (monitor.getId() != null) {
			throw new Exception("BadRequestAlert: A new monitor cannot already have an ID" + ENTITY_NAME + "idexists");
		}
		Monitor result = monitorService.save(monitor);
		return ResponseEntity.created(new URI("/api/monitors/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /monitors : Updates an existing monitor.
	 *
	 * @param monitor
	 *            the monitor to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         monitor, or with status 400 (Bad Request) if the monitor is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         monitor couldn't be updated
	 * @throws Exception
	 */
	@PutMapping("/monitors")

	public ResponseEntity<Monitor> updateMonitor(@RequestBody Monitor monitor) throws Exception {
		log.debug("REST request to update Monitor : {}", monitor);
		if (monitor.getId() == null) {
			throw new Exception("BadRequestAlert: Invalid id" + ENTITY_NAME + "idnull");
		}
		Monitor result = monitorService.save(monitor);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, monitor.getId().toString())).body(result);
	}

	/**
	 * GET /monitors : get all the monitors.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of monitors
	 *         in body
	 */
	@GetMapping("/monitors")

	public ResponseEntity<List<Monitor>> getAllMonitors(Pageable pageable) {
		log.debug("REST request to get a page of Monitors");
		Page<Monitor> page = monitorService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/monitors");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /monitors/:id : get the "id" monitor.
	 *
	 * @param id
	 *            the id of the monitor to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         monitor, or with status 404 (Not Found)
	 */
	@GetMapping("/monitors/{id}")

	public ResponseEntity<?> getMonitor(@PathVariable Long id) {
		log.debug("REST request to get Monitor : {}", id);
		Optional<Monitor> monitor = monitorService.findOne(id);
		return new ResponseEntity<>(monitor, HttpStatus.OK);
	}

	/**
	 * DELETE /monitors/:id : delete the "id" monitor.
	 *
	 * @param id
	 *            the id of the monitor to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/monitors/{id}")

	public ResponseEntity<Void> deleteMonitor(@PathVariable Long id) {
		log.debug("REST request to delete Monitor : {}", id);
		monitorService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
