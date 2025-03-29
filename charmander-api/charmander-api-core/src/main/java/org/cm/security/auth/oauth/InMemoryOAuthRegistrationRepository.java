package org.cm.security.auth.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryOAuthRegistrationRepository implements OAuthRegistrationRepository {
    private final Map<String, OAuthRegistration> registrations;

    @Override
    public OAuthRegistration findByName(String provider) {
        return registrations.getOrDefault(provider, null);
    }
}
