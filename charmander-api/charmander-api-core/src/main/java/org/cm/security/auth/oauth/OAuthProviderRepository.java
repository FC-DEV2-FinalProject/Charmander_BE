package org.cm.security.auth.oauth;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface OAuthProviderRepository {
    @Nullable
    OAuthProvider findByName(String name);

    @NonNull
    default OAuthProvider getByName(String name) {
        return Optional.ofNullable(findByName(name)).orElseThrow(() -> new RuntimeException("OAuthProviderNotFoundException"));
    }
}
