package net.ttddyy.evernote.rest;

import com.evernote.edam.notestore.*;
import com.evernote.edam.type.*;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ttddyy.evernote.rest.ResultActionsUtils.*;
import static net.ttddyy.evernote.rest.TestDomainUtils.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for NoteStore related operations on {@link StoreOperationController}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StoreOperationControllerNoteStoreIntegrationTest extends AbstractStoreOperationControllerIntegrationTest {

	@Test
	public void testGetSyncState() throws Exception {
		when(noteStoreOperations.getSyncState()).thenReturn(getSyncState());
		ResultActions resultActions = performRequest("/noteStore/getSyncState", "{}");
		verifySyncState(resultActions);
		verify(noteStoreOperations).getSyncState();
	}

	@Test
	public void testGetSyncStateWithMetrics() throws Exception {
		ClientUsageMetrics clientMetrics = new ClientUsageMetrics();
		clientMetrics.setSessions(50);
		when(noteStoreOperations.getSyncStateWithMetrics(clientMetrics)).thenReturn(getSyncState());

		String json = "{\"clientMetrics\": { \"sessions\":50 } }";
		ResultActions resultActions = performRequest("/noteStore/getSyncStateWithMetrics", json);
		verifySyncState(resultActions);
		verify(noteStoreOperations).getSyncStateWithMetrics(clientMetrics);
	}

	@Test
	public void testGetSyncChunk() throws Exception {
		when(noteStoreOperations.getSyncChunk(100, 200, true)).thenReturn(getSyncChunk());

		StringBuilder sb = new StringBuilder();
		sb.append("{                          ");
		sb.append("    \"afterUSN\":\"100\",  ");
		sb.append("    \"maxEntries\":200,    ");
		sb.append("    \"fullSyncOnly\":true  ");
		sb.append("}                          ");
		ResultActions resultActions = performRequest("/noteStore/getSyncChunk", sb.toString());

		verifySyncChunk(resultActions);
		verify(noteStoreOperations).getSyncChunk(100, 200, true);
	}

	@Test
	public void testGetFilteredSyncChunk() throws Exception {

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

		when(noteStoreOperations.getFilteredSyncChunk(30, 40, filter)).thenReturn(getSyncChunk());

		String json = readJson("getFilteredSyncChunk.json");
		ResultActions resultActions = performRequest("/noteStore/getFilteredSyncChunk", json);

		verifySyncChunk(resultActions);
		verify(noteStoreOperations).getFilteredSyncChunk(30, 40, filter);
	}

	@Test
	public void testGetLinkedNotebookSyncState() throws Exception {
		when(noteStoreOperations.getLinkedNotebookSyncState(getLinkedNotebook())).thenReturn(getSyncState());
		String json = readJson("linkedNotebook.json");
		ResultActions resultActions = performRequest("/noteStore/getLinkedNotebookSyncState", json);
		verifySyncState(resultActions);
		verify(noteStoreOperations).getLinkedNotebookSyncState(getLinkedNotebook());
	}

	@Test
	public void testGetLinkedNotebookSyncChunk() throws Exception {
		when(noteStoreOperations.getLinkedNotebookSyncChunk(getLinkedNotebook(), 10, 20, true)).thenReturn(getSyncChunk());
		String json = readJson("getLinkedNotebookSyncChunk.json");
		ResultActions resultActions = performRequest("/noteStore/getLinkedNotebookSyncChunk", json);
		verifySyncChunk(resultActions);
		verify(noteStoreOperations).getLinkedNotebookSyncChunk(getLinkedNotebook(), 10, 20, true);
	}

	@Test
	public void testListNotebooks() throws Exception {
		when(noteStoreOperations.listNotebooks()).thenReturn(Arrays.asList(getNotebook(), getNotebook()));
		ResultActions resultActions = performRequest("/noteStore/listNotebooks", "{}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifyNotebook(resultActions, "$[0]");
		verifyNotebook(resultActions, "$[1]");
		verify(noteStoreOperations).listNotebooks();
	}

	@Test
	public void testGetNotebook() throws Exception {
		when(noteStoreOperations.getNotebook("GUID_FOO")).thenReturn(getNotebook());
		ResultActions resultActions = performRequest("/noteStore/getNotebook", "{\"guid\":\"GUID_FOO\"}");
		verifyNotebook(resultActions);
		verify(noteStoreOperations).getNotebook("GUID_FOO");
	}

	@Test
	public void testGetDefaultNotebook() throws Exception {
		when(noteStoreOperations.getDefaultNotebook()).thenReturn(getNotebook());
		ResultActions resultActions = performRequest("/noteStore/getDefaultNotebook", "{}");
		verifyNotebook(resultActions);
		verify(noteStoreOperations).getDefaultNotebook();
	}

	@Test
	public void testCreateNotebook() throws Exception {
		Notebook notebook = getNotebook();
		when(noteStoreOperations.createNotebook(notebook)).thenReturn(notebook);
		String json = readJson("notebook_full.json");
		ResultActions resultActions = performRequest("/noteStore/createNotebook", json);
		verifyNotebook(resultActions);
		verify(noteStoreOperations).createNotebook(notebook);
	}

	@Test
	public void testUpdateNotebook() throws Exception {
		Notebook notebook = getNotebook();
		when(noteStoreOperations.updateNotebook(notebook)).thenReturn(10);
		String json = readJson("notebook_full.json");
		performRequest("/noteStore/updateNotebook", json).andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).updateNotebook(notebook);
	}

	@Test
	public void testExpungeNotebook() throws Exception {
		when(noteStoreOperations.expungeNotebook("GUID_FOO")).thenReturn(10);
		performRequest("/noteStore/expungeNotebook", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).expungeNotebook("GUID_FOO");
	}

	@Test
	public void testListTags() throws Exception {
		when(noteStoreOperations.listTags()).thenReturn(Arrays.asList(getTag(), getTag()));
		ResultActions resultActions = performRequest("/noteStore/listTags", "{}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifyTag(resultActions, "$[0]");
		verifyTag(resultActions, "$[1]");
		verify(noteStoreOperations).listTags();
	}

	@Test
	public void testListTagsByNotebook() throws Exception {
		when(noteStoreOperations.listTagsByNotebook("NOTEBOOK_GUID_FOO")).thenReturn(Arrays.asList(getTag(), getTag()));
		ResultActions resultActions = performRequest("/noteStore/listTagsByNotebook", "{\"notebookGuid\":\"NOTEBOOK_GUID_FOO\"}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifyTag(resultActions, "$[0]");
		verifyTag(resultActions, "$[1]");
		verify(noteStoreOperations).listTagsByNotebook("NOTEBOOK_GUID_FOO");
	}

	@Test
	public void testGetTag() throws Exception {
		when(noteStoreOperations.getTag("GUID_FOO")).thenReturn(getTag());
		ResultActions resultActions = performRequest("/noteStore/getTag", "{\"guid\":\"GUID_FOO\"}");
		verifyTag(resultActions);
		verify(noteStoreOperations).getTag("GUID_FOO");
	}

	@Test
	public void testCreateTag() throws Exception {
		Tag tag = getTag();
		when(noteStoreOperations.createTag(tag)).thenReturn(tag);
		String json = readJson("tag.json");
		ResultActions resultActions = performRequest("/noteStore/createTag", json);
		verifyTag(resultActions);
		verify(noteStoreOperations).createTag(tag);
	}

	@Test
	public void testUpdateTag() throws Exception {
		Tag tag = getTag();
		when(noteStoreOperations.updateTag(tag)).thenReturn(10);
		String json = readJson("tag.json");
		performRequest("/noteStore/updateTag", json).andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).updateTag(tag);
	}

	@Test
	public void testUntagAll() throws Exception {
		performRequest("/noteStore/untagAll", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string(""));
		verify(noteStoreOperations).untagAll("GUID_FOO");
	}

	@Test
	public void testExpungeTag() throws Exception {
		when(noteStoreOperations.expungeTag("GUID_FOO")).thenReturn(10);
		performRequest("/noteStore/expungeTag", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).expungeTag("GUID_FOO");
	}

	@Test
	public void testListSearches() throws Exception {
		when(noteStoreOperations.listSearches()).thenReturn(Arrays.asList(getSavedSearch(), getSavedSearch()));
		ResultActions resultActions = performRequest("/noteStore/listSearches", "{}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifySavedSearch(resultActions, "$[0]");
		verifySavedSearch(resultActions, "$[1]");
		verify(noteStoreOperations).listSearches();
	}

	@Test
	public void testGetSearch() throws Exception {
		when(noteStoreOperations.getSearch("GUID_FOO")).thenReturn(getSavedSearch());
		ResultActions resultActions = performRequest("/noteStore/getSearch", "{\"guid\":\"GUID_FOO\"}");
		verifySavedSearch(resultActions);
		verify(noteStoreOperations).getSearch("GUID_FOO");
	}

	@Test
	public void testCreateSearch() throws Exception {
		SavedSearch savedSearch = getSavedSearch();
		when(noteStoreOperations.createSearch(savedSearch)).thenReturn(savedSearch);
		String json = readJson("savedSearch.json");
		ResultActions resultActions = performRequest("/noteStore/createSearch", json);
		verifySavedSearch(resultActions);
		verify(noteStoreOperations).createSearch(savedSearch);
	}

	@Test
	public void testUpdateSearch() throws Exception {
		SavedSearch savedSearch = getSavedSearch();
		when(noteStoreOperations.updateSearch(savedSearch)).thenReturn(10);
		String json = readJson("savedSearch.json");
		performRequest("/noteStore/updateSearch", json).andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).updateSearch(savedSearch);
	}

	@Test
	public void testExpungeSearch() throws Exception {
		when(noteStoreOperations.expungeSearch("GUID_FOO")).thenReturn(10);
		performRequest("/noteStore/expungeSearch", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).expungeSearch("GUID_FOO");
	}

	@Test
	public void testFindNotes() throws Exception {
		NoteFilter noteFilter = getNoteFilter();
		NoteList noteList = new NoteList();
		noteList.setStartIndex(10);
		noteList.setTotalNotes(20);
		noteList.setNotes(Arrays.asList(getNote(), getNote()));
		noteList.setStoppedWords(Arrays.asList("STOPPED_WORD_1", "STOPPED_WORD_2"));
		noteList.setSearchedWords(Arrays.asList("SEARCHED_WORD_1", "SEARCHED_WORD_2"));
		noteList.setUpdateCount(30);
		when(noteStoreOperations.findNotes(noteFilter, 20, 30)).thenReturn(noteList);

		String json = readJson("findNotes.json");
		ResultActions resultActions = performRequest("/noteStore/findNotes", json);

		resultActions.andExpect(jsonPath("$.notes").isArray());
		verifyNote(resultActions, "$.notes[0]");
		verifyNote(resultActions, "$.notes[1]");

		resultActions
				.andExpect(jsonPath("$.startIndex").value(10))
				.andExpect(jsonPath("$.totalNotes").value(20))
				.andExpect(jsonPath("$.stoppedWords").isArray())
				.andExpect(jsonPath("$.stoppedWords[0]").value("STOPPED_WORD_1"))
				.andExpect(jsonPath("$.stoppedWords[1]").value("STOPPED_WORD_2"))
				.andExpect(jsonPath("$.searchedWords").isArray())
				.andExpect(jsonPath("$.searchedWords[0]").value("SEARCHED_WORD_1"))
				.andExpect(jsonPath("$.searchedWords[1]").value("SEARCHED_WORD_2"))
				.andExpect(jsonPath("$.updateCount").value(30))
		;
		verify(noteStoreOperations).findNotes(noteFilter, 20, 30);
	}

	@Test
	public void testFindNoteOffset() throws Exception {
		when(noteStoreOperations.findNoteOffset(getNoteFilter(), "GUID")).thenReturn(10);
		String json = readJson("findNoteOffset.json");
		performRequest("/noteStore/findNoteOffset", json).andExpect(status().isOk()).andExpect(content().string("10"));
		verify(noteStoreOperations).findNoteOffset(getNoteFilter(), "GUID");
	}

	@Test
	public void testFindNotesMetadata() throws Exception {

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

		NotesMetadataList notesMetadataList = new NotesMetadataList();
		notesMetadataList.setStartIndex(10);
		notesMetadataList.setTotalNotes(20);
		notesMetadataList.setNotes(Arrays.asList(getNoteMetadata(), getNoteMetadata()));
		notesMetadataList.setStoppedWords(Arrays.asList("STOPPED_WORD_1", "STOPPED_WORD_2"));
		notesMetadataList.setSearchedWords(Arrays.asList("SEARCHED_WORD_1", "SEARCHED_WORD_2"));
		notesMetadataList.setUpdateCount(30);

		when(noteStoreOperations.findNotesMetadata(getNoteFilter(), 10, 20, resultSpec)).thenReturn(notesMetadataList);

		String json = readJson("findNotesMetadata.json");
		ResultActions resultActions = performRequest("/noteStore/findNotesMetadata", json);

		resultActions.andExpect(jsonPath("$.notes").isArray());
		verifyNoteMetadata(resultActions, "$.notes[0]");
		verifyNoteMetadata(resultActions, "$.notes[1]");

		resultActions
				.andExpect(jsonPath("$.startIndex").value(10))
				.andExpect(jsonPath("$.totalNotes").value(20))
				.andExpect(jsonPath("$.stoppedWords").isArray())
				.andExpect(jsonPath("$.stoppedWords[0]").value("STOPPED_WORD_1"))
				.andExpect(jsonPath("$.stoppedWords[1]").value("STOPPED_WORD_2"))
				.andExpect(jsonPath("$.searchedWords").isArray())
				.andExpect(jsonPath("$.searchedWords[0]").value("SEARCHED_WORD_1"))
				.andExpect(jsonPath("$.searchedWords[1]").value("SEARCHED_WORD_2"))
				.andExpect(jsonPath("$.updateCount").value(30))
		;
		verify(noteStoreOperations).findNotesMetadata(getNoteFilter(), 10, 20, resultSpec);
	}

	@Test
	public void testFindNoteCounts() throws Exception {
		NoteFilter noteFilter = getNoteFilter();

		Map<String, Integer> notebookCounts = new HashMap<String, Integer>();
		notebookCounts.put("NOTEBOOK_COUNTS_1", 15);
		notebookCounts.put("NOTEBOOK_COUNTS_2", 25);
		Map<String, Integer> tagCounts = new HashMap<String, Integer>();
		tagCounts.put("TAG_COUNTS_1", 27);
		tagCounts.put("TAG_COUNTS_2", 37);

		NoteCollectionCounts noteCollectionCounts = new NoteCollectionCounts();
		noteCollectionCounts.setNotebookCounts(notebookCounts);
		noteCollectionCounts.setTagCounts(tagCounts);
		noteCollectionCounts.setTrashCount(100);
		when(noteStoreOperations.findNoteCounts(noteFilter, true)).thenReturn(noteCollectionCounts);

		String json = readJson("findNoteCounts.json");
		ResultActions resultActions = performRequest("/noteStore/findNoteCounts", json);

		resultActions
				.andExpect(jsonPath("$.notebookCounts.NOTEBOOK_COUNTS_1").value(15))
				.andExpect(jsonPath("$.notebookCounts.NOTEBOOK_COUNTS_2").value(25))
				.andExpect(jsonPath("$.tagCounts.TAG_COUNTS_1").value(27))
				.andExpect(jsonPath("$.tagCounts.TAG_COUNTS_2").value(37))
				.andExpect(jsonPath("$.trashCount.").value(100))
		;
		verify(noteStoreOperations).findNoteCounts(noteFilter, true);
	}

	@Test
	public void testGetNote() throws Exception {

		when(noteStoreOperations.getNote("GUID", true, true, true, true)).thenReturn(getNote());

		StringBuilder sb = new StringBuilder();
		sb.append("{                                        ");
		sb.append("    \"guid\":\"GUID\",                   ");
		sb.append("    \"withContent\":true,                ");
		sb.append("    \"withResourcesData\":true,          ");
		sb.append("    \"withResourcesRecognition\":true,   ");
		sb.append("    \"withResourcesAlternateData\":true  ");
		sb.append("}                                        ");
		ResultActions resultActions = performRequest("/noteStore/getNote", sb.toString());

		verifyNote(resultActions);
		verify(noteStoreOperations).getNote("GUID", true, true, true, true);
	}

	@Test
	public void testGetNoteApplicationData() throws Exception {
		when(noteStoreOperations.getNoteApplicationData("GUID_FOO")).thenReturn(getLazyMap());

		ResultActions resultActions = performRequest("/noteStore/getNoteApplicationData", "{\"guid\":\"GUID_FOO\"}");
		verifyApplicationData(resultActions);

		verify(noteStoreOperations).getNoteApplicationData("GUID_FOO");
	}

	@Test
	public void testGetNoteApplicationDataEntry() throws Exception {
		when(noteStoreOperations.getNoteApplicationDataEntry("GUID", "KEY")).thenReturn("FOO");
		performRequest("/noteStore/getNoteApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\"}")
				.andExpect(status().isOk())
				.andExpect(content().string("FOO"))
		;
		verify(noteStoreOperations).getNoteApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testSetNoteApplicationDataEntry() throws Exception {
		when(noteStoreOperations.setNoteApplicationDataEntry("GUID", "KEY", "VALUE")).thenReturn(100);
		String json = "{\"guid\":\"GUID\", \"key\":\"KEY\", \"value\":\"VALUE\"}";
		performRequest("/noteStore/setNoteApplicationDataEntry", json)
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).setNoteApplicationDataEntry("GUID", "KEY", "VALUE");
	}

	@Test
	public void testUnsetNoteApplicationDataEntry() throws Exception {
		when(noteStoreOperations.unsetNoteApplicationDataEntry("GUID", "KEY")).thenReturn(100);
		performRequest("/noteStore/unsetNoteApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\"}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).unsetNoteApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testGetNoteContent() throws Exception {
		when(noteStoreOperations.getNoteContent("GUID_FOO")).thenReturn("FOO");
		performRequest("/noteStore/getNoteContent", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getNoteContent("GUID_FOO");
	}

	@Test
	public void testGetNoteSearchText() throws Exception {
		when(noteStoreOperations.getNoteSearchText("GUID", true, true)).thenReturn("FOO");
		String json = "{\"guid\":\"GUID\", \"noteOnly\":true, \"tokenizeForIndexing\":true}";
		performRequest("/noteStore/getNoteSearchText", json)
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getNoteSearchText("GUID", true, true);
	}

	@Test
	public void testGetResourceSearchText() throws Exception {
		when(noteStoreOperations.getResourceSearchText("GUID_FOO")).thenReturn("FOO");
		performRequest("/noteStore/getResourceSearchText", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getResourceSearchText("GUID_FOO");
	}

	@Test
	public void testGetNoteTagNames() throws Exception {
		when(noteStoreOperations.getNoteTagNames("GUID_FOO")).thenReturn(Arrays.asList("FOO", "BAR"));
		performRequest("/noteStore/getNoteTagNames", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").value("FOO"))
				.andExpect(jsonPath("$[1]").value("BAR"))
		;
		verify(noteStoreOperations).getNoteTagNames("GUID_FOO");
	}

	@Test
	public void testCreateNote() throws Exception {
		Note note = getNote();
		when(noteStoreOperations.createNote(note)).thenReturn(note);
		String json = readJson("note.json");
		ResultActions resultActions = performRequest("/noteStore/createNote", json);
		verifyNote(resultActions);
		verify(noteStoreOperations).createNote(note);
	}

	@Test
	public void testUpdateNote() throws Exception {
		Note note = getNote();
		when(noteStoreOperations.updateNote(note)).thenReturn(note);
		String json = readJson("note.json");
		ResultActions resultActions = performRequest("/noteStore/updateNote", json);
		verifyNote(resultActions);
		verify(noteStoreOperations).updateNote(getNote());
	}

	@Test
	public void testDeleteNote() throws Exception {
		when(noteStoreOperations.deleteNote("GUID_FOO")).thenReturn(100);
		performRequest("/noteStore/deleteNote", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).deleteNote("GUID_FOO");
	}

	@Test
	public void testExpungeNote() throws Exception {
		when(noteStoreOperations.expungeNote("GUID_FOO")).thenReturn(100);
		performRequest("/noteStore/expungeNote", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).expungeNote("GUID_FOO");
	}

	@Test
	public void testExpungeNotes() throws Exception {
		when(noteStoreOperations.expungeNotes(Arrays.asList("FOO", "BAR"))).thenReturn(100);
		performRequest("/noteStore/expungeNotes", "{\"noteGuids\":[\"FOO\", \"BAR\"]}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).expungeNotes(Arrays.asList("FOO", "BAR"));
	}

	@Test
	public void testExpungeInactiveNotes() throws Exception {
		when(noteStoreOperations.expungeInactiveNotes()).thenReturn(100);
		performRequest("/noteStore/expungeInactiveNotes", "{}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).expungeInactiveNotes();
	}

	@Test
	public void testCopyNote() throws Exception {
		when(noteStoreOperations.copyNote("NOTE_GUID", "TO_NOTEBOOK_GUID")).thenReturn(getNote());
		StringBuilder sb = new StringBuilder();
		sb.append("{                                            ");
		sb.append("    \"noteGuid\":\"NOTE_GUID\",              ");
		sb.append("    \"toNotebookGuid\":\"TO_NOTEBOOK_GUID\"  ");
		sb.append("}                                            ");
		ResultActions resultActions = performRequest("/noteStore/copyNote", sb.toString());
		verifyNote(resultActions);
		verify(noteStoreOperations).copyNote("NOTE_GUID", "TO_NOTEBOOK_GUID");
	}

	@Test
	public void testListNoteVersions() throws Exception {
		when(noteStoreOperations.listNoteVersions("NOTE_GUID_FOO"))
				.thenReturn(Arrays.asList(getNoteVersionId(), getNoteVersionId()));
		ResultActions resultActions = performRequest("/noteStore/listNoteVersions", "{\"noteGuid\":\"NOTE_GUID_FOO\"}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifyNoteVersionId(resultActions, "$[0]");
		verifyNoteVersionId(resultActions, "$[1]");
		verify(noteStoreOperations).listNoteVersions("NOTE_GUID_FOO");
	}

	@Test
	public void testGetNoteVersion() throws Exception {
		when(noteStoreOperations.getNoteVersion("NOTE_GUID", 100, true, true, true)).thenReturn(getNote());
		StringBuilder sb = new StringBuilder();
		sb.append("{                                        ");
		sb.append("    \"noteGuid\":\"NOTE_GUID\",          ");
		sb.append("    \"updateSequenceNum\":100,           ");
		sb.append("    \"withResourcesData\":true,          ");
		sb.append("    \"withResourcesRecognition\":true,   ");
		sb.append("    \"withResourcesAlternateData\":true  ");
		sb.append("}                                        ");
		ResultActions resultActions = performRequest("/noteStore/getNoteVersion", sb.toString());
		verifyNote(resultActions);
		verify(noteStoreOperations).getNoteVersion("NOTE_GUID", 100, true, true, true);
	}

	@Test
	public void testGetResource() throws Exception {
		when(noteStoreOperations.getResource("GUID", true, true, true, true)).thenReturn(getResource());
		StringBuilder sb = new StringBuilder();
		sb.append("{                              ");
		sb.append("    \"guid\":\"GUID\",         ");
		sb.append("    \"withData\":true,         ");
		sb.append("    \"withRecognition\":true,  ");
		sb.append("    \"withAttributes\":true,   ");
		sb.append("    \"withAlternateData\":true ");
		sb.append("}                              ");
		ResultActions resultActions = performRequest("/noteStore/getResource", sb.toString());
		verifyResource(resultActions);
		verify(noteStoreOperations).getResource("GUID", true, true, true, true);
	}

	@Test
	public void testGetResourceApplicationData() throws Exception {
		when(noteStoreOperations.getResourceApplicationData("GUID_FOO")).thenReturn(getLazyMap());
		ResultActions resultActions = performRequest("/noteStore/getResourceApplicationData", "{\"guid\":\"GUID_FOO\"}");
		verifyApplicationData(resultActions);
		verify(noteStoreOperations).getResourceApplicationData("GUID_FOO");
	}

	@Test
	public void testGetResourceApplicationDataEntry() throws Exception {
		when(noteStoreOperations.getResourceApplicationDataEntry("GUID", "KEY")).thenReturn("FOO");
		performRequest("/noteStore/getResourceApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\" }")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getResourceApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testSetResourceApplicationDataEntry() throws Exception {
		when(noteStoreOperations.setResourceApplicationDataEntry("GUID", "KEY", "VALUE")).thenReturn(100);
		String json = "{\"guid\":\"GUID\", \"key\":\"KEY\", \"value\":\"VALUE\" }";
		performRequest("/noteStore/setResourceApplicationDataEntry", json)
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).setResourceApplicationDataEntry("GUID", "KEY", "VALUE");
	}

	@Test
	public void testUnsetResourceApplicationDataEntry() throws Exception {
		when(noteStoreOperations.unsetResourceApplicationDataEntry("GUID", "KEY")).thenReturn(100);
		performRequest("/noteStore/unsetResourceApplicationDataEntry", "{\"guid\":\"GUID\", \"key\":\"KEY\" }")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).unsetResourceApplicationDataEntry("GUID", "KEY");
	}

	@Test
	public void testUpdateResource() throws Exception {
		when(noteStoreOperations.updateResource(getResource())).thenReturn(100);
		String json = readJson("updateResource.json");
		performRequest("/noteStore/updateResource", json)
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).updateResource(getResource());
	}

	@Test
	public void testGetResourceData() throws Exception {
		when(noteStoreOperations.getResourceData("GUID_FOO")).thenReturn("FOO".getBytes());
		performRequest("/noteStore/getResourceData", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getResourceData("GUID_FOO");
	}

	@Test
	public void testGetResourceByHash() throws Exception {
		byte[] byteFoo = getByteFoo();
		when(noteStoreOperations.getResourceByHash("GUID", byteFoo, true, true, true)).thenReturn(getResource());
		StringBuilder sb = new StringBuilder();
		sb.append("{                               ");
		sb.append("    \"noteGuid\": \"GUID\",     ");
		sb.append("    \"contentHash\": \"Rk9P\",  ");  // base64 encoded value of "FOO" => 70,79,79 in byte
		sb.append("    \"withData\": true,         ");
		sb.append("    \"withRecognition\": true,  ");
		sb.append("    \"withAlternateData\": true ");
		sb.append("}                               ");
		ResultActions resultActions = performRequest("/noteStore/getResourceByHash", sb.toString());
		verifyResource(resultActions);
		verify(noteStoreOperations).getResourceByHash("GUID", byteFoo, true, true, true);
	}

	@Test
	public void testGetResourceRecognition() throws Exception {
		when(noteStoreOperations.getResourceRecognition("GUID_FOO")).thenReturn("FOO".getBytes());
		performRequest("/noteStore/getResourceRecognition", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getResourceRecognition("GUID_FOO");
	}

	@Test
	public void testGetResourceAlternateData() throws Exception {
		when(noteStoreOperations.getResourceAlternateData("GUID_FOO")).thenReturn("FOO".getBytes());
		performRequest("/noteStore/getResourceAlternateData", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).getResourceAlternateData("GUID_FOO");
	}

	@Test
	public void testGetResourceAttributes() throws Exception {
		when(noteStoreOperations.getResourceAttributes("GUID_FOO")).thenReturn(getResourceAttributes());
		ResultActions resultActions = performRequest("/noteStore/getResourceAttributes", "{\"guid\":\"GUID_FOO\"}");
		verifyResourceAttribute(resultActions);
		verify(noteStoreOperations).getResourceAttributes("GUID_FOO");
	}

	@Test
	public void testGetPublicNotebook() throws Exception {
		when(noteStoreOperations.getPublicNotebook(100, "PUBLIC_URI")).thenReturn(getNotebook());
		StringBuilder sb = new StringBuilder();
		sb.append("{                                  ");
		sb.append("    \"userId\": 100,               ");
		sb.append("    \"publicUri\": \"PUBLIC_URI\"  ");
		sb.append("}                                  ");
		ResultActions resultActions = performRequest("/noteStore/getPublicNotebook", sb.toString());
		verifyNotebook(resultActions);
		verify(noteStoreOperations).getPublicNotebook(100, "PUBLIC_URI");
	}

	@Test
	public void testCreateSharedNotebook() throws Exception {
		SharedNotebook sharedNotebook = getSharedNotebook1();
		when(noteStoreOperations.createSharedNotebook(sharedNotebook)).thenReturn(sharedNotebook);
		String json = readJson("sharedNotebook1.json");
		ResultActions resultActions = performRequest("/noteStore/createSharedNotebook", json);
		verifySharedNotebook1(resultActions);
		verify(noteStoreOperations).createSharedNotebook(sharedNotebook);
	}

	@Test
	public void testUpdateSharedNotebook() throws Exception {
		SharedNotebook sharedNotebook = getSharedNotebook1();
		when(noteStoreOperations.updateSharedNotebook(sharedNotebook)).thenReturn(100);
		String json = readJson("sharedNotebook1.json");
		performRequest("/noteStore/updateSharedNotebook", json)
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).updateSharedNotebook(sharedNotebook);
	}

	@Test
	public void testSendMessageToSharedNotebookMembers() throws Exception {
		List<String> recipients = Arrays.asList("FOO", "BAR");
		when(noteStoreOperations.sendMessageToSharedNotebookMembers("GUID", "MESSAGE", recipients)).thenReturn(100);

		StringBuilder sb = new StringBuilder();
		sb.append("{                                 ");
		sb.append("    \"notebookGuid\": \"GUID\",   ");
		sb.append("    \"messageText\": \"MESSAGE\", ");
		sb.append("    \"recipients\": [             ");
		sb.append("         \"FOO\", \"BAR\"         ");
		sb.append("    ]                             ");
		sb.append("}                                 ");
		performRequest("/noteStore/sendMessageToSharedNotebookMembers", sb.toString())
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).sendMessageToSharedNotebookMembers("GUID", "MESSAGE", recipients);
	}

	@Test
	public void testListSharedNotebooks() throws Exception {
		when(noteStoreOperations.listSharedNotebooks()).thenReturn(Arrays.asList(getSharedNotebook1(), getSharedNotebook1()));
		ResultActions resultActions = performRequest("/noteStore/listSharedNotebooks", "{}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifySharedNotebook1(resultActions, "$[0]");
		verifySharedNotebook1(resultActions, "$[1]");
		verify(noteStoreOperations).listSharedNotebooks();
	}

	@Test
	public void testExpungeSharedNotebooks() throws Exception {
		List<Long> sharedNotebookIds = Arrays.asList(1L, 2L, 3L);
		when(noteStoreOperations.expungeSharedNotebooks(sharedNotebookIds)).thenReturn(100);
		performRequest("/noteStore/expungeSharedNotebooks", "{\"sharedNotebookIds\":[1,2,3]}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).expungeSharedNotebooks(sharedNotebookIds);
	}

	@Test
	public void testCreateLinkedNotebook() throws Exception {
		LinkedNotebook linkedNotebook = getLinkedNotebook();
		when(noteStoreOperations.createLinkedNotebook(linkedNotebook)).thenReturn(linkedNotebook);
		String json = readJson("linkedNotebook.json");
		ResultActions resultActions = performRequest("/noteStore/createLinkedNotebook", json);
		verifyLinkedNotebook(resultActions);
		verify(noteStoreOperations).createLinkedNotebook(linkedNotebook);
	}

	@Test
	public void testUpdateLinkedNotebook() throws Exception {
		LinkedNotebook linkedNotebook = getLinkedNotebook();
		when(noteStoreOperations.updateLinkedNotebook(linkedNotebook)).thenReturn(100);
		String json = readJson("linkedNotebook.json");
		performRequest("/noteStore/updateLinkedNotebook", json)
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).updateLinkedNotebook(linkedNotebook);
	}

	@Test
	public void testListLinkedNotebooks() throws Exception {
		when(noteStoreOperations.listLinkedNotebooks()).thenReturn(Arrays.asList(getLinkedNotebook(), getLinkedNotebook()));
		ResultActions resultActions = performRequest("/noteStore/listLinkedNotebooks", "{}");
		resultActions.andExpect(jsonPath("$").isArray());
		verifyLinkedNotebook(resultActions, "$[0]");
		verifyLinkedNotebook(resultActions, "$[1]");
		verify(noteStoreOperations).listLinkedNotebooks();
	}

	@Test
	public void testExpungeLinkedNotebook() throws Exception {
		when(noteStoreOperations.expungeLinkedNotebook("GUID_FOO")).thenReturn(100);
		performRequest("/noteStore/expungeLinkedNotebook", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("100"));
		verify(noteStoreOperations).expungeLinkedNotebook("GUID_FOO");
	}

	@Test
	public void testAuthenticateToSharedNotebook() throws Exception {
		when(noteStoreOperations.authenticateToSharedNotebook("SHARE_KEY_FOO")).thenReturn(getAuthenticationResult());
		ResultActions resultActions = performRequest("/noteStore/authenticateToSharedNotebook", "{\"shareKey\":\"SHARE_KEY_FOO\"}");
		verifyAuthenticationResult(resultActions);
		verify(noteStoreOperations).authenticateToSharedNotebook("SHARE_KEY_FOO");
	}

	@Test
	public void testNoteStorGetSharedNotebookByAuth() throws Exception {
		when(noteStoreOperations.getSharedNotebookByAuth()).thenReturn(getSharedNotebook1());
		ResultActions resultActions = performRequest("/noteStore/getSharedNotebookByAuth", "{}");
		verifySharedNotebook1(resultActions);
		verify(noteStoreOperations).getSharedNotebookByAuth();
	}

	@Test
	public void testEmailNote() throws Exception {
		String json = readJson("emailNote.json");
		performRequest("/noteStore/emailNote", json)
				.andExpect(status().isOk()).andExpect(content().string(""));

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
		when(noteStoreOperations.shareNote("GUID_FOO")).thenReturn("FOO");
		performRequest("/noteStore/shareNote", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string("FOO"));
		verify(noteStoreOperations).shareNote("GUID_FOO");
	}

	@Test
	public void testStopSharingNote() throws Exception {
		performRequest("/noteStore/stopSharingNote", "{\"guid\":\"GUID_FOO\"}")
				.andExpect(status().isOk()).andExpect(content().string(""));
		verify(noteStoreOperations).stopSharingNote("GUID_FOO");
	}

	@Test
	public void testAuthenticateToSharedNote() throws Exception {
		when(noteStoreOperations.authenticateToSharedNote("GUID", "NOTE_KEY", "AUTHENTICATION_TOKEN")).thenReturn(getAuthenticationResult());
		String json = "{\"guid\":\"GUID\", \"noteKey\":\"NOTE_KEY\", \"authenticationToken\":\"AUTHENTICATION_TOKEN\" }";
		ResultActions resultActions = performRequest("/noteStore/authenticateToSharedNote", json);
		verifyAuthenticationResult(resultActions);
		verify(noteStoreOperations).authenticateToSharedNote("GUID", "NOTE_KEY", "AUTHENTICATION_TOKEN");
	}

	@Test
	public void testFindRelated() throws Exception {

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

		RelatedResult relatedResult = new RelatedResult();
		relatedResult.setNotes(Arrays.asList(getNote(), getNote()));
		relatedResult.setNotebooks(Arrays.asList(getNotebook(), getNotebook()));
		relatedResult.setTags(Arrays.asList(getTag(), getTag()));
		relatedResult.setContainingNotebooks(Arrays.asList(getNotebookDescriptor(), getNotebookDescriptor()));

		when(noteStoreOperations.findRelated(query, resultSpec)).thenReturn(relatedResult);

		String json = readJson("findRelated.json");
		ResultActions resultActions = performRequest("/noteStore/findRelated", json);

		resultActions.andExpect(jsonPath("$.notes").isArray());
		verifyNote(resultActions, "$.notes[0]");
		verifyNote(resultActions, "$.notes[1]");

		resultActions.andExpect(jsonPath("$.notebooks").isArray());
		verifyNotebook(resultActions, "$.notebooks[0]");
		verifyNotebook(resultActions, "$.notebooks[1]");

		resultActions.andExpect(jsonPath("$.tags").isArray());
		verifyTag(resultActions, "$.tags[0]");
		verifyTag(resultActions, "$.tags[1]");

		resultActions.andExpect(jsonPath("$.containingNotebooks").isArray());
		verifyNotebookDescriptor(resultActions, "$.containingNotebooks[0]");
		verifyNotebookDescriptor(resultActions, "$.containingNotebooks[1]");

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
		performRequest("/noteStore/setSharedNotebookRecipientSettings", sb.toString())
				.andExpect(status().isOk()).andExpect(content().string(""));
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
