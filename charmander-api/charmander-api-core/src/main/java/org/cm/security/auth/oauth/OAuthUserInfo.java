package org.cm.security.auth.oauth;

import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("unchecked")
public record OAuthUserInfo(
    @NonNull
    String id,
    @Nullable
    String email
) {
    public static OAuthUserInfo of(String providerName, Map<String, ?> userInfo) {
        return switch (providerName) {
            case "google" -> ofGoogle(userInfo);
            case "kakao" -> ofKakao(userInfo);
            case "naver" -> ofNaver(userInfo);
            case "github" -> ofGithub(userInfo);
            default -> throw new IllegalArgumentException("Unknown provider: " + providerName);
        };
    }

    private static OAuthUserInfo ofGoogle(Map<String, ?> userInfo) {
        return new OAuthUserInfo(
            String.valueOf(userInfo.get("sub")),
            (String) userInfo.get("email")
        );
    }

    private static OAuthUserInfo ofKakao(Map<String, ?> userInfo) {
        var account = (Map<String, ?>) userInfo.get("kakao_account");
        return new OAuthUserInfo(
            String.valueOf(userInfo.get("id")),
            (String) account.getOrDefault("email", null)
        );
    }

    private static OAuthUserInfo ofNaver(Map<String, ?> userInfo) {
        var res = (Map<String, ?>) userInfo.get("response");
        return new OAuthUserInfo(
            String.valueOf(res.get("id")),
            (String) res.get("email")
        );
    }

    private static OAuthUserInfo ofGithub(Map<String, ?> userInfo) {
        return new OAuthUserInfo(
            String.valueOf(userInfo.get("id")),
            (String) userInfo.get("email")
        );
    }
}
