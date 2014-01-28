package net.ttddyy.evernote.rest;

import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.social.evernote.api.Evernote;
import org.springframework.social.evernote.api.NoteStoreOperations;
import org.springframework.social.evernote.api.StoreClientHolder;
import org.springframework.social.evernote.api.UserStoreOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Tadaya Tsuyukubo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
		classes = {Application.class, StoreOperationControllerUserStoreIntegrationTest.MockEvernoteConfig.class},
		loader = SpringApplicationContextLoader.class,
		initializers = IntegrationTestInitializer.class
)
public abstract class AbstractStoreOperationControllerIntegrationTest {
	/**
	 * Configuration class to override a request scoped Evernote bean to a singleton bean to return a mock.
	 */
	@Configuration
	public static class MockEvernoteConfig {
		// currently evernote bean is only returning ~StoreOperations mocks.
		// In future, if evernote bean gets tainted, need to change the scope to prototpye or reset the mock.
		@Bean
		public Evernote evernote() {
			return mock(Evernote.class);
		}

	}

	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;

	@Autowired
	protected Evernote evernote;

	@Autowired
	protected ResourceLoader resourceLoader;

	protected NoteStoreOperations noteStoreOperations;

	protected UserStoreOperations userStoreOperations;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		// prepare mocks
		this.noteStoreOperations = mock(NoteStoreOperations.class, withSettings().extraInterfaces(StoreClientHolder.class));
		this.userStoreOperations = mock(UserStoreOperations.class, withSettings().extraInterfaces(StoreClientHolder.class));

		// To work around getClass() method to return actual store-client class for parameter name discovery, use
		// objenesis to create actual impl class instead of mocking.
		// mockito cannot mock getClass() since this method is final.
		Objenesis objenesis = new ObjenesisStd();
		UserStoreClient userStoreClient = (UserStoreClient) objenesis.newInstance(UserStoreClient.class);
		NoteStoreClient noteStoreClient = (NoteStoreClient) objenesis.newInstance(NoteStoreClient.class);
		when(((StoreClientHolder) userStoreOperations).getStoreClient()).thenReturn(userStoreClient);
		when(((StoreClientHolder) noteStoreOperations).getStoreClient()).thenReturn(noteStoreClient);

		when(this.evernote.userStoreOperations()).thenReturn(userStoreOperations);
		when(this.evernote.noteStoreOperations()).thenReturn(noteStoreOperations);
	}

	protected ResultActions performRequest(String url, String content) throws Exception {
		return mockMvc.perform(post(url).content(content).contentType(MediaType.APPLICATION_JSON));
	}

}
