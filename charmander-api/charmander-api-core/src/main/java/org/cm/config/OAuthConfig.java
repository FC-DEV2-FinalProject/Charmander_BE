package org.cm.config;

import java.util.Collections;
import java.util.Map;
import org.cm.security.auth.oauth.DefaultOAuthClient;
import org.cm.security.auth.oauth.InMemoryOAuthProviderRepository;
import org.cm.security.auth.oauth.InMemoryOAuthRegistrationRepository;
import org.cm.security.auth.oauth.OAuthClient;
import org.cm.security.auth.oauth.OAuthProvider;
import org.cm.security.auth.oauth.OAuthProviderRepository;
import org.cm.security.auth.oauth.OAuthRegistration;
import org.cm.security.auth.oauth.OAuthRegistrationRepository;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OAuthConfig {
    @Bean
    public OAuthClient oAuthClient(
        OAuthProviderRepository providerRepository,
        OAuthRegistrationRepository registrationRepository
    ) {
        return new DefaultOAuthClient(providerRepository, registrationRepository);
    }

    @Configuration
    public static class OAuthProviderConfig {
        private static final String PROPERTY_PATH = "oauth.client.provider";
        private static final Bindable<Map<String, OAuthProvider>> STRING_PROVIDER_MAP = Bindable.mapOf(String.class, OAuthProvider.class);

        @Bean
        public OAuthProviderRepository oAuthProviderRepository(Environment env) {
            var providers = Binder.get(env).bind(PROPERTY_PATH, STRING_PROVIDER_MAP).orElse(Collections.emptyMap());
            return new InMemoryOAuthProviderRepository(providers);
        }
    }

    @Configuration
    public static class OAuthRegistrationConfig {
        private static final String PROPERTY_PATH = "oauth.client.registration";
        private static final Bindable<Map<String, OAuthRegistration>> STRING_REGISTRATION_MAP = Bindable.mapOf(String.class, OAuthRegistration.class);

        @Bean
        public OAuthRegistrationRepository oAuthRegistrationRepository(Environment env) {
            var registrations = Binder.get(env).bind(PROPERTY_PATH, STRING_REGISTRATION_MAP).orElse(Collections.emptyMap());
            return new InMemoryOAuthRegistrationRepository(registrations);
        }
    }
}
