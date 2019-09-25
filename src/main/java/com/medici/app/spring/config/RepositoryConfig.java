package com.medici.app.spring.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * @author a73s
 *
 */
@Configuration
@ComponentScan("com.medici.app.spring")
@EnableJpaRepositories("com.medici.app.spring.repository")
@EntityScan("com.medici.app.spring.model")
public class RepositoryConfig {

}
