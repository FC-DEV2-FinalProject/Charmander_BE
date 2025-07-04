package org.cm.security.auth.oauth;

import java.util.Optional;

import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface OAuthRegistrationRepository {
    @Nullable
    OAuthRegistration findByName(String provider);

    @NonNull
    default OAuthRegistration getByName(String provider) {
        return Optional.ofNullable(findByName(provider))
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.OAUTH_REGISTRATION_NOT_FOUND));
    }
}
