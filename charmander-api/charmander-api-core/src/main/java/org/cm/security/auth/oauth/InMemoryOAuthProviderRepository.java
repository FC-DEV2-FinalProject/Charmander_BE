package org.cm.security.auth.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryOAuthProviderRepository implements OAuthProviderRepository {
    private final Map<String, OAuthProvider> providers;

    @Override
    public OAuthProvider findByName(String name) {
        return providers.getOrDefault(name, null);
    }
}
