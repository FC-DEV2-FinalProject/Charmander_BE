package org.cm.security.auth.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.utils.URLUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class DefaultOAuthClient implements OAuthClient {
    private final OAuthProviderRepository providerRepository;
    private final OAuthRegistrationRepository registrationRepository;

    private final RestClient restClient = RestClient.create();

    @Override
    public String getAuthorizeUri(String providerName, String state) {
        var provider = providerRepository.getByName(providerName);
        var registration = registrationRepository.getByName(providerName);

        var params = Map.of(
            "client_id", registration.clientId(),
            "redirect_uri", registration.redirectUri(),
            "response_type", "code",
            "scope", registration.scope(),
            "state", state
        );
        var qs = URLUtils.buildQueryString(params);
        return provider.authorizationUri() + "?" + qs;
    }

    @Override
    public Map<String, ?> getToken(String providerName, String token, String state) {
        var provider = providerRepository.getByName(providerName);
        var registration = registrationRepository.getByName(providerName);

        var tokenRequest = Map.of(
            "grant_type", "authorization_code",
            "client_id", registration.clientId(),
            "client_secret", registration.clientSecret(),
            "redirect_uri", registration.redirectUri(),
            "code", token
        );
        var qs = URLUtils.buildQueryString(tokenRequest);
        var res = restClient
            .post()
            .uri(provider.tokenUri())
            .body(qs)
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("charset", "utf-8")
            .retrieve();
        var tokenResponse = res.body(Map.class);
        if (tokenResponse.containsKey("error")) {
            log.error("Failed to get token: {}", tokenResponse);
            throw new RuntimeException("Failed to get token");
        }
        return tokenResponse;
    }

    @Override
    public OAuthUserInfo getUserInfo(String providerName, Map<String, ?> token, String state) {
        var provider = providerRepository.getByName(providerName);

        var tokenType = (String) token.get("token_type");
        var accessToken = (String) token.get("access_token");
        var res = restClient
            .get()
            .uri(provider.userInfoUri())
            .header("Accept", "application/json")
            .header("Authorization", tokenType + " " + accessToken)
            .retrieve();

        @SuppressWarnings("unchecked")
        var body = (Map<String, ?>) res.body(Map.class);
        return OAuthUserInfo.of(providerName, body);
    }
}
