package net.ttddyy.evernote.rest;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to create domain objects for test.
 *
 * @author Tadaya Tsuyukubo
 */
public class TestDomainUtils {

	public static LinkedNotebook getLinkedNotebook() {
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

	public static SavedSearch getSavedSearch() {
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

	public static Tag getTag() {
		Tag tag = new Tag();
		tag.setGuid("TAG_GUID");
		tag.setName("TAG_NAME");
		tag.setParentGuid("TAG_PARENT_GUID");
		tag.setUpdateSequenceNum(500);
		return tag;
	}

	public static NoteFilter getNoteFilter() {
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

	public static Data getData() {
		Data data = new Data();
		data.setBodyHash(new byte[]{70, 79, 79});  // "FOO", base64 encoded value is "Rk9P"
		data.setSize(20);
		data.setBody(new byte[]{66, 65, 82});  // "BAR", base64 encoded value is "QkFS"
		return data;
	}

	public static SharedNotebookRecipientSettings getSharedNotebookRecipientSettings() {
		SharedNotebookRecipientSettings setting = new SharedNotebookRecipientSettings();
		setting.setReminderNotifyEmail(true);
		setting.setReminderNotifyInApp(true);
		return setting;
	}

	public static SharedNotebook getSharedNotebook1() {
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

	public static SharedNotebook getSharedNotebook2() {
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

	public static Notebook getNotebookFull() {
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
