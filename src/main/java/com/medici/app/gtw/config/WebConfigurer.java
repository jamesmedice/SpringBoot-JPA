package com.medici.app.gtw.config;

import java.nio.charset.StandardCharsets;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;

import io.github.jhipster.config.JHipsterProperties;
import io.undertow.UndertowOptions;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
@EnableWebMvc
public class WebConfigurer extends WebMvcConfigurationSupport implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

	private final Environment env;

	private final JHipsterProperties jHipsterProperties;

	private MetricRegistry metricRegistry;

	public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties) {

		this.env = env;
		this.jHipsterProperties = jHipsterProperties;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		if (env.getActiveProfiles().length != 0) {
			log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
		}
		EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
		initMetrics(servletContext, disps);
		log.info("Web application fully configured");
	}

	/**
	 * Customize the Servlet engine: Mime types, the document root, the cache.
	 */
	@Override
	public void customize(WebServerFactory server) {
		setMimeMappings(server);

		if (jHipsterProperties.getHttp().getVersion().equals(JHipsterProperties.Http.Version.V_2_0) && server instanceof UndertowServletWebServerFactory) {

			((UndertowServletWebServerFactory) server).addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
		}
	}

	private void setMimeMappings(WebServerFactory server) {
		if (server instanceof ConfigurableServletWebServerFactory) {
			MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
			// IE issue, see
			// https://github.com/jhipster/generator-jhipster/pull/711
			mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
			// CloudFoundry issue, see
			// https://github.com/cloudfoundry/gorouter/issues/64
			mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
			ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
			servletWebServer.setMimeMappings(mappings);
		}
	}

	/**
	 * Initializes Metrics.
	 */
	private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
		log.debug("Initializing Metrics registries");
		servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
		servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);

		log.debug("Registering Metrics Filter");
		FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());

		metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
		metricsFilter.setAsyncSupported(true);

		log.debug("Registering Metrics Servlet");
		ServletRegistration.Dynamic metricsAdminServlet = servletContext.addServlet("metricsServlet", new MetricsServlet());

		metricsAdminServlet.addMapping("/management/metrics/*");
		metricsAdminServlet.setAsyncSupported(true);
		metricsAdminServlet.setLoadOnStartup(2);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = jHipsterProperties.getCors();
		if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
			log.debug("Registering CORS filter");
			source.registerCorsConfiguration("/api/**", config);
			source.registerCorsConfiguration("/management/**", config);
			source.registerCorsConfiguration("/v2/api-docs", config);
		}
		return new CorsFilter(source);
	}

	@Autowired(required = false)
	public void setMetricRegistry(MetricRegistry metricRegistry) {
		this.metricRegistry = metricRegistry;
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(Constants.SWAGGER_UI_HTML).addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
