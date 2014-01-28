package net.ttddyy.evernote.rest;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.*;
import com.evernote.edam.userstore.AuthenticationResult;
import com.evernote.edam.userstore.PublicUserInfo;

import java.util.*;

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
		data.setBodyHash(getByteFoo());  // "FOO", base64 encoded value is "Rk9P"
		data.setSize(20);
		data.setBody(getByteBAR());  // "BAR", base64 encoded value is "QkFS"
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


	public static Resource getResource() {
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
		return resource;
	}

	public static Note getNote() {
		Set<String> keysOnly = new HashSet<String>();
		keysOnly.add("KEYS_ONLY_FOO");
		keysOnly.add("KEYS_ONLY_BAR");

		Map<String, String> fullMap = new HashMap<String, String>();
		fullMap.put("FULLMAP_FOO_KEY", "FULLMAP_FOO_VALUE");
		fullMap.put("FULLMAP_BAR_KEY", "FULLMAP_BAR_VALUE");

		LazyMap applicationData = new LazyMap();
		applicationData.setKeysOnly(keysOnly);
		applicationData.setFullMap(fullMap);

		Map<String, String> classifications = new HashMap<String, String>();
		classifications.put("CLASSIFICATION_FOO_KEY", "CLASSIFICATION_FOO_VALUE");
		classifications.put("CLASSIFICATION_BAR_KEY", "CLASSIFICATION_BAR_VALUE");

		NoteAttributes attributes = new NoteAttributes();
		attributes.setSubjectDate(30);
		attributes.setLatitude(31);
		attributes.setLongitude(32);
		attributes.setAltitude(33);
		attributes.setAuthor("NOTE_ATTRIBUTE_AUTHOR");
		attributes.setSource("NOTE_ATTRIBUTE_SOURCE");
		attributes.setSourceURL("NOTE_ATTRIBUTE_URL");
		attributes.setSourceApplication("NOTE_ATTRIBUTE_SOURCE_APPLICATION");
		attributes.setShareDate(34);
		attributes.setReminderOrder(35);
		attributes.setReminderDoneTime(36);
		attributes.setReminderTime(37);
		attributes.setPlaceName("NOTE_ATTRIBUTE_PLACE_NAME");
		attributes.setContentClass("NOTE_ATTRIBUTE_CONTENT_CLASS");
		attributes.setApplicationData(applicationData);
		attributes.setLastEditedBy("NOTE_ATTRIBUTE_LAST_EDITED_BY");
		attributes.setClassifications(classifications);
		attributes.setCreatorId(38);
		attributes.setLastEditorId(39);

		Note note = new Note();
		note.setGuid("NOTE_GUID");
		note.setTitle("NOTE_TITLE");
		note.setContent("NOTE_CONTENT");
		note.setContentHash(getByteFoo());
		note.setContentLength(10);
		note.setCreated(11);
		note.setUpdated(12);
		note.setDeleted(13);
		note.setActive(true);
		note.setUpdateSequenceNum(14);
		note.setNotebookGuid("NOTEBOOK_GUID");
		note.setTagGuids(Arrays.asList("TAG_GUID_FOO", "TAG_GUID_BAR"));
		note.setResources(Arrays.asList(getResource(), getResource()));
		note.setAttributes(attributes);
		note.setTagNames(Arrays.asList("NOTE_TAG_FOO", "NOTE_TAG_BAR"));
		return note;
	}

	public static byte[] getByteFoo() {
		return new byte[]{70, 79, 79};  // "FOO" as byte. base64 encoded value is "Rk9P"
	}

	public static byte[] getByteBAR() {
		return new byte[]{66, 65, 82};  // "BAR" as byte. "BAR", base64 encoded value is "QkFS"
	}

	public static PublicUserInfo getPublicUserInfo() {
		PublicUserInfo publicUserInfo = new PublicUserInfo();
		publicUserInfo.setUserId(100);
		publicUserInfo.setShardId("SHARDID");
		publicUserInfo.setPrivilege(PrivilegeLevel.VIP);
		publicUserInfo.setUsername("USERNAME");
		publicUserInfo.setNoteStoreUrl("NOTE_STORE_URL");
		publicUserInfo.setWebApiUrlPrefix("WEB_API_URL_PREFIX");
		return publicUserInfo;
	}

	public static User getUser() {

		UserAttributes attributes = new UserAttributes();
		attributes.setDefaultLocationName("DEFAULT_LOCATION_NAME");
		attributes.setDefaultLatitude(10.01);
		attributes.setDefaultLongitude(12.12);
		attributes.setPreactivation(true);
		attributes.setViewedPromotions(Arrays.asList("VIEWED_PROMOTIONS_1", "VIEWED_PROMOTIONS_2"));
		attributes.setIncomingEmailAddress("INCOMING_EMAIL_ADDRESS");
		attributes.setRecentMailedAddresses(Arrays.asList("RECENT_MAILED_ADDRESSES_1", "RECENT_MAILED_ADDRESSES_2"));
		attributes.setComments("COMMENTS");
		attributes.setDateAgreedToTermsOfService(20L);
		attributes.setMaxReferrals(30);
		attributes.setReferralCount(40);
		attributes.setRefererCode("REFERER_CODE");
		attributes.setSentEmailDate(200L);
		attributes.setSentEmailCount(50);
		attributes.setDailyEmailLimit(60);
		attributes.setEmailOptOutDate(300L);
		attributes.setPartnerEmailOptInDate(400L);
		attributes.setPreferredLanguage("PREFERRED_LANGUAGE");
		attributes.setPreferredCountry("PREFERRED_COUNTRY");
		attributes.setClipFullPage(true);
		attributes.setTwitterUserName("TWITTER_USER_NAME");
		attributes.setTwitterId("TWITTER_ID");
		attributes.setGroupName("GROUP_NAME");
		attributes.setRecognitionLanguage("RECOGNITION_LANGUAGE");
		attributes.setReferralProof("REFERRAL_PROOF");
		attributes.setEducationalDiscount(true);
		attributes.setBusinessAddress("BUSINESS_ADDRESS");
		attributes.setHideSponsorBilling(true);
		attributes.setTaxExempt(true);
		attributes.setUseEmailAutoFiling(true);
		attributes.setReminderEmailConfig(ReminderEmailConfig.SEND_DAILY_EMAIL);

		Accounting accounting = new Accounting();
		accounting.setUploadLimit(1000L);
		accounting.setUploadLimitEnd(1010L);
		accounting.setUploadLimitNextMonth(1020L);
		accounting.setPremiumServiceStatus(PremiumOrderStatus.ACTIVE);
		accounting.setPremiumOrderNumber("PREMIUM_ORDER_NUMBER");
		accounting.setPremiumCommerceService("PREMIUM_COMMERCE_SERVICE");
		accounting.setPremiumServiceStart(1030L);
		accounting.setPremiumServiceSKU("PREMIUM_SERVICE_SKU");
		accounting.setLastSuccessfulCharge(1040L);
		accounting.setLastFailedCharge(1050L);
		accounting.setLastFailedChargeReason("LAST_FAILED_CHARGE_REASON");
		accounting.setNextPaymentDue(1060L);
		accounting.setPremiumLockUntil(1070L);
		accounting.setUpdated(1080L);
		accounting.setPremiumSubscriptionNumber("PREMIUM_SUBSCRIPTION_NUMBER");
		accounting.setLastRequestedCharge(1090L);
		accounting.setCurrency("CURRENCY");
		accounting.setUnitPrice(200);
		accounting.setBusinessId(300);
		accounting.setBusinessName("BUSINESS_NAME");
		accounting.setBusinessRole(BusinessUserRole.ADMIN);
		accounting.setUnitDiscount(400);
		accounting.setNextChargeDate(1100L);

		BusinessUserInfo businessUserInfo = new BusinessUserInfo();
		businessUserInfo.setBusinessId(50);
		businessUserInfo.setBusinessName("BUSINESS_NAME");
		businessUserInfo.setRole(BusinessUserRole.ADMIN);
		businessUserInfo.setEmail("EMAIL");

		User user = new User();
		user.setId(100);
		user.setUsername("USERNAME");
		user.setEmail("EMAIL");
		user.setName("NAME");
		user.setTimezone("TIMEZONE");
		user.setPrivilege(PrivilegeLevel.VIP);
		user.setCreated(100);
		user.setUpdated(200);
		user.setDeleted(300);
		user.setActive(true);
		user.setShardId("SHARD_ID");
		user.setAttributes(attributes);
		user.setAccounting(accounting);
		user.setPremiumInfo(getPremiumInfo());
		user.setBusinessUserInfo(businessUserInfo);

		return user;
	}

	public static AuthenticationResult getAuthenticationResult() {

		AuthenticationResult authenticationResult = new AuthenticationResult();
		authenticationResult.setCurrentTime(100L);
		authenticationResult.setAuthenticationToken("AUTHENTICATION_TOKEN");
		authenticationResult.setExpiration(200L);
		authenticationResult.setUser(getUser());
		authenticationResult.setPublicUserInfo(getPublicUserInfo());
		authenticationResult.setNoteStoreUrl("NOTE_STORE_URL");
		authenticationResult.setWebApiUrlPrefix("WEB_API_URL_PREFIX");
		authenticationResult.setSecondFactorRequired(true);
		authenticationResult.setSecondFactorDeliveryHint("SECOND_FACTOR_DELIVERY_HINT");

		return authenticationResult;
	}

	public static PremiumInfo getPremiumInfo() {
		PremiumInfo premiumInfo = new PremiumInfo();
		premiumInfo.setCurrentTime(2000L);
		premiumInfo.setPremium(true);
		premiumInfo.setPremiumRecurring(true);
		premiumInfo.setPremiumExpirationDate(2010L);
		premiumInfo.setPremiumExtendable(true);
		premiumInfo.setPremiumPending(true);
		premiumInfo.setPremiumCancellationPending(true);
		premiumInfo.setCanPurchaseUploadAllowance(true);
		premiumInfo.setSponsoredGroupName("SPONSORED_GROUP_NAME");
		premiumInfo.setSponsoredGroupRole(SponsoredGroupRole.GROUP_ADMIN);
		premiumInfo.setPremiumUpgradable(true);
		return premiumInfo;
	}
}
