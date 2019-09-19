package com.medici.app.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medici.app.spring.model.Jobs;


/**
 * Spring Data  repository for the Jobs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobsRepository extends JpaRepository<Jobs, Long> {

}
