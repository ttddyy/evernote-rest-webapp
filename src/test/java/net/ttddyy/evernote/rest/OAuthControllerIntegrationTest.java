package net.ttddyy.evernote.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.MediaType;
import org.springframework.social.evernote.connect.EvernoteConnectionFactory;
import org.springframework.social.evernote.connect.EvernoteOAuth1Operations;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for {@link OAuthController}.
 *
 * @author Tadaya Tsuyukubo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
		classes = {Application.class},
		loader = SpringApplicationContextLoader.class,
		initializers = IntegrationTestInitializer.class
)
public class OAuthControllerIntegrationTest {

	private MockMvc mockMvc;

	private MockRestServiceServer mockServer;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private EvernoteConnectionFactory evernoteConnectionFactory;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		EvernoteOAuth1Operations evernoteOAuth1Operations = (EvernoteOAuth1Operations) this.evernoteConnectionFactory.getOAuthOperations();
		OAuth1Operations operations = evernoteOAuth1Operations.getSelectedOauth1Operations();
		RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(operations, "restTemplate");
		this.mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void testAuthorization() throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("oauth_token=" + "TOKEN");
		sb.append("&oauth_token_secret=" + "SECRET");
		sb.append("&oauth_callback_confirmed=" + "true");
		mockServer.expect(requestTo("https://sandbox.evernote.com/oauth"))
				.andRespond(withSuccess(sb.toString(), MediaType.TEXT_PLAIN));

		mockMvc.perform(post("/oauth/auth").param("callbackUrl", "foo"))
				.andExpect(jsonPath("$.authorizeUrl").value("https://sandbox.evernote.com/OAuth.action?oauth_token=TOKEN"))
				.andExpect(jsonPath("$.requestTokenValue").value("TOKEN"))
				.andExpect(jsonPath("$.requestTokenSecret").value("SECRET"));

		mockServer.verify();
	}

	@Test
	public void testAuthorizationWithNewAccount() throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("oauth_token=" + "TOKEN");
		sb.append("&oauth_token_secret=" + "SECRET");
		sb.append("&oauth_callback_confirmed=" + "true");
		mockServer.expect(requestTo("https://sandbox.evernote.com/oauth"))
				.andRespond(withSuccess(sb.toString(), MediaType.TEXT_PLAIN));

		mockMvc.perform(post("/oauth/auth")
				.param("callbackUrl", "foo")
				.param("preferRegistration", "true"))
				.andExpect(jsonPath("$.authorizeUrl").value("https://sandbox.evernote.com/OAuth.action?oauth_token=TOKEN&preferRegistration=true"));

		mockServer.verify();
	}

	@Test
	public void testAccessToken() throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("oauth_token=" + "TOKEN");
		sb.append("&oauth_token_secret=" + "SECRET");
		sb.append("&edam_shard=" + "SHARD");
		sb.append("&edam_userId=" + "1234");
		sb.append("&edam_expires=" + "5678");
		sb.append("&edam_noteStoreUrl=" + "https://NOTE_STORE_URL");
		sb.append("&edam_webApiUrlPrefix=" + "https://WEB_API_URL_PREFIX");
		mockServer.expect(requestTo("https://sandbox.evernote.com/oauth"))
				.andRespond(withSuccess(sb.toString(), MediaType.TEXT_PLAIN));

		mockMvc.perform(post("/oauth/accessToken")
				.param("oauthToken", "token")
				.param("oauthVerifier", "verifier")
				.param("requestTokenSecret", "secret"))
				.andExpect(jsonPath("$.value").value("TOKEN"))
				.andExpect(jsonPath("$.secret").value("SECRET"))
				.andExpect(jsonPath("$.edamShard").value("SHARD"))
				.andExpect(jsonPath("$.edamUserId").value("1234"))
				.andExpect(jsonPath("$.edamExpires").value("5678"))
				.andExpect(jsonPath("$.edamNoteStoreUrl").value("https://NOTE_STORE_URL"))
				.andExpect(jsonPath("$.edamWebApiUrlPrefix").value("https://WEB_API_URL_PREFIX"));

		mockServer.verify();
	}
}
