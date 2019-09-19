package com.medici.app.spring.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${spring.datasource.username}")
	private String user;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.driver-class-name}")
	private String dataSourceClassName;

	
	@PostConstruct
	public void init() {
		System.out.println(user);
		System.out.println(password);
		System.out.println(dataSourceUrl);
		System.out.println(dataSourceClassName);
	}
	
	
}
