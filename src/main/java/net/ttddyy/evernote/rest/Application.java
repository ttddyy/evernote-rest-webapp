package net.ttddyy.evernote.rest;

import com.evernote.auth.EvernoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.social.evernote.api.Evernote;
import org.springframework.social.evernote.api.impl.EvernoteTemplate;
import org.springframework.social.evernote.connect.EvernoteConnectionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Evernote Rest Webapp application and configuration.
 *
 * @author Tadaya Tsuyukubo
 */
@ComponentScan
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
public class Application {

	@Autowired
	public EvernotePropertiesConfiguration evernotePropertiesConfiguration;


	@Configuration
	@ConfigurationProperties("evernote")
	public static class EvernotePropertiesConfiguration {

		@NotNull
		public String consumerKey;
		@NotNull
		public String consumerSecret;

		public String accessToken;
		public boolean alwaysUseTokenFromConfig;
		public boolean fallbackToTokenFromConfig;
		public EvernoteService environment = EvernoteService.SANDBOX;  // default is sandbox
		public Map<String, String> customHeaders = new HashMap<String, String>();

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

		public void setAlwaysUseTokenFromConfig(boolean alwaysUseTokenFromConfig) {
			this.alwaysUseTokenFromConfig = alwaysUseTokenFromConfig;
		}

		public void setFallbackToTokenFromConfig(boolean fallbackToTokenFromConfig) {
			this.fallbackToTokenFromConfig = fallbackToTokenFromConfig;
		}

		public void setCustomHeaders(Map<String, String> customHeaders) {
			this.customHeaders = customHeaders;
		}

		// getter is required for map to bind property values
		public Map<String, String> getCustomHeaders() {
			return customHeaders;
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

		final Evernote evernote;
		if (config.alwaysUseTokenFromConfig) {
			evernote = new EvernoteTemplate(evernoteService, config.accessToken);
		} else {
			String accessToken = request.getHeader("evernote-rest-accesstoken");
			if (accessToken == null && config.fallbackToTokenFromConfig) {
				accessToken = config.accessToken; // fallback to accesstoken from config
			}

			final String noteStoreUrl = request.getHeader("evernote-rest-notestoreurl");
			final String webApiUrlPrefix = request.getHeader("evernote-rest-webapiurlprefix");
			final String userId = request.getHeader("evernote-rest-userid");

			if (noteStoreUrl != null && webApiUrlPrefix != null && userId != null) {
				evernote = new EvernoteTemplate(evernoteService, accessToken, noteStoreUrl, webApiUrlPrefix, userId);
			} else {
				evernote = new EvernoteTemplate(evernoteService, accessToken);
			}
		}

		// for this rest app, do not create proxy for thrift object
		evernote.setApplyNullSafe(false);

		final Map<String, String> customHeaders = config.getCustomHeaders();
		if (!customHeaders.isEmpty()) {
			evernote.clientFactory().setCustomHeaders(customHeaders);
		}


		return evernote;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * To initialize application when it is deployed to web container as a war file.
	 */
	public static class ApplicationServletInitializer extends SpringBootServletInitializer {
		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
			return application.sources(Application.class);
		}
	}

}
