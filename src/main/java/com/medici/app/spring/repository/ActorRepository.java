package com.medici.app.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.medici.app.spring.model.Actor;

/**
 * 
 * @author a73s
 *
 */
@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {

	@Query("SELECT req FROM Actor req WHERE req.first_name LIKE %:firstName%")
	List<Actor> findByFirstName(@Param("firstName") String firstName);

	@Query("SELECT req FROM Actor req WHERE req.last_name LIKE %:lastName%")
	List<Actor> findByLastName(@Param("lastName") String lastName);

}
