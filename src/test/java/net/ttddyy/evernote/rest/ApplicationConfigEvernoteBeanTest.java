package net.ttddyy.evernote.rest;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import org.junit.Test;
import org.springframework.social.evernote.api.Evernote;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.WebRequest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * @author Tadaya Tsuyukubo
 */
public class ApplicationConfigEvernoteBeanTest {

	@Test
	public void testWithAccessTokenOnly() {

		Application application = new Application();
		application.evernotePropertiesConfiguration = new Application.EvernotePropertiesConfiguration();

		WebRequest request = mock(WebRequest.class);
		when(request.getHeader("evernote-rest-accesstoken")).thenReturn("ACCESS_TOKEN");

		Evernote evernote = application.evernote(request);
		assertThat(evernote, is(notNullValue()));

		ClientFactory clientFactory = evernote.clientFactory();
		assertThat(clientFactory, is(notNullValue()));

		EvernoteAuth evernoteAuth = retrieveEvernoteAuth(clientFactory);
		assertThat(evernoteAuth, is(notNullValue()));
		assertThat(evernoteAuth.getToken(), is("ACCESS_TOKEN"));
		assertThat(evernoteAuth.getNoteStoreUrl(), is(nullValue()));
		assertThat(evernoteAuth.getUserId(), is(0));  // default
		assertThat(evernoteAuth.getWebApiUrlPrefix(), is(nullValue()));

	}

	@Test
	public void testWithFullAccessTokenAttributes() {

		Application application = new Application();
		application.evernotePropertiesConfiguration = new Application.EvernotePropertiesConfiguration();

		WebRequest request = mock(WebRequest.class);
		when(request.getHeader("evernote-rest-accesstoken")).thenReturn("ACCESS_TOKEN");
		when(request.getHeader("evernote-rest-notestoreurl")).thenReturn("NOTE_STORE_URL");
		when(request.getHeader("evernote-rest-webapiurlprefix")).thenReturn("WEB_API_URL_PREFIX");
		when(request.getHeader("evernote-rest-userid")).thenReturn("100");

		Evernote evernote = application.evernote(request);
		assertThat(evernote, is(notNullValue()));

		ClientFactory clientFactory = evernote.clientFactory();
		assertThat(clientFactory, is(notNullValue()));

		EvernoteAuth evernoteAuth = retrieveEvernoteAuth(clientFactory);
		assertThat(evernoteAuth, is(notNullValue()));
		assertThat(evernoteAuth.getToken(), is("ACCESS_TOKEN"));
		assertThat(evernoteAuth.getNoteStoreUrl(), is("NOTE_STORE_URL"));
		assertThat(evernoteAuth.getUserId(), is(100));
		assertThat(evernoteAuth.getWebApiUrlPrefix(), is("WEB_API_URL_PREFIX"));

	}

	@Test
	public void testAlwaysUseTokenFromConfig() {

		Application.EvernotePropertiesConfiguration config = new Application.EvernotePropertiesConfiguration();
		config.setAlwaysUseTokenFromConfig(true);
		config.setAccessToken("ACCESS_TOKEN");

		Application application = new Application();
		application.evernotePropertiesConfiguration = config;

		WebRequest request = mock(WebRequest.class);
		Evernote evernote = application.evernote(request);
		assertThat(evernote, is(notNullValue()));

		ClientFactory clientFactory = evernote.clientFactory();
		assertThat(clientFactory, is(notNullValue()));

		EvernoteAuth evernoteAuth = retrieveEvernoteAuth(clientFactory);
		assertThat(evernoteAuth, is(notNullValue()));
		assertThat(evernoteAuth.getToken(), is("ACCESS_TOKEN"));

		EvernoteService evernoteService = retrieveEvernoteService(evernoteAuth);
		assertThat(evernoteService, is(notNullValue()));
		assertThat(evernoteService, is(EvernoteService.SANDBOX));

	}

	@Test
	public void testFallbackToTokenFromConfig() {

		Application.EvernotePropertiesConfiguration config = new Application.EvernotePropertiesConfiguration();
		config.setFallbackToTokenFromConfig(true);
		config.setAccessToken("ACCESS_TOKEN_FROM_CONFIG");

		Application application = new Application();
		application.evernotePropertiesConfiguration = config;

		WebRequest request = mock(WebRequest.class);
		Evernote evernote = application.evernote(request);
		assertThat(evernote, is(notNullValue()));

		ClientFactory clientFactory = evernote.clientFactory();
		assertThat(clientFactory, is(notNullValue()));

		EvernoteAuth evernoteAuth = retrieveEvernoteAuth(clientFactory);
		assertThat(evernoteAuth, is(notNullValue()));
		assertThat(evernoteAuth.getToken(), is("ACCESS_TOKEN_FROM_CONFIG"));

	}

	private EvernoteAuth retrieveEvernoteAuth(ClientFactory clientFactory) {
		return (EvernoteAuth) ReflectionTestUtils.getField(clientFactory, "evernoteAuth");
	}

	private EvernoteService retrieveEvernoteService(EvernoteAuth evernoteAuth) {
		return (EvernoteService) ReflectionTestUtils.getField(evernoteAuth, "service");
	}
}
