package net.ttddyy.evernote.rest;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationContextInitializer to set required system properties.
 *
 * @author Tadaya Tsuyukubo
 */
public class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("evernote.consumerKey", "test_consumer_key");
		map.put("evernote.consumerSecret", "test_consumer_secret");

		// disable jmx export for test to avoid InstanceAlreadyExistsException for multiple SpringBoot app contexts
		map.put("endpoints.jmx.enabled", "false");

		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		MutablePropertySources propertySources = environment.getPropertySources();
		propertySources.addFirst(new MapPropertySource("override", map));
	}

}
