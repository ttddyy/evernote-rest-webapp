package net.ttddyy.evernote.rest;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.social.evernote.api.EvernoteException;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.WebUtils;

import java.util.Enumeration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Tadaya Tsuyukubo
 */
public class StoreOperationControllerExceptionIntegrationTest extends AbstractStoreOperationControllerIntegrationTest {


	@Autowired
	private ErrorAttributes errorAttributes;


	@Test
	public void testWhenEdamUserExceptionThrown() throws Exception {
		EvernoteException evernoteException = new EvernoteException("MESSAGE", new EDAMUserException());
		when(userStoreOperations.isBusinessUser()).thenThrow(evernoteException);

		performRequest("/userStore/isBusinessUser", "{}").andExpect(forwardedUrl("/error"));
		verify(userStoreOperations).isBusinessUser();
	}

	@Test
	public void testWhenEdamNotFoundExceptionThrown() throws Exception {
		EvernoteException evernoteException = new EvernoteException("MESSAGE", new EDAMNotFoundException());
		when(userStoreOperations.isBusinessUser()).thenThrow(evernoteException);

		performRequest("/userStore/isBusinessUser", "{}").andExpect(forwardedUrl("/error"));
		verify(userStoreOperations).isBusinessUser();
	}

	@Test
	public void testWhenEdamSystemExceptionThrown() throws Exception {
		EvernoteException evernoteException = new EvernoteException("MESSAGE", new EDAMSystemException());
		when(userStoreOperations.isBusinessUser()).thenThrow(evernoteException);

		performRequest("/userStore/isBusinessUser", "{}").andExpect(forwardedUrl("/error"));
		verify(userStoreOperations).isBusinessUser();
	}

	@Test
	public void testWhenTExceptionThrown() throws Exception {
		EvernoteException evernoteException = new EvernoteException("MESSAGE", new TException());
		when(userStoreOperations.isBusinessUser()).thenThrow(evernoteException);

		try {
			performRequest("/userStore/isBusinessUser", "{}");
			fail("error should be thrown");
		} catch (NestedServletException e) {
			assertThat(e.getCause(), instanceOf(EvernoteRestException.class));
			assertThat(e.getCause().getCause(), sameInstance((Throwable)evernoteException));
		}
	}

	@Test
	public void testForwardingToError() throws Exception {

		EvernoteException evernoteException = new EvernoteException("MESSAGE", new EDAMUserException());

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		// mimic how exception info is registered in logic
		((HandlerExceptionResolver) errorAttributes).resolveException(request, response, null, evernoteException);
		request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST.value()); // return error 400

		MockHttpServletRequestBuilder requestBuilder = post("/error");
		copyRequestAttributes(requestBuilder, request);

		ResultActions result = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON));
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("$.timestamp").exists());
		result.andExpect(jsonPath("$.status").value(400));
		result.andExpect(jsonPath("$.error").exists());
		result.andExpect(jsonPath("$.exception").exists());
		result.andExpect(jsonPath("$.message").value("MESSAGE"));

	}

	private void copyRequestAttributes(MockHttpServletRequestBuilder requestBuilder, MockHttpServletRequest request) {
		Enumeration<String> attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attrName = attributeNames.nextElement();
			requestBuilder.requestAttr(attrName, request.getAttribute(attrName));
		}
	}
}
