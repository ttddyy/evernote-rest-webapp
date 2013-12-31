package net.ttddyy.evernote.rest;

import com.evernote.auth.EvernoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.social.evernote.api.Evernote;
import org.springframework.social.evernote.api.impl.EvernoteTemplate;
import org.springframework.social.evernote.connect.EvernoteConnectionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Tadaya Tsuyukubo
 */
@ComponentScan
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
public class ApplicationConfig {

	@Autowired
	public EvernotePropertiesConfiguration evernotePropertiesConfiguration;


	@Configuration
	@ConfigurationProperties(name = "evernote")
	public static class EvernotePropertiesConfiguration {

		public String consumerKey;
		public String consumerSecret;
		public String accessToken;
		public boolean alwaysUseTokenFromProperties;
		public EvernoteService environment = EvernoteService.SANDBOX;  // default is sandbox

		public void setEnvironment(EvernoteService environment) {
			this.environment = environment;
		}

		public void setConsumerKey(String consumerKey) {
			this.consumerKey = consumerKey;
		}

		public void setConsumerSecret(String consumerSecret) {
			this.consumerSecret = consumerSecret;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public void setAlwaysUseTokenFromProperties(boolean alwaysUseTokenFromProperties) {
			this.alwaysUseTokenFromProperties = alwaysUseTokenFromProperties;
		}
	}


	@Bean
	public EvernoteConnectionFactory evernoteConnectionFactory() {
		final String consumerKey = this.evernotePropertiesConfiguration.consumerKey;
		final String consumerSecret = this.evernotePropertiesConfiguration.consumerSecret;
		final EvernoteService evernoteService = this.evernotePropertiesConfiguration.environment;
		return new EvernoteConnectionFactory(consumerKey, consumerSecret, evernoteService);
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
	public Evernote evernote(WebRequest request) {
		final EvernotePropertiesConfiguration config = this.evernotePropertiesConfiguration;
		final EvernoteService evernoteService = config.environment;

		String accessToken = config.accessToken;
		if (!config.alwaysUseTokenFromProperties) {
			// override access token from request
			final String headerAccessToken = request.getHeader("evernote-rest-accesstoken");
			if (headerAccessToken != null) {
				accessToken = headerAccessToken;
			}
		}

		final Evernote evernote = new EvernoteTemplate(evernoteService, accessToken);
		evernote.setApplyNullSafe(false);
		return evernote;
	}

}
