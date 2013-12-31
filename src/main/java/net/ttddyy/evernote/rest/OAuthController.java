package net.ttddyy.evernote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.evernote.connect.EvernoteConnectionFactory;
import org.springframework.social.evernote.connect.EvernoteOAuthToken;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tadaya Tsuyukubo
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

	@Autowired
	private EvernoteConnectionFactory evernoteConnectionFactory;

	@RequestMapping("/auth")
	public Map<String, String> authorization(@RequestParam String callbackUrl,
	                                         @RequestParam(required = false) boolean preferRegistration) {

		// obtain request token (temporal credential)
		final OAuth1Operations oauthOperations = this.evernoteConnectionFactory.getOAuthOperations();
		final OAuthToken requestToken = oauthOperations.fetchRequestToken(callbackUrl, null); // no additional param

		// construct authorization url with callback url for client to redirect
		final OAuth1Parameters parameters = new OAuth1Parameters();
		if (preferRegistration) {
			parameters.set("preferRegistration", "true");  // create account
		}
		final String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), parameters);

		final Map<String, String> map = new HashMap<String, String>();
		map.put("authorizeUrl", authorizeUrl);
		map.put("requestTokenValue", requestToken.getValue());
		map.put("requestTokenSecret", requestToken.getSecret());
		return map;
	}


	@RequestMapping("/accessToken")
	public EvernoteOAuthToken obtainAccessToken(@RequestParam String oauthToken, @RequestParam String oauthVerifier,
	                                            @RequestParam String requestTokenSecret) {
		final OAuthToken requestToken = new OAuthToken(oauthToken, requestTokenSecret);
		final AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(requestToken, oauthVerifier);

		final OAuth1Operations oAuth1Operations = this.evernoteConnectionFactory.getOAuthOperations();  // EvernoteOAuth1Operations
		final OAuthToken accessToken = oAuth1Operations.exchangeForAccessToken(authorizedRequestToken, null);  // no additional param
		return (EvernoteOAuthToken) accessToken;
	}

}
