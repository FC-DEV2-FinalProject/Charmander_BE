package org.cm.security.auth.oauth;

public record OAuthProvider(
    String authorizationUri,
    String tokenUri,
    String userInfoUri
) {
}
