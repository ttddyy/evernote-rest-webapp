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
		String consumerKey = "evernote.consumerKey:test_consumer_key";
		String consumerSecret = "evernote.consumerSecret:test_consumer_secret";
		// disable jmx export for test to avoid InstanceAlreadyExistsException for multiple SpringBoot app contexts
		String disableJmx = "spring.jmx.enabled:false";

		EnvironmentTestUtils.addEnvironment(applicationContext, consumerKey, consumerSecret, disableJmx);
	}

}
