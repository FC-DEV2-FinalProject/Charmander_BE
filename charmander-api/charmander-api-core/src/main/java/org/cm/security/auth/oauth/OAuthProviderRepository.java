package org.cm.security.auth.oauth;

import java.util.Optional;

import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface OAuthProviderRepository {
    @Nullable
    OAuthProvider findByName(String name);

    @NonNull
    default OAuthProvider getByName(String name) {
        return Optional.ofNullable(findByName(name))
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.OAUTH_PROVIDER_NOT_FOUND));
    }
}
