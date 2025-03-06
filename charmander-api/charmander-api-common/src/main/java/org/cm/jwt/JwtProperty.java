package org.cm.jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Getter
@Setter(AccessLevel.PROTECTED)
@Component
@ConfigurationProperties("jwt")
public class JwtProperty {
    @NestedConfigurationProperty
    private CookieProperty cookie = new CookieProperty();

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public static class CookieProperty {
        private String domain = "localhost";
    }
}
