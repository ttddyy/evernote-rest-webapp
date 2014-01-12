package net.ttddyy.evernote.rest;

import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * ApplicationContextInitializer to set required system properties.
 *
 * @author Tadaya Tsuyukubo
 */
public class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		EnvironmentTestUtils.addEnviroment(environment, "evernote.consumerKey:test_consumer_key");
		EnvironmentTestUtils.addEnviroment(environment, "evernote.consumerSecret:test_consumer_secret");

		// disable jmx export for test to avoid InstanceAlreadyExistsException for multiple SpringBoot app contexts
		EnvironmentTestUtils.addEnviroment(environment, "spring.jmx.enabled:false");
	}

}
