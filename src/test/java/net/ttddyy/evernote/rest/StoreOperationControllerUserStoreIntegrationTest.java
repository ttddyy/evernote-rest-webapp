package net.ttddyy.evernote.rest;

import org.junit.Test;

import static org.mockito.Mockito.verify;

/**
 * Test class for UserStore related operations on {@link StoreOperationController}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StoreOperationControllerUserStoreIntegrationTest extends AbstractStoreOperationControllerIntegrationTest {

	@Test
	public void testIsBusinessUser() throws Exception {
		performRequest("/userStore/isBusinessUser", "{}");
		verify(userStoreOperations).isBusinessUser();
	}

	@Test
	public void testCheckVersion() throws Exception {
		String json = "{\"clientName\": \"foo\", \"edamVersionMajor\": 10, \"edamVersionMinor\": 20}";
		performRequest("/userStore/checkVersion", json);
		verify(userStoreOperations).checkVersion("foo", (short) 10, (short) 20);
	}

	@Test
	public void testGetBootstrapInfo() throws Exception {
		String json = "{\"locale\": \"foo\"}";
		performRequest("/userStore/getBootstrapInfo", json);
		verify(userStoreOperations).getBootstrapInfo("foo");
	}

	@Test
	public void testAuthenticate() throws Exception {
		String json = "{\"username\":\"foo\", \"password\":\"foo-pass\", " +
				"\"consumerKey\": \"key\", \"consumerSecret\": \"secret\", \"supportsTwoFactor\":true }";
		performRequest("/userStore/authenticate", json);
		verify(userStoreOperations).authenticate("foo", "foo-pass", "key", "secret", true);
	}

	@Test
	public void testAuthenticateLongSession() throws Exception {
		String json = "{\"username\":\"foo\", \"password\":\"foo-pass\", " +
				"\"consumerKey\": \"key\", \"consumerSecret\": \"secret\", " +
				"\"deviceIdentifier\":\"deviceId\", \"deviceDescription\":\"deviceDesc\", " +
				"\"supportsTwoFactor\":true }";
		performRequest("/userStore/authenticateLongSession", json);
		verify(userStoreOperations).authenticateLongSession("foo", "foo-pass", "key", "secret", "deviceId", "deviceDesc", true);
	}

	@Test
	public void testAuthenticateToBusiness() throws Exception {
		performRequest("/userStore/authenticateToBusiness", "{}");
		verify(userStoreOperations).authenticateToBusiness();
	}

	@Test
	public void testRefreshAuthentication() throws Exception {
		performRequest("/userStore/refreshAuthentication", "{}");
		verify(userStoreOperations).refreshAuthentication();
	}

	@Test
	public void testGetUser() throws Exception {
		performRequest("/userStore/getUser", "{}");
		verify(userStoreOperations).getUser();
	}

	@Test
	public void testGetPublicUserInfo() throws Exception {
		performRequest("/userStore/getPublicUserInfo", "{\"username\":\"foo\"}");
		verify(userStoreOperations).getPublicUserInfo("foo");
	}

	@Test
	public void testGetPremiumInfo() throws Exception {
		performRequest("/userStore/getPremiumInfo", "{}");
		verify(userStoreOperations).getPremiumInfo();
	}

	@Test
	public void testGetNoteSoreUrl() throws Exception {
		performRequest("/userStore/getNoteStoreUrl", "{}");
		verify(userStoreOperations).getNoteStoreUrl();
	}

	@Test
	public void testRevokeLongSession() throws Exception {
		performRequest("/userStore/revokeLongSession", "{}");
		verify(userStoreOperations).revokeLongSession();
	}

	@Test
	public void testCompleteTwoFactorAuthentication() throws Exception {
		String json = "{\"authenticationToken\":\"authToken\", \"oneTimeCode\":\"oneTime\", " +
				"\"deviceIdentifier\":\"deviceId\", \"deviceDescription\":\"deviceDesc\"}";
		performRequest("/userStore/completeTwoFactorAuthentication", json);
		verify(userStoreOperations).completeTwoFactorAuthentication("authToken", "oneTime", "deviceId", "deviceDesc");
	}

}
