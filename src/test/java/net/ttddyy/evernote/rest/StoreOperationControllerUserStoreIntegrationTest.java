package net.ttddyy.evernote.rest;

import com.evernote.edam.userstore.BootstrapInfo;
import com.evernote.edam.userstore.BootstrapProfile;
import com.evernote.edam.userstore.BootstrapSettings;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

	private void verifyAuthenticationResult(ResultActions resultActions) throws Exception {
		verifyUser(resultActions, "$.user");
		resultActions
				.andExpect(jsonPath("$.currentTime").value(100))
				.andExpect(jsonPath("$.authenticationToken").value("AUTHENTICATION_TOKEN"))
				.andExpect(jsonPath("$.expiration").value(200))
				.andExpect(jsonPath("$.publicUserInfo.userId").value(100))
				.andExpect(jsonPath("$.publicUserInfo.shardId").value("SHARDID"))
				.andExpect(jsonPath("$.publicUserInfo.privilege").value("VIP"))
				.andExpect(jsonPath("$.publicUserInfo.username").value("USERNAME"))
				.andExpect(jsonPath("$.publicUserInfo.noteStoreUrl").value("NOTE_STORE_URL"))
				.andExpect(jsonPath("$.publicUserInfo.webApiUrlPrefix").value("WEB_API_URL_PREFIX"))
				.andExpect(jsonPath("$.noteStoreUrl").value("NOTE_STORE_URL"))
				.andExpect(jsonPath("$.webApiUrlPrefix").value("WEB_API_URL_PREFIX"))
				.andExpect(jsonPath("$.secondFactorRequired").value(true))
				.andExpect(jsonPath("$.secondFactorDeliveryHint").value("SECOND_FACTOR_DELIVERY_HINT"))
		;
	}

	private void verifyUser(ResultActions resultActions) throws Exception {
		verifyUser(resultActions, null);
	}

	private void verifyUser(ResultActions resultActions, String prefix) throws Exception {
		if (StringUtils.isEmpty(prefix)) {
			prefix = "$";
		}
		resultActions
				.andExpect(jsonPath(prefix + ".id").value(100))
				.andExpect(jsonPath(prefix + ".username").value("USERNAME"))
				.andExpect(jsonPath(prefix + ".email").value("EMAIL"))
				.andExpect(jsonPath(prefix + ".name").value("NAME"))
				.andExpect(jsonPath(prefix + ".timezone").value("TIMEZONE"))
				.andExpect(jsonPath(prefix + ".privilege").value("VIP"))
				.andExpect(jsonPath(prefix + ".created").value(100))
				.andExpect(jsonPath(prefix + ".updated").value(200))
				.andExpect(jsonPath(prefix + ".deleted").value(300))
				.andExpect(jsonPath(prefix + ".active").value(true))
				.andExpect(jsonPath(prefix + ".shardId").value("SHARD_ID"))
				.andExpect(jsonPath(prefix + ".attributes.defaultLocationName").value("DEFAULT_LOCATION_NAME"))
				.andExpect(jsonPath(prefix + ".attributes.defaultLatitude").value(10.01))
				.andExpect(jsonPath(prefix + ".attributes.defaultLongitude").value(12.12))
				.andExpect(jsonPath(prefix + ".attributes.preactivation").value(true))
				.andExpect(jsonPath(prefix + ".attributes.viewedPromotions[0]").value("VIEWED_PROMOTIONS_1"))
				.andExpect(jsonPath(prefix + ".attributes.viewedPromotions[1]").value("VIEWED_PROMOTIONS_2"))
				.andExpect(jsonPath(prefix + ".attributes.incomingEmailAddress").value("INCOMING_EMAIL_ADDRESS"))
				.andExpect(jsonPath(prefix + ".attributes.recentMailedAddresses[0]").value("RECENT_MAILED_ADDRESSES_1"))
				.andExpect(jsonPath(prefix + ".attributes.recentMailedAddresses[1]").value("RECENT_MAILED_ADDRESSES_2"))
				.andExpect(jsonPath(prefix + ".attributes.comments").value("COMMENTS"))
				.andExpect(jsonPath(prefix + ".attributes.dateAgreedToTermsOfService").value(20))
				.andExpect(jsonPath(prefix + ".attributes.maxReferrals").value(30))
				.andExpect(jsonPath(prefix + ".attributes.referralCount").value(40))
				.andExpect(jsonPath(prefix + ".attributes.refererCode").value("REFERER_CODE"))
				.andExpect(jsonPath(prefix + ".attributes.sentEmailDate").value(200))
				.andExpect(jsonPath(prefix + ".attributes.sentEmailCount").value(50))
				.andExpect(jsonPath(prefix + ".attributes.dailyEmailLimit").value(60))
				.andExpect(jsonPath(prefix + ".attributes.emailOptOutDate").value(300))
				.andExpect(jsonPath(prefix + ".attributes.partnerEmailOptInDate").value(400))
				.andExpect(jsonPath(prefix + ".attributes.preferredLanguage").value("PREFERRED_LANGUAGE"))
				.andExpect(jsonPath(prefix + ".attributes.preferredCountry").value("PREFERRED_COUNTRY"))
				.andExpect(jsonPath(prefix + ".attributes.clipFullPage").value(true))
				.andExpect(jsonPath(prefix + ".attributes.twitterUserName").value("TWITTER_USER_NAME"))
				.andExpect(jsonPath(prefix + ".attributes.twitterId").value("TWITTER_ID"))
				.andExpect(jsonPath(prefix + ".attributes.groupName").value("GROUP_NAME"))
				.andExpect(jsonPath(prefix + ".attributes.recognitionLanguage").value("RECOGNITION_LANGUAGE"))
				.andExpect(jsonPath(prefix + ".attributes.referralProof").value("REFERRAL_PROOF"))
				.andExpect(jsonPath(prefix + ".attributes.educationalDiscount").value(true))
				.andExpect(jsonPath(prefix + ".attributes.businessAddress").value("BUSINESS_ADDRESS"))
				.andExpect(jsonPath(prefix + ".attributes.hideSponsorBilling").value(true))
				.andExpect(jsonPath(prefix + ".attributes.taxExempt").value(true))
				.andExpect(jsonPath(prefix + ".attributes.useEmailAutoFiling").value(true))
				.andExpect(jsonPath(prefix + ".attributes.reminderEmailConfig").value("SEND_DAILY_EMAIL"))
				.andExpect(jsonPath(prefix + ".accounting.uploadLimit").value(1000))
				.andExpect(jsonPath(prefix + ".accounting.uploadLimitEnd").value(1010))
				.andExpect(jsonPath(prefix + ".accounting.uploadLimitNextMonth").value(1020))
				.andExpect(jsonPath(prefix + ".accounting.premiumServiceStatus").value("ACTIVE"))
				.andExpect(jsonPath(prefix + ".accounting.premiumOrderNumber").value("PREMIUM_ORDER_NUMBER"))
				.andExpect(jsonPath(prefix + ".accounting.premiumCommerceService").value("PREMIUM_COMMERCE_SERVICE"))
				.andExpect(jsonPath(prefix + ".accounting.premiumServiceStart").value(1030))
				.andExpect(jsonPath(prefix + ".accounting.premiumServiceSKU").value("PREMIUM_SERVICE_SKU"))
				.andExpect(jsonPath(prefix + ".accounting.lastSuccessfulCharge").value(1040))
				.andExpect(jsonPath(prefix + ".accounting.lastFailedCharge").value(1050))
				.andExpect(jsonPath(prefix + ".accounting.lastFailedChargeReason").value("LAST_FAILED_CHARGE_REASON"))
				.andExpect(jsonPath(prefix + ".accounting.nextPaymentDue").value(1060))
				.andExpect(jsonPath(prefix + ".accounting.premiumLockUntil").value(1070))
				.andExpect(jsonPath(prefix + ".accounting.updated").value(1080))
				.andExpect(jsonPath(prefix + ".accounting.premiumSubscriptionNumber").value("PREMIUM_SUBSCRIPTION_NUMBER"))
				.andExpect(jsonPath(prefix + ".accounting.lastRequestedCharge").value(1090))
				.andExpect(jsonPath(prefix + ".accounting.currency").value("CURRENCY"))
				.andExpect(jsonPath(prefix + ".accounting.unitPrice").value(200))
				.andExpect(jsonPath(prefix + ".accounting.businessId").value(300))
				.andExpect(jsonPath(prefix + ".accounting.businessName").value("BUSINESS_NAME"))
				.andExpect(jsonPath(prefix + ".accounting.businessRole").value("ADMIN"))
				.andExpect(jsonPath(prefix + ".accounting.unitDiscount").value(400))
				.andExpect(jsonPath(prefix + ".accounting.nextChargeDate").value(1100))
				.andExpect(jsonPath(prefix + ".premiumInfo.currentTime").value(2000))
				.andExpect(jsonPath(prefix + ".premiumInfo.premium").value(true))
				.andExpect(jsonPath(prefix + ".premiumInfo.premiumRecurring").value(true))
				.andExpect(jsonPath(prefix + ".premiumInfo.premiumExpirationDate").value(2010))
				.andExpect(jsonPath(prefix + ".premiumInfo.premiumExtendable").value(true))
				.andExpect(jsonPath(prefix + ".premiumInfo.premiumPending").value(true))
				.andExpect(jsonPath(prefix + ".premiumInfo.premiumCancellationPending").value(true))
				.andExpect(jsonPath(prefix + ".premiumInfo.canPurchaseUploadAllowance").value(true))
				.andExpect(jsonPath(prefix + ".premiumInfo.sponsoredGroupName").value("SPONSORED_GROUP_NAME"))
				.andExpect(jsonPath(prefix + ".premiumInfo.sponsoredGroupRole").value("GROUP_ADMIN"))
				.andExpect(jsonPath(prefix + ".premiumInfo.premiumUpgradable").value(true))
				.andExpect(jsonPath(prefix + ".businessUserInfo.businessId").value(50))
				.andExpect(jsonPath(prefix + ".businessUserInfo.businessName").value("BUSINESS_NAME"))
				.andExpect(jsonPath(prefix + ".businessUserInfo.role").value("ADMIN"))
				.andExpect(jsonPath(prefix + ".businessUserInfo.email").value("EMAIL"))
		;
	}

}
