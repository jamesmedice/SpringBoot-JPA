package com.medici.app.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medici.app.spring.model.Monitor;

/**
 * Spring Data repository for the Monitor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {

}
