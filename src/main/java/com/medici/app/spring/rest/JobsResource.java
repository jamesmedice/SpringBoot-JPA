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

import com.medici.app.spring.model.Jobs;
import com.medici.app.spring.rest.util.HeaderUtil;
import com.medici.app.spring.rest.util.PaginationUtil;
import com.medici.app.spring.sevice.JobsService;

/**
 * REST controller for managing Jobs.
 */
@RestController
@RequestMapping("/api")
public class JobsResource {

	private final Logger log = LoggerFactory.getLogger(JobsResource.class);

	private static final String ENTITY_NAME = "jobs";

	private final JobsService jobsService;

	public JobsResource(JobsService jobsService) {
		this.jobsService = jobsService;
	}

	/**
	 * POST /jobs : Create a new jobs.
	 *
	 * @param jobs
	 *            the jobs to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new jobs, or with status 400 (Bad Request) if the jobs has
	 *         already an ID
	 * @throws Exception
	 */
	@PostMapping("/jobs")

	public ResponseEntity<Jobs> createJobs(@RequestBody Jobs jobs) throws Exception {
		log.debug("REST request to save Jobs : {}", jobs);
		if (jobs.getId() != null) {
			throw new Exception("BadRequestAlert: A new jobs cannot already have an ID" + ENTITY_NAME + "idexists");
		}
		Jobs result = jobsService.save(jobs);
		return ResponseEntity.created(new URI("/api/jobs/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /jobs : Updates an existing jobs.
	 *
	 * @param jobs
	 *            the jobs to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         jobs, or with status 400 (Bad Request) if the jobs is not valid,
	 *         or with status 500 (Internal Server Error) if the jobs couldn't
	 *         be updated
	 * @throws Exception
	 */
	@PutMapping("/jobs")

	public ResponseEntity<Jobs> updateJobs(@RequestBody Jobs jobs) throws Exception {
		log.debug("REST request to update Jobs : {}", jobs);
		if (jobs.getId() == null) {
			throw new Exception("BadRequestAlert: Invalid id" + ENTITY_NAME + "idnull");
		}
		Jobs result = jobsService.save(jobs);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobs.getId().toString())).body(result);
	}

	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in
	 *         body
	 */
	@GetMapping("/jobs")

	public ResponseEntity<List<Jobs>> getAllJobs(Pageable pageable) {
		log.debug("REST request to get a page of Jobs");
		Page<Jobs> page = jobsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /jobs/:id : get the "id" jobs.
	 *
	 * @param id
	 *            the id of the jobs to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the jobs,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/jobs/{id}")

	public ResponseEntity<?> getJobs(@PathVariable Long id) {
		log.debug("REST request to get Jobs : {}", id);
		Optional<Jobs> jobs = jobsService.findOne(id);
		return new ResponseEntity<>(jobs, HttpStatus.OK);
	}

	/**
	 * DELETE /jobs/:id : delete the "id" jobs.
	 *
	 * @param id
	 *            the id of the jobs to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/jobs/{id}")

	public ResponseEntity<Void> deleteJobs(@PathVariable Long id) {
		log.debug("REST request to delete Jobs : {}", id);
		jobsService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
