package net.ttddyy.evernote.rest;

import org.junit.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Miscellaneous tests for {@link StoreOperationController}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StoreOperationControllerMiscIntegrationTest extends AbstractStoreOperationControllerIntegrationTest {

	@Test
	public void testNoInputForNoParameterMethod() throws Exception {
		given(userStoreOperations.isBusinessUser()).willReturn(true);
		mockMvc.perform(post("/userStore/isBusinessUser"));
		verify(userStoreOperations).isBusinessUser();
	}

}
