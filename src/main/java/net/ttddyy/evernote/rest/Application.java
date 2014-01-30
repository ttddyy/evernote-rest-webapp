package net.ttddyy.evernote.rest;

import com.evernote.auth.EvernoteService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
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
		public String userAgent;
		public Map<String, String> customHeaders = new HashMap<String, String>();
		public boolean formatOutput = false;

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

		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}

		public void setCustomHeaders(Map<String, String> customHeaders) {
			this.customHeaders = customHeaders;
		}

		// getter is required for map to bind property values
		public Map<String, String> getCustomHeaders() {
			return customHeaders;
		}

		public void setFormatOutput(boolean formatOutput) {
			this.formatOutput = formatOutput;
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

		if (config.userAgent != null) {
			evernote.clientFactory().setUserAgent(config.userAgent);
		}

		final Map<String, String> customHeaders = config.getCustomHeaders();
		if (!customHeaders.isEmpty()) {
			evernote.clientFactory().setCustomHeaders(customHeaders);
		}


		return evernote;
	}

	/**
	 * override spring-boot default ObjectMapper to configure output(serialization) json.
	 *
	 * @see org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration.ObjectMappers#jacksonObjectMapper()
	 */
	@Bean
	public ObjectMapper jacksonObjectMapper() {

		// use different visibility for serialization(output json)
		// I want to ONLY change the visibility for serialization, but couldn't find nice way to do it.
		// maybe related to this issue: https://github.com/FasterXML/jackson-databind/issues/352
		// for now, override ObjectMapper and set new SerializationConfig in instance initializer.
		// TODO: find correct way to do this.
		final ObjectMapper mapper = new ObjectMapper() {
			{
				// use instance fields for output json
				_serializationConfig = _serializationConfig.with(
						_serializationConfig.getDefaultVisibilityChecker()
								.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
								.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
								.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
								.withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
								.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				);

				if (evernotePropertiesConfiguration.formatOutput) {
					_serializationConfig = _serializationConfig.with(SerializationFeature.INDENT_OUTPUT);
				}
			}
		};

		// mix-in to ignore thrift specific fields for serialization.
		mapper.addMixInAnnotations(Object.class, ThriftPropertyJacksonFilter.class);

		return mapper;
	}

	/**
	 * Mix-in class for Jackson to ignore thrift specific fields in serialization.
	 */
	@JsonIgnoreProperties("__isset_vector")
	private static abstract class ThriftPropertyJacksonFilter {
	}

	/**
	 * To maximize the caching feature in name discoverer, inject as a bean instead of creating every time.
	 */
	@Bean
	public ParameterNameDiscoverer parameterNameDiscoverer() {
		return new DefaultParameterNameDiscoverer();
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
