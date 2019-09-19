package com.medici.app.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medici.app.spring.model.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
