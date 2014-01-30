package net.ttddyy.evernote.rest;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author Tadaya Tsuyukubo
 */
public class ResultActionsUtils {

	public static void verifyAuthenticationResult(ResultActions resultActions) throws Exception {
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

	public static void verifyUser(ResultActions resultActions) throws Exception {
		verifyUser(resultActions, "$");
	}

	public static void verifyUser(ResultActions resultActions, String prefix) throws Exception {
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

	public static void verifyResource(ResultActions resultActions) throws Exception {
		verifyResource(resultActions, "$");
	}

	public static void verifyResource(ResultActions resultActions, String prefix) throws Exception {
		verifyData(resultActions, prefix + ".data");
		verifyData(resultActions, prefix + ".recognition");
		verifyData(resultActions, prefix + ".alternateData");
		verifyResourceAttribute(resultActions, prefix + ".attributes");

		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("RESOURCE_GUID"))
				.andExpect(jsonPath(prefix + ".noteGuid").value("RESOURCE_NOTE_GUID"))
				.andExpect(jsonPath(prefix + ".mime").value("RESOURCE_MIME"))
				.andExpect(jsonPath(prefix + ".width").value(10))
				.andExpect(jsonPath(prefix + ".height").value(20))
				.andExpect(jsonPath(prefix + ".duration").value(30))
				.andExpect(jsonPath(prefix + ".active").value(true))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(40))
		;
	}

	public static void verifyData(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".bodyHash").value("Rk9P"))  // "FOO"
				.andExpect(jsonPath(prefix + ".size").value(20))
				.andExpect(jsonPath(prefix + ".body").value("QkFS"))  // "BAR"
		;
	}

	public static void verifyResourceAttribute(ResultActions resultActions) throws Exception {
		verifyResourceAttribute(resultActions, "$");
	}

	public static void verifyResourceAttribute(ResultActions resultActions, String prefix) throws Exception {
		verifyApplicationData(resultActions, prefix + ".applicationData");
		resultActions
				.andExpect(jsonPath(prefix + ".sourceURL").value("RESOURCE_ATTR_SOURCE_URL"))
				.andExpect(jsonPath(prefix + ".timestamp").value(100))
				.andExpect(jsonPath(prefix + ".latitude").value(200.0))
				.andExpect(jsonPath(prefix + ".longitude").value(300.0))
				.andExpect(jsonPath(prefix + ".altitude").value(400.0))
				.andExpect(jsonPath(prefix + ".cameraMake").value("RESOURCE_ATTR_CAMERA_MAKE"))
				.andExpect(jsonPath(prefix + ".cameraModel").value("RESOURCE_ATTR_CAMERA_MODEL"))
				.andExpect(jsonPath(prefix + ".clientWillIndex").value(true))
				.andExpect(jsonPath(prefix + ".recoType").value("RESOURCE_ATTR_RECO_TYPE"))
				.andExpect(jsonPath(prefix + ".fileName").value("RESOURCE_ATTR_FILE_NAME"))
				.andExpect(jsonPath(prefix + ".attachment").value(true))
		;
	}

	public static void verifyApplicationData(ResultActions resultActions) throws Exception {
		verifyApplicationData(resultActions, "$");
	}

	public static void verifyApplicationData(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".keysOnly").isArray())
				.andExpect(jsonPath(prefix + ".keysOnly[0]").value("KEYS_ONLY_FOO"))
				.andExpect(jsonPath(prefix + ".keysOnly[1]").value("KEYS_ONLY_BAR"))
				.andExpect(jsonPath(prefix + ".fullMap.FULLMAP_FOO_KEY").value("FULLMAP_FOO_VALUE"))
				.andExpect(jsonPath(prefix + ".fullMap.FULLMAP_BAR_KEY").value("FULLMAP_BAR_VALUE"))
		;
	}


	public static void verifyNoteAttribute(ResultActions resultActions, String prefix) throws Exception {
		verifyApplicationData(resultActions, prefix + ".applicationData");
		resultActions
				.andExpect(jsonPath(prefix + ".subjectDate").value(30))
				.andExpect(jsonPath(prefix + ".latitude").value(31.0))
				.andExpect(jsonPath(prefix + ".longitude").value(32.0))
				.andExpect(jsonPath(prefix + ".altitude").value(33.0))
				.andExpect(jsonPath(prefix + ".author").value("NOTE_ATTRIBUTE_AUTHOR"))
				.andExpect(jsonPath(prefix + ".source").value("NOTE_ATTRIBUTE_SOURCE"))
				.andExpect(jsonPath(prefix + ".sourceURL").value("NOTE_ATTRIBUTE_URL"))
				.andExpect(jsonPath(prefix + ".sourceApplication").value("NOTE_ATTRIBUTE_SOURCE_APPLICATION"))
				.andExpect(jsonPath(prefix + ".shareDate").value(34))
				.andExpect(jsonPath(prefix + ".reminderOrder").value(35))
				.andExpect(jsonPath(prefix + ".reminderDoneTime").value(36))
				.andExpect(jsonPath(prefix + ".reminderTime").value(37))
				.andExpect(jsonPath(prefix + ".placeName").value("NOTE_ATTRIBUTE_PLACE_NAME"))
				.andExpect(jsonPath(prefix + ".contentClass").value("NOTE_ATTRIBUTE_CONTENT_CLASS"))
				.andExpect(jsonPath(prefix + ".lastEditedBy").value("NOTE_ATTRIBUTE_LAST_EDITED_BY"))
				.andExpect(jsonPath(prefix + ".classifications.CLASSIFICATION_FOO_KEY").value("CLASSIFICATION_FOO_VALUE"))
				.andExpect(jsonPath(prefix + ".classifications.CLASSIFICATION_BAR_KEY").value("CLASSIFICATION_BAR_VALUE"))
				.andExpect(jsonPath(prefix + ".creatorId").value(38))
				.andExpect(jsonPath(prefix + ".lastEditorId").value(39))
		;
	}

	public static void verifyNotebook(ResultActions resultActions) throws Exception {
		verifyNotebook(resultActions, "$");
	}

	public static void verifyNotebook(ResultActions resultActions, String prefix) throws Exception {
		verifyUser(resultActions, prefix + ".contact");

		resultActions.andExpect(jsonPath(prefix + ".sharedNotebooks").isArray());
		verifySharedNotebook1(resultActions, prefix + ".sharedNotebooks[0]");

		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("GUID"))
				.andExpect(jsonPath(prefix + ".name").value("NAME"))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(100))
				.andExpect(jsonPath(prefix + ".defaultNotebook").value(true))
				.andExpect(jsonPath(prefix + ".serviceCreated").value(200))
				.andExpect(jsonPath(prefix + ".serviceUpdated").value(300))
				.andExpect(jsonPath(prefix + ".publishing.uri").value("URI"))
				.andExpect(jsonPath(prefix + ".publishing.order").value("RELEVANCE"))
				.andExpect(jsonPath(prefix + ".publishing.ascending").value(true))
				.andExpect(jsonPath(prefix + ".publishing.publicDescription").value("PUBLIC_DESCRIPTION"))
				.andExpect(jsonPath(prefix + ".published").value(true))
				.andExpect(jsonPath(prefix + ".stack").value("STACK"))
				.andExpect(jsonPath(prefix + ".sharedNotebookIds").isArray())
				.andExpect(jsonPath(prefix + ".sharedNotebookIds[0]").value(10))
				.andExpect(jsonPath(prefix + ".sharedNotebookIds[1]").value(20))
				.andExpect(jsonPath(prefix + ".sharedNotebookIds[2]").value(30))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].id").value(2001))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].userId").value(2002))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].notebookGuid").value("SHARED_NOTEBOOK_2_GUID"))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].email").value("SHARED_NOTEBOOK_2_EMAIL"))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].notebookModifiable").value(true))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].requireLogin").value(true))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].serviceCreated").value(2003))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].serviceUpdated").value(2004))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].shareKey").value("SHARED_NOTEBOOK_2_SHARE_KEY"))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].username").value("SHARED_NOTEBOOK_2_USERNAME"))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].privilege").value("GROUP"))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].allowPreview").value(true))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].recipientSettings.reminderNotifyEmail").value(true))
				.andExpect(jsonPath(prefix + ".sharedNotebooks[1].recipientSettings.reminderNotifyInApp").value(true))
				.andExpect(jsonPath(prefix + ".businessNotebook.notebookDescription").value("BUSINESS_NOTEBOOK_DESC"))
				.andExpect(jsonPath(prefix + ".businessNotebook.privilege").value("READ_NOTEBOOK"))
				.andExpect(jsonPath(prefix + ".businessNotebook.recommended").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noReadNotes").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noCreateNotes").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noUpdateNotes").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noExpungeNotes").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noShareNotes").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noEmailNotes").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noSendMessageToRecipients").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noUpdateNotebook").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noExpungeNotebook").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noSetDefaultNotebook").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noSetNotebookStack").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noPublishToPublic").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noPublishToBusinessLibrary").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noCreateTags").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noUpdateTags").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noExpungeTags").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noSetParentTag").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.noCreateSharedNotebooks").value(true))
				.andExpect(jsonPath(prefix + ".restrictions.updateWhichSharedNotebookRestrictions").value("NO_SHARED_NOTEBOOKS"))
				.andExpect(jsonPath(prefix + ".restrictions.expungeWhichSharedNotebookRestrictions").value("ONLY_JOINED_OR_PREVIEW"))
		;
	}

	public static void verifySharedNotebook1(ResultActions resultActions) throws Exception {
		verifySharedNotebook1(resultActions, "$");
	}

	public static void verifySharedNotebook1(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".id").value(1001))
				.andExpect(jsonPath(prefix + ".userId").value(1002))
				.andExpect(jsonPath(prefix + ".notebookGuid").value("SHARED_NOTEBOOK_1_GUID"))
				.andExpect(jsonPath(prefix + ".email").value("SHARED_NOTEBOOK_1_EMAIL"))
				.andExpect(jsonPath(prefix + ".notebookModifiable").value(true))
				.andExpect(jsonPath(prefix + ".requireLogin").value(true))
				.andExpect(jsonPath(prefix + ".serviceCreated").value(1003))
				.andExpect(jsonPath(prefix + ".serviceUpdated").value(1004))
				.andExpect(jsonPath(prefix + ".shareKey").value("SHARED_NOTEBOOK_1_SHARE_KEY"))
				.andExpect(jsonPath(prefix + ".username").value("SHARED_NOTEBOOK_1_USERNAME"))
				.andExpect(jsonPath(prefix + ".privilege").value("FULL_ACCESS"))
				.andExpect(jsonPath(prefix + ".allowPreview").value(true))
				.andExpect(jsonPath(prefix + ".recipientSettings.reminderNotifyEmail").value(true))
				.andExpect(jsonPath(prefix + ".recipientSettings.reminderNotifyInApp").value(true))
		;
	}

	public static void verifyNote(ResultActions resultActions) throws Exception {
		verifyNote(resultActions, "$");
	}

	public static void verifyNote(ResultActions resultActions, String prefix) throws Exception {
		resultActions.andExpect(jsonPath(prefix + ".resources").isArray());
		verifyResource(resultActions, prefix + ".resources[0]");
		verifyResource(resultActions, prefix + ".resources[1]");
		verifyNoteAttribute(resultActions, prefix + ".attributes");

		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("NOTE_GUID"))
				.andExpect(jsonPath(prefix + ".title").value("NOTE_TITLE"))
				.andExpect(jsonPath(prefix + ".content").value("NOTE_CONTENT"))
				.andExpect(jsonPath(prefix + ".contentHash").value("Rk9P"))
				.andExpect(jsonPath(prefix + ".contentLength").value(10))
				.andExpect(jsonPath(prefix + ".created").value(11))
				.andExpect(jsonPath(prefix + ".updated").value(12))
				.andExpect(jsonPath(prefix + ".deleted").value(13))
				.andExpect(jsonPath(prefix + ".active").value(true))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(14))
				.andExpect(jsonPath(prefix + ".notebookGuid").value("NOTEBOOK_GUID"))
				.andExpect(jsonPath(prefix + ".tagGuids").isArray())
				.andExpect(jsonPath(prefix + ".tagGuids[0]").value("TAG_GUID_FOO"))
				.andExpect(jsonPath(prefix + ".tagGuids[1]").value("TAG_GUID_BAR"))
				.andExpect(jsonPath(prefix + ".tagNames").isArray())
				.andExpect(jsonPath(prefix + ".tagNames[0]").value("NOTE_TAG_FOO"))
				.andExpect(jsonPath(prefix + ".tagNames[1]").value("NOTE_TAG_BAR"))
		;
	}

	public static void verifySyncChunk(ResultActions resultActions) throws Exception {
		resultActions.andExpect(jsonPath("$.notes").isArray());
		verifyNote(resultActions, "$.notes[0]");

		resultActions.andExpect(jsonPath("$.notebooks").isArray());
		verifyNotebook(resultActions, "$.notebooks[0]");

		resultActions.andExpect(jsonPath("$.resources").isArray());
		verifyResource(resultActions, "$.resources[0]");

		resultActions.andExpect(jsonPath("$.tags").isArray());
		verifyTag(resultActions, "$.tags[0]");

		resultActions.andExpect(jsonPath("$.searches").isArray());
		verifySavedSearch(resultActions, "$.searches[0]");

		resultActions.andExpect(jsonPath("$.linkedNotebooks").isArray());
		verifyLinkedNotebook(resultActions, "$.linkedNotebooks[0]");

		resultActions
				.andExpect(jsonPath("$.currentTime").value(10))
				.andExpect(jsonPath("$.chunkHighUSN").value(20))
				.andExpect(jsonPath("$.updateCount").value(30))
				.andExpect(jsonPath("$.expungedNotes").isArray())
				.andExpect(jsonPath("$.expungedNotes[0]").value("EXPUNGED_NOTE"))
				.andExpect(jsonPath("$.expungedNotebooks").isArray())
				.andExpect(jsonPath("$.expungedNotebooks[0]").value("EXPUNGED_NOTEBOOK"))
				.andExpect(jsonPath("$.expungedTags").isArray())
				.andExpect(jsonPath("$.expungedTags[0]").value("EXPUNGED_TAG"))
				.andExpect(jsonPath("$.expungedSearches").isArray())
				.andExpect(jsonPath("$.expungedSearches[0]").value("EXPUNGED_SEARCHES"))
				.andExpect(jsonPath("$.linkedNotebooks").isArray())
				.andExpect(jsonPath("$.expungedLinkedNotebooks").isArray())
				.andExpect(jsonPath("$.expungedLinkedNotebooks[0]").value("EXPUNGED_LINKED_NOTEBOOK"))
		;
	}

	public static void verifyLinkedNotebook(ResultActions resultActions) throws Exception {
		verifyLinkedNotebook(resultActions, "$");
	}

	public static void verifyLinkedNotebook(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".shareName").value("SHARE_NAME"))
				.andExpect(jsonPath(prefix + ".username").value("USERNAME"))
				.andExpect(jsonPath(prefix + ".shardId").value("SHARD_ID"))
				.andExpect(jsonPath(prefix + ".shareKey").value("SHARE_KEY"))
				.andExpect(jsonPath(prefix + ".uri").value("URI"))
				.andExpect(jsonPath(prefix + ".guid").value("GUID"))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(100))
				.andExpect(jsonPath(prefix + ".noteStoreUrl").value("NOTE_STORE_URL"))
				.andExpect(jsonPath(prefix + ".webApiUrlPrefix").value("WEB_API_URL_PREFIX"))
				.andExpect(jsonPath(prefix + ".stack").value("STACK"))
				.andExpect(jsonPath(prefix + ".businessId").value(200))
		;
	}

	public static void verifySyncState(ResultActions resultActions) throws Exception {
		resultActions
				.andExpect(jsonPath("$.currentTime").value(100))
				.andExpect(jsonPath("$.fullSyncBefore").value(200))
				.andExpect(jsonPath("$.updateCount").value(30))
				.andExpect(jsonPath("$.uploaded").value(400))
		;
	}

	public static void verifyTag(ResultActions resultActions) throws Exception {
		verifyTag(resultActions, "$");
	}

	public static void verifyTag(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("TAG_GUID"))
				.andExpect(jsonPath(prefix + ".name").value("TAG_NAME"))
				.andExpect(jsonPath(prefix + ".parentGuid").value("TAG_PARENT_GUID"))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(500))
		;
	}

	public static void verifySavedSearch(ResultActions resultActions) throws Exception {
		verifySavedSearch(resultActions, "$");
	}

	public static void verifySavedSearch(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("SAVEDSEARCH_GUID"))
				.andExpect(jsonPath(prefix + ".name").value("SAVEDSEARCH_NAME"))
				.andExpect(jsonPath(prefix + ".query").value("SAVEDSEARCH_QUERY"))
				.andExpect(jsonPath(prefix + ".format").value("USER"))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(200))
				.andExpect(jsonPath(prefix + ".scope.includeAccount").value(true))
				.andExpect(jsonPath(prefix + ".scope.includePersonalLinkedNotebooks").value(true))
				.andExpect(jsonPath(prefix + ".scope.includeBusinessLinkedNotebooks").value(true))
		;
	}

	public static void verifyNoteMetadata(ResultActions resultActions, String prefix) throws Exception {
		verifyNoteAttribute(resultActions, prefix + ".attributes");
		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("NOTE_METADATA_GUID"))
				.andExpect(jsonPath(prefix + ".title").value("NOTE_METADATA_TITLE"))
				.andExpect(jsonPath(prefix + ".contentLength").value(10))
				.andExpect(jsonPath(prefix + ".created").value(100))
				.andExpect(jsonPath(prefix + ".updated").value(200))
				.andExpect(jsonPath(prefix + ".deleted").value(300))
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(20))
				.andExpect(jsonPath(prefix + ".notebookGuid").value("NOTE_METADATA_NOTEBOOK_GUID"))
				.andExpect(jsonPath(prefix + ".tagGuids").isArray())
				.andExpect(jsonPath(prefix + ".tagGuids[0]").value("NOTE_METADATA_TAG_ID_1"))
				.andExpect(jsonPath(prefix + ".tagGuids[1]").value("NOTE_METADATA_TAG_ID_2"))
				.andExpect(jsonPath(prefix + ".largestResourceMime").value("NOTE_METADATA_LARGEST_RESOURCE_MIME"))
				.andExpect(jsonPath(prefix + ".largestResourceSize").value(30))
		;
	}

	public static void verifyNoteVersionId(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".updateSequenceNum").value(5))
				.andExpect(jsonPath(prefix + ".updated").value(100))
				.andExpect(jsonPath(prefix + ".saved").value(200))
				.andExpect(jsonPath(prefix + ".title").value("NOTE_VERSION_ID_TITLE"))
		;
	}

	public static void verifyNotebookDescriptor(ResultActions resultActions, String prefix) throws Exception {
		resultActions
				.andExpect(jsonPath(prefix + ".guid").value("NOTEBOOK_DESCRIPTOR_GUID"))
				.andExpect(jsonPath(prefix + ".notebookDisplayName").value("NOTEBOOK_DESCRIPTOR_DISPLAY_NAME"))
				.andExpect(jsonPath(prefix + ".contactName").value("NOTEBOOK_DESCRIPTOR_CONTACT_NAME"))
				.andExpect(jsonPath(prefix + ".hasSharedNotebook").value(true))
				.andExpect(jsonPath(prefix + ".joinedUserCount").value(100))
		;
	}
}
