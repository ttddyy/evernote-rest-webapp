package net.ttddyy.evernote.rest;

import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.notestore.RelatedQuery;
import com.evernote.edam.notestore.RelatedResultSpec;
import com.evernote.edam.type.*;
import org.junit.Before;
import org.junit.Test;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test class for {@link StoreOperationController}
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
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private ResourceLoader resourceLoader;


	@Autowired
	private Evernote evernote;

	private NoteStoreOperations noteStoreOperations;

	private UserStoreOperations userStoreOperations;


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


	//// test for UserStoreOperations

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

	//// test for NoteStoreOperations

	@Test
	public void testNoteStoreGetSyncState() throws Exception {
		performRequest("/noteStore/getSyncState", "{}");
		verify(noteStoreOperations).getSyncState();
	}

	// TODO: getSyncStateWithMetrics(ClientUsageMetrics clientMetrics)
	@Test
	public void testNoteStoreGetSyncChunk() throws Exception {
		performRequest("/noteStore/getSyncChunk", "{\"afterUSN\":100, \"maxEntries\":200, \"fullSyncOnly\":true}");
		verify(noteStoreOperations).getSyncChunk(100, 200, true);
	}

	// TODO: getFilteredSyncChunk(int afterUSN, int maxEntries, SyncChunkFilter filter)
	@Test
	public void testNoteStoreGetLinkedNotebookSyncState() throws Exception {
		String json = readJson("classpath:/input/linkedNotebook.json");
		performRequest("/noteStore/getLinkedNotebookSyncState", json);
		verify(noteStoreOperations).getLinkedNotebookSyncState(getLinkedNotebook());
	}

	@Test
	public void testNoteStoreGetLinkedNotebookSyncChunk() throws Exception {
		String json = readJson("classpath:/input/getLinkedNotebookSyncChunk.json");
		performRequest("/noteStore/getLinkedNotebookSyncChunk", json);
		verify(noteStoreOperations).getLinkedNotebookSyncChunk(getLinkedNotebook(), 10, 20, true);
	}

	@Test
	public void testNoteStoreListNotebooks() throws Exception {
		performRequest("/noteStore/listNotebooks", "{}");
		verify(noteStoreOperations).listNotebooks();
	}

	@Test
	public void testNoteStoreGetNotebook() throws Exception {
		performRequest("/noteStore/getNotebook", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNotebook("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetDefaultNotebook() throws Exception {
		performRequest("/noteStore/getDefaultNotebook", "{}");
		verify(noteStoreOperations).getDefaultNotebook();
	}

	@Test
	public void testNoteStoreCreateNotebook() throws Exception {
		String json = readJson("classpath:/input/notebook_full.json");
		performRequest("/noteStore/createNotebook", json);
		Notebook notebook = getNotebookFull();
		verify(noteStoreOperations).createNotebook(notebook);
	}

	@Test
	public void testNoteStoreUpdateNotebook() throws Exception {
		String json = readJson("classpath:/input/notebook_full.json");
		performRequest("/noteStore/updateNotebook", json);
		Notebook notebook = getNotebookFull();
		verify(noteStoreOperations).updateNotebook(notebook);
	}

	@Test
	public void testNoteStoreExpungeNotebook() throws Exception {
		performRequest("/noteStore/expungeNotebook", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeNotebook("GUID_FOO");
	}

	@Test
	public void testNoteStoreListTags() throws Exception {
		performRequest("/noteStore/listTags", "{}");
		verify(noteStoreOperations).listTags();
	}

	@Test
	public void testNoteStoreListTagsByNotebook() throws Exception {
		performRequest("/noteStore/listTagsByNotebook", "{\"notebookGuid\":\"NOTEBOOK_GUID_FOO\"}");
		verify(noteStoreOperations).listTagsByNotebook("NOTEBOOK_GUID_FOO");
	}

	@Test
	public void testNoteStoreGetTag() throws Exception {
		performRequest("/noteStore/getTag", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getTag("GUID_FOO");
	}

	@Test
	public void testNoteStoreCreateTag() throws Exception {
		String json = readJson("classpath:/input/tag.json");
		performRequest("/noteStore/createTag", json);
		verify(noteStoreOperations).createTag(getTag());
	}

	@Test
	public void testNoteStoreUpdateTag() throws Exception {
		String json = readJson("classpath:/input/tag.json");
		performRequest("/noteStore/updateTag", json);
		verify(noteStoreOperations).updateTag(getTag());
	}

	@Test
	public void testNoteStoreUntagAll() throws Exception {
		performRequest("/noteStore/untagAll", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).untagAll("GUID_FOO");
	}

	@Test
	public void testNoteStoreExpungeTag() throws Exception {
		performRequest("/noteStore/expungeTag", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeTag("GUID_FOO");
	}

	@Test
	public void testNoteStoreListSearches() throws Exception {
		performRequest("/noteStore/listSearches", "{}");
		verify(noteStoreOperations).listSearches();
	}

	@Test
	public void testNoteStoreGetSearch() throws Exception {
		performRequest("/noteStore/getSearch", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getSearch("GUID_FOO");
	}

	@Test
	public void testNoteStoreCreateSearch() throws Exception {
		String json = readJson("classpath:/input/savedSearch.json");
		performRequest("/noteStore/createSearch", json);
		verify(noteStoreOperations).createSearch(getSavedSearch());
	}

	@Test
	public void testNoteStoreUpdateSearch() throws Exception {
		String json = readJson("classpath:/input/savedSearch.json");
		performRequest("/noteStore/updateSearch", json);
		verify(noteStoreOperations).updateSearch(getSavedSearch());
	}

	@Test
	public void testNoteStoreExpungeSearch() throws Exception {
		performRequest("/noteStore/expungeSearch", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeSearch("GUID_FOO");
	}

	@Test
	public void testNoteStoreFindNotes() throws Exception {
		String json = readJson("classpath:/input/findNotes.json");
		performRequest("/noteStore/findNotes", json);
		verify(noteStoreOperations).findNotes(getNoteFilter(), 20, 30);
	}

	@Test
	public void testNoteStoreFindNoteOffset() throws Exception {
		String json = readJson("classpath:/input/findNoteOffset.json");
		performRequest("/noteStore/findNoteOffset", json);
		verify(noteStoreOperations).findNoteOffset(getNoteFilter(), "GUID");
	}

	@Test
	public void testNoteStoreFindNotesMetadata() throws Exception {
		String json = readJson("classpath:/input/findNotesMetadata.json");
		performRequest("/noteStore/findNotesMetadata", json);

		NotesMetadataResultSpec resultSpec = new NotesMetadataResultSpec();
		resultSpec.setIncludeTitle(true);
		resultSpec.setIncludeContentLength(true);
		resultSpec.setIncludeCreated(true);
		resultSpec.setIncludeUpdated(true);
		resultSpec.setIncludeDeleted(true);
		resultSpec.setIncludeUpdateSequenceNum(true);
		resultSpec.setIncludeNotebookGuid(true);
		resultSpec.setIncludeTagGuids(true);
		resultSpec.setIncludeAttributes(true);
		resultSpec.setIncludeLargestResourceMime(true);
		resultSpec.setIncludeLargestResourceSize(true);

		verify(noteStoreOperations).findNotesMetadata(getNoteFilter(), 10, 20, resultSpec);
	}

	@Test
	public void testNoteStoreFindNoteCounts() throws Exception {
		String json = readJson("classpath:/input/findNoteCounts.json");
		performRequest("/noteStore/findNoteCounts", json);
		verify(noteStoreOperations).findNoteCounts(getNoteFilter(), true);
	}

	@Test
	public void testNoteStoreGetNote() throws Exception {
		String json = "{\"guid\":\"GUID\", \"withContent\":true, \"withResourcesData\":true, \"withResourcesRecognition\":true, \"withResourcesAlternateData\":true}";
		performRequest("/noteStore/getNote", json);
		verify(noteStoreOperations).getNote("GUID", true, true, true, true);
	}

	@Test
	public void testNoteStoreGetNoteApplicationData() throws Exception {
		performRequest("/noteStore/getNoteApplicationData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNoteApplicationData("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetNoteApplicationDataEntry() throws Exception {
		performRequest("/noteStore/getNoteApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\"}");
		verify(noteStoreOperations).getNoteApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testNoteStoreSetNoteApplicationDataEntry() throws Exception {
		String json = "{\"guid\":\"GUID\", \"key\":\"KEY\", \"value\":\"VALUE\"}";
		performRequest("/noteStore/setNoteApplicationDataEntry", json);
		verify(noteStoreOperations).setNoteApplicationDataEntry("GUID", "KEY", "VALUE");
	}

	@Test
	public void testNoteStoreUnsetNoteApplicationDataEntry() throws Exception {
		performRequest("/noteStore/unsetNoteApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\"}");
		verify(noteStoreOperations).unsetNoteApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testNoteStoreGetNoteContent() throws Exception {
		performRequest("/noteStore/getNoteContent", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNoteContent("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetNoteSearchText() throws Exception {
		String json = "{\"guid\":\"GUID\", \"noteOnly\":true, \"tokenizeForIndexing\":true}";
		performRequest("/noteStore/getNoteSearchText", json);
		verify(noteStoreOperations).getNoteSearchText("GUID", true, true);
	}

	@Test
	public void testNoteStoreGetResourceSearchText() throws Exception {
		performRequest("/noteStore/getResourceSearchText", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceSearchText("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetNoteTagNames() throws Exception {
		performRequest("/noteStore/getNoteTagNames", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNoteTagNames("GUID_FOO");
	}

	// TODO: createNote(Note note)
	// TODO: updateNote(Note note)
	@Test
	public void testNoteStoreDeleteNote() throws Exception {
		performRequest("/noteStore/deleteNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).deleteNote("GUID_FOO");
	}

	@Test
	public void testNoteStoreExpungeNote() throws Exception {
		performRequest("/noteStore/expungeNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeNote("GUID_FOO");
	}

	@Test
	public void testNoteStoreExpungeNotes() throws Exception {
		performRequest("/noteStore/expungeNotes", "{\"noteGuids\":[\"FOO\", \"BAR\"]}");
		verify(noteStoreOperations).expungeNotes(Arrays.asList("FOO", "BAR"));
	}

	@Test
	public void testNoteStoreExpungeInactiveNotes() throws Exception {
		performRequest("/noteStore/expungeInactiveNotes", "{}");
		verify(noteStoreOperations).expungeInactiveNotes();
	}

	@Test
	public void testNoteStoreCopyNote() throws Exception {
		performRequest("/noteStore/copyNote", "{\"noteGuid\":\"NOTE_GUID\", \"toNotebookGuid\":\"TO_NOTEBOOK_GUID\"}");
		verify(noteStoreOperations).copyNote("NOTE_GUID", "TO_NOTEBOOK_GUID");
	}

	@Test
	public void testNoteStoreListNoteVersions() throws Exception {
		performRequest("/noteStore/listNoteVersions", "{\"noteGuid\":\"NOTE_GUID_FOO\"}");
		verify(noteStoreOperations).listNoteVersions("NOTE_GUID_FOO");
	}

	@Test
	public void testNoteStoreGetNoteVersion() throws Exception {
		String json = "{\"noteGuid\":\"NOTE_GUID\", \"updateSequenceNum\":100, \"withResourcesData\":true, \"withResourcesRecognition\":true, \"withResourcesAlternateData\":true}";
		performRequest("/noteStore/getNoteVersion", json);
		verify(noteStoreOperations).getNoteVersion("NOTE_GUID", 100, true, true, true);
	}

	@Test
	public void testNoteStoreGetResource() throws Exception {
		String json = "{\"guid\":\"GUID\", \"withData\":true, \"withRecognition\":true, \"withAttributes\":true, \"withAlternateData\":true}";
		performRequest("/noteStore/getResource", json);
		verify(noteStoreOperations).getResource("GUID", true, true, true, true);
	}

	@Test
	public void testNoteStoreGetResourceApplicationData() throws Exception {
		performRequest("/noteStore/getResourceApplicationData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceApplicationData("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetResourceApplicationDataEntry() throws Exception {
		performRequest("/noteStore/getResourceApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\" }");
		verify(noteStoreOperations).getResourceApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testNoteStoreSetResourceApplicationDataEntry() throws Exception {
		String json = "{\"guid\":\"GUID\", \"key\":\"KEY\", \"value\":\"VALUE\" }";
		performRequest("/noteStore/setResourceApplicationDataEntry", json);
		verify(noteStoreOperations).setResourceApplicationDataEntry("GUID", "KEY", "VALUE");
	}

	@Test
	public void testNoteStoreUnsetResourceApplicationDataEntry() throws Exception {
		performRequest("/noteStore/unsetResourceApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\" }");
		verify(noteStoreOperations).unsetResourceApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testNoteStoreUpdateResource() throws Exception {
		String json = readJson("classpath:/input/updateResource.json");
		performRequest("/noteStore/updateResource", json);

		Set<String> keysOnly = new HashSet<String>();
		keysOnly.add("KEYS_ONLY_FOO");
		keysOnly.add("KEYS_ONLY_BAR");

		Map<String, String> fullMap = new HashMap<String, String>();
		fullMap.put("FULLMAP_FOO_KEY", "FULLMAP_FOO_VALUE");
		fullMap.put("FULLMAP_BAR_KEY", "FULLMAP_BAR_VALUE");

		LazyMap lazyMap = new LazyMap();
		lazyMap.setKeysOnly(keysOnly);
		lazyMap.setFullMap(fullMap);

		ResourceAttributes resourceAttributes = new ResourceAttributes();
		resourceAttributes.setSourceURL("RESOURCE_ATTR_SOURCE_URL");
		resourceAttributes.setTimestamp(100);
		resourceAttributes.setLatitude(200);
		resourceAttributes.setLongitude(300);
		resourceAttributes.setAltitude(400);
		resourceAttributes.setCameraMake("RESOURCE_ATTR_CAMERA_MAKE");
		resourceAttributes.setCameraModel("RESOURCE_ATTR_CAMERA_MODEL");
		resourceAttributes.setClientWillIndex(true);
		resourceAttributes.setRecoType("RESOURCE_ATTR_RECO_TYPE");
		resourceAttributes.setFileName("RESOURCE_ATTR_FILE_NAME");
		resourceAttributes.setAttachment(true);
		resourceAttributes.setApplicationData(lazyMap);

		Resource resource = new Resource();
		resource.setGuid("RESOURCE_GUID");
		resource.setNoteGuid("RESOURCE_NOTE_GUID");
		resource.setData(getData());
		resource.setMime("RESOURCE_MIME");
		resource.setWidth((short) 10);
		resource.setHeight((short) 20);
		resource.setDuration((short) 30);
		resource.setActive(true);
		resource.setRecognition(getData());
		resource.setAttributes(resourceAttributes);
		resource.setUpdateSequenceNum(40);
		resource.setAlternateData(getData());
		verify(noteStoreOperations).updateResource(resource);
	}

	@Test
	public void testNoteStoreGetResourceData() throws Exception {
		performRequest("/noteStore/getResourceData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceData("GUID_FOO");
	}

	// TODO: getResourceByHash(String noteGuid, byte[] contentHash, boolean withData, boolean withRecognition, boolean withAlternateData)
	@Test
	public void testNoteStoreGetResourceRecognition() throws Exception {
		performRequest("/noteStore/getResourceRecognition", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceRecognition("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetResourceAlternateData() throws Exception {
		performRequest("/noteStore/getResourceAlternateData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceAlternateData("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetResourceAttributes() throws Exception {
		performRequest("/noteStore/getResourceAttributes", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceAttributes("GUID_FOO");
	}

	@Test
	public void testNoteStoreGetPublicNotebook() throws Exception {
		performRequest("/noteStore/getPublicNotebook", "{\"userId\": 100, \"publicUri\":\"PUBLIC_URI\"}");
		verify(noteStoreOperations).getPublicNotebook(100, "PUBLIC_URI");
	}

	@Test
	public void testNoteStoreCreateSharedNotebook() throws Exception {
		String json = readJson("classpath:/input/sharedNotebook1.json");
		performRequest("/noteStore/createSharedNotebook", json);
		verify(noteStoreOperations).createSharedNotebook(getSharedNotebook1());
	}

	@Test
	public void testNoteStoreUpdateSharedNotebook() throws Exception {
		String json = readJson("classpath:/input/sharedNotebook1.json");
		performRequest("/noteStore/updateSharedNotebook", json);
		verify(noteStoreOperations).updateSharedNotebook(getSharedNotebook1());
	}

	// TODO: sendMessageToSharedNotebookMembers(String notebookGuid, String messageText, List<String> recipients)
	@Test
	public void testNoteStoreListSharedNotebooks() throws Exception {
		performRequest("/noteStore/listSharedNotebooks", "{}");
		verify(noteStoreOperations).listSharedNotebooks();
	}

	@Test
	public void testNoteStoreExpungeSharedNotebooks() throws Exception {
		performRequest("/noteStore/expungeSharedNotebooks", "{\"sharedNotebookIds\":[1,2,3]}");
		verify(noteStoreOperations).expungeSharedNotebooks(Arrays.asList(1L, 2L, 3L));
	}

	@Test
	public void testNoteStoreCreateLinkedNotebook() throws Exception {
		String json = readJson("classpath:/input/linkedNotebook.json");
		performRequest("/noteStore/createLinkedNotebook", json);
		verify(noteStoreOperations).createLinkedNotebook(getLinkedNotebook());
	}

	@Test
	public void testNoteStoreUpdateLinkedNotebook() throws Exception {
		String json = readJson("classpath:/input/linkedNotebook.json");
		performRequest("/noteStore/updateLinkedNotebook", json);
		verify(noteStoreOperations).updateLinkedNotebook(getLinkedNotebook());
	}

	@Test
	public void testNoteStoreListLinkedNotebooks() throws Exception {
		performRequest("/noteStore/listLinkedNotebooks", "{}");
		verify(noteStoreOperations).listLinkedNotebooks();
	}

	@Test
	public void testNoteStoreExpungeLinkedNotebook() throws Exception {
		performRequest("/noteStore/expungeLinkedNotebook", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeLinkedNotebook("GUID_FOO");
	}

	@Test
	public void testNoteStoreAuthenticateToSharedNotebook() throws Exception {
		performRequest("/noteStore/authenticateToSharedNotebook", "{\"shareKey\":\"SHARE_KEY_FOO\"}");
		verify(noteStoreOperations).authenticateToSharedNotebook("SHARE_KEY_FOO");
	}

	@Test
	public void testNoteStorGetSharedNotebookByAuth() throws Exception {
		performRequest("/noteStore/getSharedNotebookByAuth", "{}");
		verify(noteStoreOperations).getSharedNotebookByAuth();
	}

	// TODO: emailNote(NoteEmailParameters parameters)
	@Test
	public void testNoteStoreShareNote() throws Exception {
		performRequest("/noteStore/shareNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).shareNote("GUID_FOO");
	}

	@Test
	public void testNoteStoreStopSharingNote() throws Exception {
		performRequest("/noteStore/stopSharingNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).stopSharingNote("GUID_FOO");
	}

	@Test
	public void testNoteStoreAuthenticateToSharedNote() throws Exception {
		String json = "{\"guid\":\"GUID\", \"noteKey\":\"NOTE_KEY\", \"authenticationToken\":\"AUTHENTICATION_TOKEN\" }";
		performRequest("/noteStore/authenticateToSharedNote", json);
		verify(noteStoreOperations).authenticateToSharedNote("GUID", "NOTE_KEY", "AUTHENTICATION_TOKEN");
	}

	@Test
	public void testNoteStoreFindRelated() throws Exception {
		String json = readJson("classpath:/input/findRelated.json");
		performRequest("/noteStore/findRelated", json);

		RelatedQuery query = new RelatedQuery();
		query.setNoteGuid("RELATED_QUERY_NOTE_GUID");
		query.setPlainText("RELATED_QUERY_PLAIN_TEXT");
		query.setFilter(getNoteFilter());
		query.setReferenceUri("RELATED_QUERY_REFERENCE_URI");

		RelatedResultSpec resultSpec = new RelatedResultSpec();
		resultSpec.setMaxNotes(1);
		resultSpec.setMaxNotebooks(2);
		resultSpec.setMaxTags(3);
		resultSpec.setWritableNotebooksOnly(true);
		resultSpec.setIncludeContainingNotebooks(true);
		verify(noteStoreOperations).findRelated(query, resultSpec);
	}
	// TODO: setSharedNotebookRecipientSettings( final String authenticationToken, final long sharedNotebookId, final SharedNotebookRecipientSettings recipientSettings)


	private void performRequest(String url, String content) throws Exception {
		mockMvc.perform(post(url).content(content).contentType(MediaType.APPLICATION_JSON));
	}

	private String readJson(String location) throws IOException {
		org.springframework.core.io.Resource resource = resourceLoader.getResource(location);
		return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
	}

	private LinkedNotebook getLinkedNotebook() {
		LinkedNotebook linkedNotebook = new LinkedNotebook();
		linkedNotebook.setShareName("SHARE_NAME");
		linkedNotebook.setUsername("USERNAME");
		linkedNotebook.setShardId("SHARD_ID");
		linkedNotebook.setShareKey("SHARE_KEY");
		linkedNotebook.setUri("URI");
		linkedNotebook.setGuid("GUID");
		linkedNotebook.setUpdateSequenceNum(100);
		linkedNotebook.setNoteStoreUrl("NOTE_STORE_URL");
		linkedNotebook.setWebApiUrlPrefix("WEB_API_URL_PREFIX");
		linkedNotebook.setStack("STACK");
		linkedNotebook.setBusinessId(200);
		return linkedNotebook;
	}

	private SavedSearch getSavedSearch() {
		SavedSearchScope scope = new SavedSearchScope();
		scope.setIncludeAccount(true);
		scope.setIncludePersonalLinkedNotebooks(true);
		scope.setIncludeBusinessLinkedNotebooks(true);

		SavedSearch savedSearch = new SavedSearch();
		savedSearch.setGuid("SAVEDSEARCH_GUID");
		savedSearch.setName("SAVEDSEARCH_NAME");
		savedSearch.setQuery("SAVEDSEARCH_QUERY");
		savedSearch.setFormat(QueryFormat.USER);
		savedSearch.setUpdateSequenceNum(200);
		savedSearch.setScope(scope);
		return savedSearch;
	}

	private Tag getTag() {
		Tag tag = new Tag();
		tag.setGuid("TAG_GUID");
		tag.setName("TAG_NAME");
		tag.setParentGuid("TAG_PARENT_GUID");
		tag.setUpdateSequenceNum(500);
		return tag;
	}

	private NoteFilter getNoteFilter() {
		NoteFilter noteFilter = new NoteFilter();
		noteFilter.setOrder(20);
		noteFilter.setAscending(true);
		noteFilter.setWords("NOTE_FILTER_WORDS");
		noteFilter.setNotebookGuid("NOTE_FILTER_NOTEBOOK_GUID");
		noteFilter.setTagGuids(Arrays.asList("NOTE_FILTER_TAG_1", "NOTE_FILTER_TAG_2"));
		noteFilter.setTimeZone("NOTE_FILTER_TIMEZONE");
		noteFilter.setInactive(true);
		noteFilter.setEmphasized("NOTE_FILTER_EMPHASIZED");
		return noteFilter;
	}

	private Data getData() {
		Data data = new Data();
		data.setBodyHash(new byte[]{70, 79, 79});  // "FOO", base64 encoded value is "Rk9P"
		data.setSize(20);
		data.setBody(new byte[]{66, 65, 82});  // "BAR", base64 encoded value is "QkFS"
		return data;
	}

	private SharedNotebookRecipientSettings getSharedNotebookRecipientSettings() {
		SharedNotebookRecipientSettings setting = new SharedNotebookRecipientSettings();
		setting.setReminderNotifyEmail(true);
		setting.setReminderNotifyInApp(true);
		return setting;
	}

	private SharedNotebook getSharedNotebook1() {
		SharedNotebook sharedNotebook1 = new SharedNotebook();
		sharedNotebook1.setId(1001);
		sharedNotebook1.setUserId(1002);
		sharedNotebook1.setNotebookGuid("SHARED_NOTEBOOK_1_GUID");
		sharedNotebook1.setEmail("SHARED_NOTEBOOK_1_EMAIL");
		sharedNotebook1.setNotebookModifiable(true);
		sharedNotebook1.setRequireLogin(true);
		sharedNotebook1.setServiceCreated(1003);
		sharedNotebook1.setServiceUpdated(1004);
		sharedNotebook1.setShareKey("SHARED_NOTEBOOK_1_SHARE_KEY");
		sharedNotebook1.setUsername("SHARED_NOTEBOOK_1_USERNAME");
		sharedNotebook1.setPrivilege(SharedNotebookPrivilegeLevel.FULL_ACCESS);
		sharedNotebook1.setAllowPreview(true);
		sharedNotebook1.setRecipientSettings(getSharedNotebookRecipientSettings());
		return sharedNotebook1;
	}

	private SharedNotebook getSharedNotebook2() {
		SharedNotebook sharedNotebook2 = new SharedNotebook();
		sharedNotebook2.setId(2001);
		sharedNotebook2.setUserId(2002);
		sharedNotebook2.setNotebookGuid("SHARED_NOTEBOOK_2_GUID");
		sharedNotebook2.setEmail("SHARED_NOTEBOOK_2_EMAIL");
		sharedNotebook2.setNotebookModifiable(true);
		sharedNotebook2.setRequireLogin(true);
		sharedNotebook2.setServiceCreated(2003);
		sharedNotebook2.setServiceUpdated(2004);
		sharedNotebook2.setShareKey("SHARED_NOTEBOOK_2_SHARE_KEY");
		sharedNotebook2.setUsername("SHARED_NOTEBOOK_2_USERNAME");
		sharedNotebook2.setPrivilege(SharedNotebookPrivilegeLevel.GROUP);
		sharedNotebook2.setAllowPreview(true);
		sharedNotebook2.setRecipientSettings(getSharedNotebookRecipientSettings());
		return sharedNotebook2;
	}

	private Notebook getNotebookFull() {
		Publishing publishing = new Publishing();
		publishing.setUri("URI");
		publishing.setOrder(NoteSortOrder.RELEVANCE);
		publishing.setAscending(true);
		publishing.setPublicDescription("PUBLIC_DESCRIPTION");

		List<SharedNotebook> sharedNotebooks = new ArrayList<SharedNotebook>();
		sharedNotebooks.add(getSharedNotebook1());
		sharedNotebooks.add(getSharedNotebook2());

		BusinessNotebook businessNotebook = new BusinessNotebook();
		businessNotebook.setNotebookDescription("BUSINESS_NOTEBOOK_DESC");
		businessNotebook.setPrivilege(SharedNotebookPrivilegeLevel.READ_NOTEBOOK);
		businessNotebook.setRecommended(true);

		UserAttributes userAttributes = new UserAttributes();
		userAttributes.setDefaultLocationName("USERATTR_DEFAULT_LOCATION_NAME");
		userAttributes.setDefaultLatitude(11);
		userAttributes.setDefaultLongitude(22);
		userAttributes.setPreactivation(true);
		userAttributes.setViewedPromotions(Arrays.asList("USERATTR_VIEWED_PROM_1", "USERATTR_VIEWED_PROM_2"));
		userAttributes.setIncomingEmailAddress("USERATTR_INCOMING_EMAIL_ADDRESS");
		userAttributes.setRecentMailedAddresses(Arrays.asList("USERATTR_RECENT_ADDR_1", "USERATTR_RECENT_ADDR_2"));
		userAttributes.setComments("USERATTR_COMMENTS");
		userAttributes.setDateAgreedToTermsOfService(33);
		userAttributes.setMaxReferrals(44);
		userAttributes.setReferralCount(55);
		userAttributes.setRefererCode("USERATTR_REFFERER_CODE");
		userAttributes.setSentEmailDate(66);
		userAttributes.setSentEmailCount(77);
		userAttributes.setDailyEmailLimit(88);
		userAttributes.setEmailOptOutDate(99);
		userAttributes.setPartnerEmailOptInDate(11);
		userAttributes.setPreferredLanguage("USERATTR_PREFERRED_LANG");
		userAttributes.setPreferredCountry("USERATTR_PREFERRED_COUNTRY");
		userAttributes.setClipFullPage(true);
		userAttributes.setTwitterUserName("USERATTR_TWITTER_USERNAME");
		userAttributes.setTwitterId("USERATTR_TWITTER_ID");
		userAttributes.setGroupName("USERATTR_GROUPNAME");
		userAttributes.setRecognitionLanguage("USERATTR_RECOGNITION_LANG");
		userAttributes.setReferralProof("USERATTR_REFERRAL_PROOF");
		userAttributes.setEducationalDiscount(true);
		userAttributes.setBusinessAddress("USERATTR_BUSINESS_ADDRESS");
		userAttributes.setHideSponsorBilling(true);
		userAttributes.setTaxExempt(true);
		userAttributes.setUseEmailAutoFiling(true);
		userAttributes.setReminderEmailConfig(ReminderEmailConfig.SEND_DAILY_EMAIL);

		Accounting accounting = new Accounting();
		accounting.setUploadLimit(400);
		accounting.setUploadLimitEnd(401);
		accounting.setUploadLimitNextMonth(402);
		accounting.setPremiumServiceStatus(PremiumOrderStatus.PENDING);
		accounting.setPremiumOrderNumber("ACCOUNTING_PREMIUM_ORDER_NUMBER");
		accounting.setPremiumCommerceService("ACCOUNTING_PREMIUM_COMMERCE_SERVICE");
		accounting.setPremiumServiceStart(403);
		accounting.setPremiumServiceSKU("ACCOUNTING_SKU");
		accounting.setLastSuccessfulCharge(404);
		accounting.setLastFailedCharge(405);
		accounting.setLastFailedChargeReason("ACCOUNTING_LAST_FAILED_CHARGE_REASON");
		accounting.setNextPaymentDue(406);
		accounting.setPremiumLockUntil(407);
		accounting.setUpdated(408);
		accounting.setPremiumSubscriptionNumber("ACCOUNTING_PREMIUM_SUBSCRIPTION_NUMBER");
		accounting.setLastRequestedCharge(409);
		accounting.setCurrency("ACCOUNTING_CURRENCY");
		accounting.setUnitPrice(410);
		accounting.setBusinessId(411);
		accounting.setBusinessName("ACCOUNTING_BUSINESS_NAME");
		accounting.setBusinessRole(BusinessUserRole.ADMIN);
		accounting.setUnitDiscount(412);
		accounting.setNextChargeDate(413);

		PremiumInfo premiumInfo = new PremiumInfo();
		premiumInfo.setCurrentTime(500);
		premiumInfo.setPremium(true);
		premiumInfo.setPremiumRecurring(true);
		premiumInfo.setPremiumExpirationDate(501);
		premiumInfo.setPremiumExtendable(true);
		premiumInfo.setPremiumPending(true);
		premiumInfo.setPremiumCancellationPending(true);
		premiumInfo.setCanPurchaseUploadAllowance(true);
		premiumInfo.setSponsoredGroupName("PREMIUMINFO_SPONSORED_GROUPNAME");
		premiumInfo.setSponsoredGroupRole(SponsoredGroupRole.GROUP_MEMBER);
		premiumInfo.setPremiumUpgradable(true);

		BusinessUserInfo businessUserInfo = new BusinessUserInfo();
		businessUserInfo.setBusinessId(600);
		businessUserInfo.setBusinessName("BUSINESS_USER_INFO_BUSINESS_NAME");
		businessUserInfo.setRole(BusinessUserRole.ADMIN);
		businessUserInfo.setEmail("BUSINESS_USER_INFO_EMAIL");

		User contact = new User();
		contact.setId(5000);
		contact.setUsername("CONTACT_USERNAME");
		contact.setEmail("CONTACT_EMAIL");
		contact.setName("CONTACT_NAME");
		contact.setTimezone("CONTACT_TIMEZONE");
		contact.setPrivilege(PrivilegeLevel.VIP);
		contact.setCreated(5001);
		contact.setUpdated(5002);
		contact.setDeleted(5003);
		contact.setActive(true);
		contact.setShardId("CONTACT_SHARDID");
		contact.setAttributes(userAttributes);
		contact.setAccounting(accounting);
		contact.setPremiumInfo(premiumInfo);
		contact.setBusinessUserInfo(businessUserInfo);

		NotebookRestrictions restrictions = new NotebookRestrictions();
		restrictions.setNoReadNotes(true);
		restrictions.setNoCreateNotes(true);
		restrictions.setNoUpdateNotes(true);
		restrictions.setNoExpungeNotes(true);
		restrictions.setNoShareNotes(true);
		restrictions.setNoEmailNotes(true);
		restrictions.setNoSendMessageToRecipients(true);
		restrictions.setNoUpdateNotebook(true);
		restrictions.setNoExpungeNotebook(true);
		restrictions.setNoSetDefaultNotebook(true);
		restrictions.setNoSetNotebookStack(true);
		restrictions.setNoPublishToPublic(true);
		restrictions.setNoPublishToBusinessLibrary(true);
		restrictions.setNoCreateTags(true);
		restrictions.setNoUpdateTags(true);
		restrictions.setNoExpungeTags(true);
		restrictions.setNoSetParentTag(true);
		restrictions.setNoCreateSharedNotebooks(true);
		restrictions.setUpdateWhichSharedNotebookRestrictions(SharedNotebookInstanceRestrictions.NO_SHARED_NOTEBOOKS);
		restrictions.setExpungeWhichSharedNotebookRestrictions(SharedNotebookInstanceRestrictions.ONLY_JOINED_OR_PREVIEW);


		Notebook notebook = new Notebook();
		notebook.setGuid("GUID");
		notebook.setName("NAME");
		notebook.setUpdateSequenceNum(100);
		notebook.setDefaultNotebook(true);
		notebook.setServiceCreated(200);
		notebook.setServiceUpdated(300);
		notebook.setPublishing(publishing);
		notebook.setPublished(true);
		notebook.setStack("STACK");
		notebook.setSharedNotebookIds(Arrays.asList(10L, 20L, 30L));
		notebook.setSharedNotebooks(sharedNotebooks);
		notebook.setBusinessNotebook(businessNotebook);
		notebook.setContact(contact);
		notebook.setRestrictions(restrictions);

		return notebook;
	}
}
