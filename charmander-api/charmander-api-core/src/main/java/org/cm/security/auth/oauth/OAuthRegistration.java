package org.cm.security.auth.oauth;

public record OAuthRegistration(
    String clientId,
    String clientSecret,
    String redirectUri,
    String scope
) {
}
