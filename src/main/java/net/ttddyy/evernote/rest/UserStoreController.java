package net.ttddyy.evernote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.evernote.api.Evernote;
import org.springframework.social.evernote.api.NoteStoreOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tadaya Tsuyukubo
 */
@RestController
@RequestMapping("/userStore")
public class UserStoreController extends AbstractStoreController<NoteStoreOperations> {

	@Autowired
	private Evernote evernote;


	@Override
	protected NoteStoreOperations getStoreOperations() {
		return evernote.noteStoreOperations();
	}

}
