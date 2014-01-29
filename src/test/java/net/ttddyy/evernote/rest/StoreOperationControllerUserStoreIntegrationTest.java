package net.ttddyy.evernote.rest;

import com.evernote.edam.userstore.BootstrapInfo;
import com.evernote.edam.userstore.BootstrapProfile;
import com.evernote.edam.userstore.BootstrapSettings;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static net.ttddyy.evernote.rest.ResultActionsUtils.verifyAuthenticationResult;
import static net.ttddyy.evernote.rest.ResultActionsUtils.verifyUser;
import static net.ttddyy.evernote.rest.TestDomainUtils.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for UserStore related operations on {@link StoreOperationController}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StoreOperationControllerUserStoreIntegrationTest extends AbstractStoreOperationControllerIntegrationTest {

	@Test
	public void testIsBusinessUser() throws Exception {
		when(userStoreOperations.isBusinessUser()).thenReturn(true);
		performRequest("/userStore/isBusinessUser", "{}").andExpect(content().string("true"));
		verify(userStoreOperations).isBusinessUser();
	}

	@Test
	public void testCheckVersion() throws Exception {
		when(userStoreOperations.checkVersion("foo", (short) 10, (short) 20)).thenReturn(true);

		StringBuilder sb = new StringBuilder();
		sb.append("{                             ");
		sb.append("    \"clientName\":\"foo\",   ");
		sb.append("    \"edamVersionMajor\": 10, ");
		sb.append("    \"edamVersionMinor\": 20  ");
		sb.append("}                             ");

		performRequest("/userStore/checkVersion", sb.toString()).andExpect(content().string("true"));

		verify(userStoreOperations).checkVersion("foo", (short) 10, (short) 20);
	}

	@Test
	public void testGetBootstrapInfo() throws Exception {
		BootstrapSettings settings = new BootstrapSettings();
		settings.setServiceHost("SERVICE_HOST");
		settings.setMarketingUrl("MARKETING_URL");
		settings.setSupportUrl("SUPPORT_URL");
		settings.setAccountEmailDomain("ACCOUNT_EMAIL_DOMAIN");
		settings.setEnableFacebookSharing(true);
		settings.setEnableGiftSubscriptions(true);
		settings.setEnableSupportTickets(true);
		settings.setEnableSharedNotebooks(true);
		settings.setEnableSingleNoteSharing(true);
		settings.setEnableSponsoredAccounts(true);
		settings.setEnableTwitterSharing(true);
		settings.setEnableLinkedInSharing(true);
		settings.setEnablePublicNotebooks(true);

		BootstrapProfile profile = new BootstrapProfile();
		profile.setName("PROFILE_NAME");
		profile.setSettings(settings);

		List<BootstrapProfile> profiles = new ArrayList<BootstrapProfile>();
		profiles.add(profile);

		BootstrapInfo bootstrapInfo = new BootstrapInfo();
		bootstrapInfo.setProfiles(profiles);
		when(userStoreOperations.getBootstrapInfo("foo")).thenReturn(bootstrapInfo);

		performRequest("/userStore/getBootstrapInfo", "{\"locale\": \"foo\"}")
				.andExpect(jsonPath("$.profiles[0].name").value("PROFILE_NAME"))
				.andExpect(jsonPath("$.profiles[0].settings.serviceHost").value("SERVICE_HOST"))
				.andExpect(jsonPath("$.profiles[0].settings.marketingUrl").value("MARKETING_URL"))
				.andExpect(jsonPath("$.profiles[0].settings.supportUrl").value("SUPPORT_URL"))
				.andExpect(jsonPath("$.profiles[0].settings.accountEmailDomain").value("ACCOUNT_EMAIL_DOMAIN"))
				.andExpect(jsonPath("$.profiles[0].settings.enableFacebookSharing").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableGiftSubscriptions").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableSupportTickets").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableSharedNotebooks").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableSingleNoteSharing").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableSponsoredAccounts").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableTwitterSharing").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enableLinkedInSharing").value(true))
				.andExpect(jsonPath("$.profiles[0].settings.enablePublicNotebooks").value(true))
		;
		verify(userStoreOperations).getBootstrapInfo("foo");
	}

	@Test
	public void testAuthenticate() throws Exception {
		when(userStoreOperations.authenticate("foo", "foo-pass", "key", "secret", true)).thenReturn(getAuthenticationResult());

		StringBuilder sb = new StringBuilder();
		sb.append("{                                    ");
		sb.append("    \"username\":\"foo\",            ");
		sb.append("    \"password\":\"foo-pass\",       ");
		sb.append("    \"consumerKey\":\"key\",         ");
		sb.append("    \"consumerSecret\":\"secret\",   ");
		sb.append("    \"supportsTwoFactor\":true       ");
		sb.append("}                                    ");

		ResultActions result = performRequest("/userStore/authenticate", sb.toString());
		verifyAuthenticationResult(result);
		verify(userStoreOperations).authenticate("foo", "foo-pass", "key", "secret", true);
	}

	@Test
	public void testAuthenticateLongSession() throws Exception {

		when(userStoreOperations.authenticateLongSession("foo", "foo-pass", "key", "secret", "deviceId", "deviceDesc", true)).thenReturn(getAuthenticationResult());

		StringBuilder sb = new StringBuilder();
		sb.append("{                                    ");
		sb.append("    \"username\":\"foo\",            ");
		sb.append("    \"password\":\"foo-pass\",       ");
		sb.append("    \"consumerKey\":\"key\",         ");
		sb.append("    \"consumerSecret\":\"secret\",   ");
		sb.append("    \"deviceIdentifier\":\"deviceId\",   ");
		sb.append("    \"deviceDescription\":\"deviceDesc\",   ");
		sb.append("    \"supportsTwoFactor\":true       ");
		sb.append("}                                    ");

		ResultActions result = performRequest("/userStore/authenticateLongSession", sb.toString());
		verifyAuthenticationResult(result);
		verify(userStoreOperations).authenticateLongSession("foo", "foo-pass", "key", "secret", "deviceId", "deviceDesc", true);
	}

	@Test
	public void testAuthenticateToBusiness() throws Exception {
		when(userStoreOperations.authenticateToBusiness()).thenReturn(getAuthenticationResult());
		ResultActions result = performRequest("/userStore/authenticateToBusiness", "{}");
		verifyAuthenticationResult(result);
		verify(userStoreOperations).authenticateToBusiness();
	}

	@Test
	public void testRefreshAuthentication() throws Exception {
		when(userStoreOperations.refreshAuthentication()).thenReturn(getAuthenticationResult());
		ResultActions result = performRequest("/userStore/refreshAuthentication", "{}");
		verifyAuthenticationResult(result);
		verify(userStoreOperations).refreshAuthentication();
	}

	@Test
	public void testGetUser() throws Exception {
		when(userStoreOperations.getUser()).thenReturn(getUser());
		ResultActions result = performRequest("/userStore/getUser", "{}");
		verifyUser(result);
		verify(userStoreOperations).getUser();
	}

	@Test
	public void testGetPublicUserInfo() throws Exception {

		when(userStoreOperations.getPublicUserInfo("foo")).thenReturn(getPublicUserInfo());

		performRequest("/userStore/getPublicUserInfo", "{\"username\":\"foo\"}")
				.andExpect(jsonPath("$.userId").value(100))
				.andExpect(jsonPath("$.shardId").value("SHARDID"))
				.andExpect(jsonPath("$.privilege").value("VIP"))
				.andExpect(jsonPath("$.username").value("USERNAME"))
				.andExpect(jsonPath("$.noteStoreUrl").value("NOTE_STORE_URL"))
				.andExpect(jsonPath("$.webApiUrlPrefix").value("WEB_API_URL_PREFIX"))
		;
		verify(userStoreOperations).getPublicUserInfo("foo");
	}

	@Test
	public void testGetPremiumInfo() throws Exception {
		when(userStoreOperations.getPremiumInfo()).thenReturn(getPremiumInfo());
		performRequest("/userStore/getPremiumInfo", "{}")
				.andExpect(jsonPath("$.currentTime").value(2000))
				.andExpect(jsonPath("$.premium").value(true))
				.andExpect(jsonPath("$.premiumRecurring").value(true))
				.andExpect(jsonPath("$.premiumExpirationDate").value(2010))
				.andExpect(jsonPath("$.premiumExtendable").value(true))
				.andExpect(jsonPath("$.premiumPending").value(true))
				.andExpect(jsonPath("$.premiumCancellationPending").value(true))
				.andExpect(jsonPath("$.canPurchaseUploadAllowance").value(true))
				.andExpect(jsonPath("$.sponsoredGroupName").value("SPONSORED_GROUP_NAME"))
				.andExpect(jsonPath("$.sponsoredGroupRole").value("GROUP_ADMIN"))
				.andExpect(jsonPath("$.premiumUpgradable").value(true))
		;
		verify(userStoreOperations).getPremiumInfo();
	}

	@Test
	public void testGetNoteSoreUrl() throws Exception {
		when(userStoreOperations.getNoteStoreUrl()).thenReturn("NOTE_STORE_URL");
		performRequest("/userStore/getNoteStoreUrl", "{}").andExpect(content().string("NOTE_STORE_URL"));
		verify(userStoreOperations).getNoteStoreUrl();
	}

	@Test
	public void testRevokeLongSession() throws Exception {
		performRequest("/userStore/revokeLongSession", "{}")
				.andExpect(status().isOk())
				.andExpect(content().string(""))
		;
		verify(userStoreOperations).revokeLongSession();
	}

	@Test
	public void testCompleteTwoFactorAuthentication() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{                                          ");
		sb.append("    \"authenticationToken\":\"authToken\", ");
		sb.append("    \"oneTimeCode\":\"oneTime\",           ");
		sb.append("    \"deviceIdentifier\":\"deviceId\",     ");
		sb.append("    \"deviceDescription\":\"deviceDesc\"   ");
		sb.append("}                                          ");

		performRequest("/userStore/completeTwoFactorAuthentication", sb.toString())
				.andExpect(status().isOk())
				.andExpect(content().string(""))
		;
		verify(userStoreOperations).completeTwoFactorAuthentication("authToken", "oneTime", "deviceId", "deviceDesc");
	}

}
