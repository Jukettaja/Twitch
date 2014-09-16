package com.deswaef.twitch.work.oauth;


import org.springframework.http.HttpStatus;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * User: Quinten
 * Date: 16-9-2014
 * Time: 14:24
 *
 * @author Quinten De Swaef
 */
public class AccessTokenFetcher {

    private String oauthTokenUrl = "/oauth2/token";
    private String baseUrl;

    private AccessTokenRequest requestTemplate;

    public AccessTokenFetcher() {
        requestTemplate = new AccessTokenRequest();
    }


    public AccessTokenResponse requestToken(String accessCode) {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter());
        try {
            return template.postForObject(
                    baseUrl + oauthTokenUrl,
                    requestTemplate.copy(accessCode),
                    AccessTokenResponse.class);
        } catch (HttpClientErrorException badRequestException) {
            if(badRequestException.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                return AccessTokenResponse.forbidden();
            }
            return AccessTokenResponse.invalid_code();
        } catch(Exception ex) {
            return AccessTokenResponse.unknown_issue();
        }
    }

    public AccessTokenFetcher setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public AccessTokenFetcher setClientId(String clientId) {
        this.requestTemplate.setClient_id(clientId);
        return this;
    }


    public AccessTokenFetcher setClientSecret(String clientSecret) {
        this.requestTemplate.setClient_secret(clientSecret);
        return this;
    }


    public AccessTokenFetcher setRedirectUrl(String redirectUrl) {
        this.requestTemplate.setRedirect_uri(redirectUrl);
        return this;
    }
}
