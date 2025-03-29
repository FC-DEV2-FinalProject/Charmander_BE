package org.cm.security.auth.oauth;

import java.util.Map;

public interface OAuthClient {
    String getAuthorizeUri(String providerName, String state);

    Map<String, ?> getToken(String providerName, String token, String state);

    OAuthUserInfo getUserInfo(String providerName, Map<String, ?> token, String state);
}
