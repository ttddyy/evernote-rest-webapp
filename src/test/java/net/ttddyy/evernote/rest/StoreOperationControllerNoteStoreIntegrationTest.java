package net.ttddyy.evernote.rest;

import com.evernote.edam.notestore.*;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebookRecipientSettings;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static net.ttddyy.evernote.rest.TestDomainUtils.*;
import static org.mockito.Mockito.verify;

/**
 * Test class for NoteStore related operations on {@link StoreOperationController}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StoreOperationControllerNoteStoreIntegrationTest extends AbstractStoreOperationControllerIntegrationTest {

	@Test
	public void testGetSyncState() throws Exception {
		performRequest("/noteStore/getSyncState", "{}");
		verify(noteStoreOperations).getSyncState();
	}

	@Test
	public void testGetSyncStateWithMetrics() throws Exception {
		performRequest("/noteStore/getSyncStateWithMetrics", "{\"clientMetrics\": { \"sessions\":50 } }");
		ClientUsageMetrics clientMetrics = new ClientUsageMetrics();
		clientMetrics.setSessions(50);
		verify(noteStoreOperations).getSyncStateWithMetrics(clientMetrics);
	}

	@Test
	public void testGetSyncChunk() throws Exception {
		performRequest("/noteStore/getSyncChunk", "{\"afterUSN\":100, \"maxEntries\":200, \"fullSyncOnly\":true}");
		verify(noteStoreOperations).getSyncChunk(100, 200, true);
	}

	@Test
	public void testGetFilteredSyncChunk() throws Exception {
		String json = readJson("getFilteredSyncChunk.json");
		performRequest("/noteStore/getFilteredSyncChunk", json);
		SyncChunkFilter filter = new SyncChunkFilter();
		filter.setIncludeNotes(true);
		filter.setIncludeNoteResources(true);
		filter.setIncludeNoteAttributes(true);
		filter.setIncludeNotebooks(true);
		filter.setIncludeTags(true);
		filter.setIncludeSearches(true);
		filter.setIncludeResources(true);
		filter.setIncludeLinkedNotebooks(true);
		filter.setIncludeExpunged(true);
		filter.setIncludeNoteApplicationDataFullMap(true);
		filter.setIncludeResourceApplicationDataFullMap(true);
		filter.setIncludeNoteResourceApplicationDataFullMap(true);
		filter.setRequireNoteContentClass("FILTER_NOTE_CONTENT_CLASS");
		verify(noteStoreOperations).getFilteredSyncChunk(30, 40, filter);
	}

	@Test
	public void testGetLinkedNotebookSyncState() throws Exception {
		String json = readJson("linkedNotebook.json");
		performRequest("/noteStore/getLinkedNotebookSyncState", json);
		verify(noteStoreOperations).getLinkedNotebookSyncState(getLinkedNotebook());
	}

	@Test
	public void testGetLinkedNotebookSyncChunk() throws Exception {
		String json = readJson("getLinkedNotebookSyncChunk.json");
		performRequest("/noteStore/getLinkedNotebookSyncChunk", json);
		verify(noteStoreOperations).getLinkedNotebookSyncChunk(getLinkedNotebook(), 10, 20, true);
	}

	@Test
	public void testListNotebooks() throws Exception {
		performRequest("/noteStore/listNotebooks", "{}");
		verify(noteStoreOperations).listNotebooks();
	}

	@Test
	public void testGetNotebook() throws Exception {
		performRequest("/noteStore/getNotebook", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNotebook("GUID_FOO");
	}

	@Test
	public void testGetDefaultNotebook() throws Exception {
		performRequest("/noteStore/getDefaultNotebook", "{}");
		verify(noteStoreOperations).getDefaultNotebook();
	}

	@Test
	public void testCreateNotebook() throws Exception {
		String json = readJson("notebook_full.json");
		performRequest("/noteStore/createNotebook", json);
		Notebook notebook = getNotebookFull();
		verify(noteStoreOperations).createNotebook(notebook);
	}

	@Test
	public void testUpdateNotebook() throws Exception {
		String json = readJson("notebook_full.json");
		performRequest("/noteStore/updateNotebook", json);
		Notebook notebook = getNotebookFull();
		verify(noteStoreOperations).updateNotebook(notebook);
	}

	@Test
	public void testExpungeNotebook() throws Exception {
		performRequest("/noteStore/expungeNotebook", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeNotebook("GUID_FOO");
	}

	@Test
	public void testListTags() throws Exception {
		performRequest("/noteStore/listTags", "{}");
		verify(noteStoreOperations).listTags();
	}

	@Test
	public void testListTagsByNotebook() throws Exception {
		performRequest("/noteStore/listTagsByNotebook", "{\"notebookGuid\":\"NOTEBOOK_GUID_FOO\"}");
		verify(noteStoreOperations).listTagsByNotebook("NOTEBOOK_GUID_FOO");
	}

	@Test
	public void testGetTag() throws Exception {
		performRequest("/noteStore/getTag", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getTag("GUID_FOO");
	}

	@Test
	public void testCreateTag() throws Exception {
		String json = readJson("tag.json");
		performRequest("/noteStore/createTag", json);
		verify(noteStoreOperations).createTag(getTag());
	}

	@Test
	public void testUpdateTag() throws Exception {
		String json = readJson("tag.json");
		performRequest("/noteStore/updateTag", json);
		verify(noteStoreOperations).updateTag(getTag());
	}

	@Test
	public void testUntagAll() throws Exception {
		performRequest("/noteStore/untagAll", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).untagAll("GUID_FOO");
	}

	@Test
	public void testExpungeTag() throws Exception {
		performRequest("/noteStore/expungeTag", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeTag("GUID_FOO");
	}

	@Test
	public void testListSearches() throws Exception {
		performRequest("/noteStore/listSearches", "{}");
		verify(noteStoreOperations).listSearches();
	}

	@Test
	public void testGetSearch() throws Exception {
		performRequest("/noteStore/getSearch", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getSearch("GUID_FOO");
	}

	@Test
	public void testCreateSearch() throws Exception {
		String json = readJson("savedSearch.json");
		performRequest("/noteStore/createSearch", json);
		verify(noteStoreOperations).createSearch(getSavedSearch());
	}

	@Test
	public void testUpdateSearch() throws Exception {
		String json = readJson("savedSearch.json");
		performRequest("/noteStore/updateSearch", json);
		verify(noteStoreOperations).updateSearch(getSavedSearch());
	}

	@Test
	public void testExpungeSearch() throws Exception {
		performRequest("/noteStore/expungeSearch", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeSearch("GUID_FOO");
	}

	@Test
	public void testFindNotes() throws Exception {
		String json = readJson("findNotes.json");
		performRequest("/noteStore/findNotes", json);
		verify(noteStoreOperations).findNotes(getNoteFilter(), 20, 30);
	}

	@Test
	public void testFindNoteOffset() throws Exception {
		String json = readJson("findNoteOffset.json");
		performRequest("/noteStore/findNoteOffset", json);
		verify(noteStoreOperations).findNoteOffset(getNoteFilter(), "GUID");
	}

	@Test
	public void testFindNotesMetadata() throws Exception {
		String json = readJson("findNotesMetadata.json");
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
	public void testFindNoteCounts() throws Exception {
		String json = readJson("findNoteCounts.json");
		performRequest("/noteStore/findNoteCounts", json);
		verify(noteStoreOperations).findNoteCounts(getNoteFilter(), true);
	}

	@Test
	public void testGetNote() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                                        ");
		sb.append("    \"guid\":\"GUID\",                   ");
		sb.append("    \"withContent\":true,                ");
		sb.append("    \"withResourcesData\":true,          ");
		sb.append("    \"withResourcesRecognition\":true,   ");
		sb.append("    \"withResourcesAlternateData\":true  ");
		sb.append("}                                        ");
		performRequest("/noteStore/getNote", sb.toString());
		verify(noteStoreOperations).getNote("GUID", true, true, true, true);
	}

	@Test
	public void testGetNoteApplicationData() throws Exception {
		performRequest("/noteStore/getNoteApplicationData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNoteApplicationData("GUID_FOO");
	}

	@Test
	public void testGetNoteApplicationDataEntry() throws Exception {
		performRequest("/noteStore/getNoteApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\"}");
		verify(noteStoreOperations).getNoteApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testSetNoteApplicationDataEntry() throws Exception {
		String json = "{\"guid\":\"GUID\", \"key\":\"KEY\", \"value\":\"VALUE\"}";
		performRequest("/noteStore/setNoteApplicationDataEntry", json);
		verify(noteStoreOperations).setNoteApplicationDataEntry("GUID", "KEY", "VALUE");
	}

	@Test
	public void testUnsetNoteApplicationDataEntry() throws Exception {
		performRequest("/noteStore/unsetNoteApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\"}");
		verify(noteStoreOperations).unsetNoteApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testGetNoteContent() throws Exception {
		performRequest("/noteStore/getNoteContent", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNoteContent("GUID_FOO");
	}

	@Test
	public void testGetNoteSearchText() throws Exception {
		String json = "{\"guid\":\"GUID\", \"noteOnly\":true, \"tokenizeForIndexing\":true}";
		performRequest("/noteStore/getNoteSearchText", json);
		verify(noteStoreOperations).getNoteSearchText("GUID", true, true);
	}

	@Test
	public void testGetResourceSearchText() throws Exception {
		performRequest("/noteStore/getResourceSearchText", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceSearchText("GUID_FOO");
	}

	@Test
	public void testGetNoteTagNames() throws Exception {
		performRequest("/noteStore/getNoteTagNames", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getNoteTagNames("GUID_FOO");
	}

	@Test
	public void testCreateNote() throws Exception {
		String json = readJson("note.json");
		performRequest("/noteStore/createNote", json);
		verify(noteStoreOperations).createNote(getNote());
	}

	@Test
	public void testUpdateNote() throws Exception {
		String json = readJson("note.json");
		performRequest("/noteStore/updateNote", json);
		verify(noteStoreOperations).updateNote(getNote());
	}

	@Test
	public void testDeleteNote() throws Exception {
		performRequest("/noteStore/deleteNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).deleteNote("GUID_FOO");
	}

	@Test
	public void testExpungeNote() throws Exception {
		performRequest("/noteStore/expungeNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeNote("GUID_FOO");
	}

	@Test
	public void testExpungeNotes() throws Exception {
		performRequest("/noteStore/expungeNotes", "{\"noteGuids\":[\"FOO\", \"BAR\"]}");
		verify(noteStoreOperations).expungeNotes(Arrays.asList("FOO", "BAR"));
	}

	@Test
	public void testExpungeInactiveNotes() throws Exception {
		performRequest("/noteStore/expungeInactiveNotes", "{}");
		verify(noteStoreOperations).expungeInactiveNotes();
	}

	@Test
	public void testCopyNote() throws Exception {
		performRequest("/noteStore/copyNote", "{\"noteGuid\":\"NOTE_GUID\", \"toNotebookGuid\":\"TO_NOTEBOOK_GUID\"}");
		verify(noteStoreOperations).copyNote("NOTE_GUID", "TO_NOTEBOOK_GUID");
	}

	@Test
	public void testListNoteVersions() throws Exception {
		performRequest("/noteStore/listNoteVersions", "{\"noteGuid\":\"NOTE_GUID_FOO\"}");
		verify(noteStoreOperations).listNoteVersions("NOTE_GUID_FOO");
	}

	@Test
	public void testGetNoteVersion() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                                        ");
		sb.append("    \"noteGuid\":\"NOTE_GUID\",          ");
		sb.append("    \"updateSequenceNum\":100,           ");
		sb.append("    \"withResourcesData\":true,          ");
		sb.append("    \"withResourcesRecognition\":true,   ");
		sb.append("    \"withResourcesAlternateData\":true  ");
		sb.append("}                                        ");
		performRequest("/noteStore/getNoteVersion", sb.toString());
		verify(noteStoreOperations).getNoteVersion("NOTE_GUID", 100, true, true, true);
	}

	@Test
	public void testGetResource() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                              ");
		sb.append("    \"guid\":\"GUID\",         ");
		sb.append("    \"withData\":true,         ");
		sb.append("    \"withRecognition\":true,  ");
		sb.append("    \"withAttributes\":true,   ");
		sb.append("    \"withAlternateData\":true ");
		sb.append("}                              ");
		performRequest("/noteStore/getResource", sb.toString());
		verify(noteStoreOperations).getResource("GUID", true, true, true, true);
	}

	@Test
	public void testGetResourceApplicationData() throws Exception {
		performRequest("/noteStore/getResourceApplicationData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceApplicationData("GUID_FOO");
	}

	@Test
	public void testGetResourceApplicationDataEntry() throws Exception {
		performRequest("/noteStore/getResourceApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\" }");
		verify(noteStoreOperations).getResourceApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testSetResourceApplicationDataEntry() throws Exception {
		String json = "{\"guid\":\"GUID\", \"key\":\"KEY\", \"value\":\"VALUE\" }";
		performRequest("/noteStore/setResourceApplicationDataEntry", json);
		verify(noteStoreOperations).setResourceApplicationDataEntry("GUID", "KEY", "VALUE");
	}

	@Test
	public void testUnsetResourceApplicationDataEntry() throws Exception {
		performRequest("/noteStore/unsetResourceApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\" }");
		verify(noteStoreOperations).unsetResourceApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testUpdateResource() throws Exception {
		String json = readJson("updateResource.json");
		performRequest("/noteStore/updateResource", json);
		verify(noteStoreOperations).updateResource(getResource());
	}

	@Test
	public void testGetResourceData() throws Exception {
		performRequest("/noteStore/getResourceData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceData("GUID_FOO");
	}

	@Test
	public void testGetResourceByHash() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                               ");
		sb.append("    \"noteGuid\": \"GUID\",     ");
		sb.append("    \"contentHash\": \"Rk9P\",  ");  // base64 encoded value of "FOO" => 70,79,79 in byte
		sb.append("    \"withData\": true,         ");
		sb.append("    \"withRecognition\": true,  ");
		sb.append("    \"withAlternateData\": true ");
		sb.append("}                               ");
		performRequest("/noteStore/getResourceByHash", sb.toString());
		verify(noteStoreOperations).getResourceByHash("GUID", getByteFoo(), true, true, true);
	}

	@Test
	public void testGetResourceRecognition() throws Exception {
		performRequest("/noteStore/getResourceRecognition", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceRecognition("GUID_FOO");
	}

	@Test
	public void testGetResourceAlternateData() throws Exception {
		performRequest("/noteStore/getResourceAlternateData", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceAlternateData("GUID_FOO");
	}

	@Test
	public void testGetResourceAttributes() throws Exception {
		performRequest("/noteStore/getResourceAttributes", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).getResourceAttributes("GUID_FOO");
	}

	@Test
	public void testGetPublicNotebook() throws Exception {
		performRequest("/noteStore/getPublicNotebook", "{\"userId\": 100, \"publicUri\":\"PUBLIC_URI\"}");
		verify(noteStoreOperations).getPublicNotebook(100, "PUBLIC_URI");
	}

	@Test
	public void testCreateSharedNotebook() throws Exception {
		String json = readJson("sharedNotebook1.json");
		performRequest("/noteStore/createSharedNotebook", json);
		verify(noteStoreOperations).createSharedNotebook(getSharedNotebook1());
	}

	@Test
	public void testUpdateSharedNotebook() throws Exception {
		String json = readJson("sharedNotebook1.json");
		performRequest("/noteStore/updateSharedNotebook", json);
		verify(noteStoreOperations).updateSharedNotebook(getSharedNotebook1());
	}

	@Test
	public void testSendMessageToSharedNotebookMembers() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                                 ");
		sb.append("    \"notebookGuid\": \"GUID\",   ");
		sb.append("    \"messageText\": \"MESSAGE\", ");
		sb.append("    \"recipients\": [             ");
		sb.append("         \"FOO\", \"BAR\"         ");
		sb.append("    ]                             ");
		sb.append("}                                 ");
		performRequest("/noteStore/sendMessageToSharedNotebookMembers", sb.toString());
		verify(noteStoreOperations).sendMessageToSharedNotebookMembers("GUID", "MESSAGE", Arrays.asList("FOO", "BAR"));
	}

	@Test
	public void testListSharedNotebooks() throws Exception {
		performRequest("/noteStore/listSharedNotebooks", "{}");
		verify(noteStoreOperations).listSharedNotebooks();
	}

	@Test
	public void testExpungeSharedNotebooks() throws Exception {
		performRequest("/noteStore/expungeSharedNotebooks", "{\"sharedNotebookIds\":[1,2,3]}");
		verify(noteStoreOperations).expungeSharedNotebooks(Arrays.asList(1L, 2L, 3L));
	}

	@Test
	public void testCreateLinkedNotebook() throws Exception {
		String json = readJson("linkedNotebook.json");
		performRequest("/noteStore/createLinkedNotebook", json);
		verify(noteStoreOperations).createLinkedNotebook(getLinkedNotebook());
	}

	@Test
	public void testUpdateLinkedNotebook() throws Exception {
		String json = readJson("linkedNotebook.json");
		performRequest("/noteStore/updateLinkedNotebook", json);
		verify(noteStoreOperations).updateLinkedNotebook(getLinkedNotebook());
	}

	@Test
	public void testListLinkedNotebooks() throws Exception {
		performRequest("/noteStore/listLinkedNotebooks", "{}");
		verify(noteStoreOperations).listLinkedNotebooks();
	}

	@Test
	public void testExpungeLinkedNotebook() throws Exception {
		performRequest("/noteStore/expungeLinkedNotebook", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).expungeLinkedNotebook("GUID_FOO");
	}

	@Test
	public void testAuthenticateToSharedNotebook() throws Exception {
		performRequest("/noteStore/authenticateToSharedNotebook", "{\"shareKey\":\"SHARE_KEY_FOO\"}");
		verify(noteStoreOperations).authenticateToSharedNotebook("SHARE_KEY_FOO");
	}

	@Test
	public void testNoteStorGetSharedNotebookByAuth() throws Exception {
		performRequest("/noteStore/getSharedNotebookByAuth", "{}");
		verify(noteStoreOperations).getSharedNotebookByAuth();
	}

	@Test
	public void testEmailNote() throws Exception {
		String json = readJson("emailNote.json");
		performRequest("/noteStore/emailNote", json);

		NoteEmailParameters parameters = new NoteEmailParameters();
		parameters.setGuid("NOTE_EMAIL_PARAM_GUID");
		parameters.setNote(getNote());
		parameters.setToAddresses(Arrays.asList("NOTE_EMAIL_PARAM_TO_ADDR_FOO", "NOTE_EMAIL_PARAM_TO_ADDR_BAR"));
		parameters.setCcAddresses(Arrays.asList("NOTE_EMAIL_PARAM_CC_ADDR_FOO", "NOTE_EMAIL_PARAM_CC_ADDR_BAR"));
		parameters.setSubject("NOTE_EMAIL_PARAM_SUBJECT");
		parameters.setMessage("NOTE_EMAIL_PARAM_MESSAGE");
		verify(noteStoreOperations).emailNote(parameters);
	}

	@Test
	public void testShareNote() throws Exception {
		performRequest("/noteStore/shareNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).shareNote("GUID_FOO");
	}

	@Test
	public void testStopSharingNote() throws Exception {
		performRequest("/noteStore/stopSharingNote", "{\"guid\":\"GUID_FOO\"}");
		verify(noteStoreOperations).stopSharingNote("GUID_FOO");
	}

	@Test
	public void testAuthenticateToSharedNote() throws Exception {
		String json = "{\"guid\":\"GUID\", \"noteKey\":\"NOTE_KEY\", \"authenticationToken\":\"AUTHENTICATION_TOKEN\" }";
		performRequest("/noteStore/authenticateToSharedNote", json);
		verify(noteStoreOperations).authenticateToSharedNote("GUID", "NOTE_KEY", "AUTHENTICATION_TOKEN");
	}

	@Test
	public void testFindRelated() throws Exception {
		String json = readJson("findRelated.json");
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

	@Test
	public void testSetSharedNotebookRecipientSettings() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                                        ");
		sb.append("    \"authenticationToken\": \"TOKEN\",  ");
		sb.append("    \"sharedNotebookId\": 111,           ");
		sb.append("    \"recipientSettings\": {             ");
		sb.append("       \"reminderNotifyEmail\": true,    ");
		sb.append("       \"reminderNotifyInApp\": true     ");
		sb.append("    }                                    ");
		sb.append("}                                        ");
		performRequest("/noteStore/setSharedNotebookRecipientSettings", sb.toString());
		SharedNotebookRecipientSettings recipientSettings = new SharedNotebookRecipientSettings();
		recipientSettings.setReminderNotifyEmail(true);
		recipientSettings.setReminderNotifyInApp(true);
		verify(noteStoreOperations).setSharedNotebookRecipientSettings("TOKEN", 111, recipientSettings);
	}


	private String readJson(String filename) throws IOException {
		String location = "classpath:/input/" + filename;
		org.springframework.core.io.Resource resource = resourceLoader.getResource(location);
		return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
	}

}
