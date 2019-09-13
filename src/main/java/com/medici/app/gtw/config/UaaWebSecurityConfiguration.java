package com.medici.app.gtw.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class UaaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public UaaWebSecurityConfiguration(UserDetailsService userDetailsService, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.userDetailsService = userDetailsService;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@PostConstruct
	public void init() throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
            .antMatchers("/h2-console/**");
        
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		return firewall;
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}
