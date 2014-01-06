package net.ttddyy.evernote.rest;

import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
import org.springframework.social.evernote.api.Evernote;
import org.springframework.social.evernote.api.NoteStoreOperations;
import org.springframework.social.evernote.api.StoreClientHolder;
import org.springframework.social.evernote.api.UserStoreOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * This test class uses mock in static variables, so this test class cannot run in parallel.
 *
 * @author Tadaya Tsuyukubo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
		classes = {Application.class, StoreOperationControllerIntegrationTest.MockEvernoteConfig.class},
		loader = SpringApplicationContextLoader.class,
		initializers = IntegrationTestInitializer.class
)
public class StoreOperationControllerIntegrationTest {

	private static NoteStoreOperations noteStoreOperations;
	private static UserStoreOperations userStoreOperations;

	/**
	 * Configuration class to override a request scoped Evernote bean to return a mock.
	 */
	@Configuration
	public static class MockEvernoteConfig {
		@Bean
		@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
		public Evernote evernote(WebRequest request) {
			Evernote evernote = mock(Evernote.class);
			when(evernote.userStoreOperations()).thenReturn(userStoreOperations);
			when(evernote.noteStoreOperations()).thenReturn(noteStoreOperations);
			return evernote;
		}

	}

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		// prepare mocks
		noteStoreOperations = mock(NoteStoreOperations.class, withSettings().extraInterfaces(StoreClientHolder.class));
		userStoreOperations = mock(UserStoreOperations.class, withSettings().extraInterfaces(StoreClientHolder.class));

		// To work around getClass() method to return actual store-client class for parameter name discovery, use
		// objenesis to create actual impl class instead of mocking.
		// mockito cannot mock getClass() since this method is final.
		Objenesis objenesis = new ObjenesisStd();
		UserStoreClient userStoreClient = (UserStoreClient) objenesis.newInstance(UserStoreClient.class);
		NoteStoreClient noteStoreClient = (NoteStoreClient) objenesis.newInstance(NoteStoreClient.class);
		when(((StoreClientHolder) userStoreOperations).getStoreClient()).thenReturn(userStoreClient);
		when(((StoreClientHolder) noteStoreOperations).getStoreClient()).thenReturn(noteStoreClient);
	}

	@Test
	public void testUserStoreIsBusinessUser() throws Exception {
		performRequest("/userStore/isBusinessUser", "{}");
		verify(userStoreOperations).isBusinessUser();
	}

	@Test
	public void testUserStoreCheckVersion() throws Exception {
		String json = "{\"clientName\": \"foo\", \"edamVersionMajor\": 10, \"edamVersionMinor\": 20}";
		performRequest("/userStore/checkVersion", json);
		verify(userStoreOperations).checkVersion("foo", (short) 10, (short) 20);
	}

	@Test
	public void testUserStoreGetBootstrapInfo() throws Exception {
		String json = "{\"locale\": \"foo\"}";
		performRequest("/userStore/getBootstrapInfo", json);
		verify(userStoreOperations).getBootstrapInfo("foo");
	}

	@Test
	public void testUserStoreAuthenticate() throws Exception {
		String json = "{\"username\":\"foo\", \"password\":\"foo-pass\", " +
				"\"consumerKey\": \"key\", \"consumerSecret\": \"secret\", \"supportsTwoFactor\":true }";
		performRequest("/userStore/authenticate", json);
		verify(userStoreOperations).authenticate("foo", "foo-pass", "key", "secret", true);
	}

	@Test
	public void testUserStoreAuthenticateLongSession() throws Exception {
		String json = "{\"username\":\"foo\", \"password\":\"foo-pass\", " +
				"\"consumerKey\": \"key\", \"consumerSecret\": \"secret\", " +
				"\"deviceIdentifier\":\"deviceId\", \"deviceDescription\":\"deviceDesc\", " +
				"\"supportsTwoFactor\":true }";
		performRequest("/userStore/authenticateLongSession", json);
		verify(userStoreOperations).authenticateLongSession("foo", "foo-pass", "key", "secret", "deviceId", "deviceDesc", true);
	}

	@Test
	public void testUserStoreAuthenticateToBusiness() throws Exception {
		performRequest("/userStore/authenticateToBusiness", "{}");
		verify(userStoreOperations).authenticateToBusiness();
	}

	@Test
	public void testUserStoreRefreshAuthentication() throws Exception {
		performRequest("/userStore/refreshAuthentication", "{}");
		verify(userStoreOperations).refreshAuthentication();
	}

	@Test
	public void testUserStoreGetUser() throws Exception {
		performRequest("/userStore/getUser", "{}");
		verify(userStoreOperations).getUser();
	}

	@Test
	public void testUserStoreGetPublicUserInfo() throws Exception {
		performRequest("/userStore/getPublicUserInfo", "{\"username\":\"foo\"}");
		verify(userStoreOperations).getPublicUserInfo("foo");
	}

	@Test
	public void testUserStoreGetPremiumInfo() throws Exception {
		performRequest("/userStore/getPremiumInfo", "{}");
		verify(userStoreOperations).getPremiumInfo();
	}

	@Test
	public void testUserStoreGetNoteSoreUrl() throws Exception {
		performRequest("/userStore/getNoteStoreUrl", "{}");
		verify(userStoreOperations).getNoteStoreUrl();
	}

	@Test
	public void testUserStoreRevokeLongSession() throws Exception {
		performRequest("/userStore/revokeLongSession", "{}");
		verify(userStoreOperations).revokeLongSession();
	}

	@Test
	public void testUserStoreCompleteTwoFactorAuthentication() throws Exception {
		String json = "{\"authenticationToken\":\"authToken\", \"oneTimeCode\":\"oneTime\", " +
				"\"deviceIdentifier\":\"deviceId\", \"deviceDescription\":\"deviceDesc\"}";
		performRequest("/userStore/completeTwoFactorAuthentication", json);
		verify(userStoreOperations).completeTwoFactorAuthentication("authToken", "oneTime", "deviceId", "deviceDesc");
	}


	private void performRequest(String url, String content) throws Exception {
		mockMvc.perform(post(url).content(content).contentType(MediaType.APPLICATION_JSON));
	}
}
