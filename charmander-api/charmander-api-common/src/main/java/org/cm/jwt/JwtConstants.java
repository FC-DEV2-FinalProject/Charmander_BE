package org.cm.jwt;

import java.time.Duration;

public class JwtConstants {
    // @formatter:off
    public static final String ISSUER = "charmander.xyz";

    public static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(10);

    public static final Duration REFRESH_TOKEN_DURATION          = Duration.ofDays(7);
    public static final Duration REFRESH_TOKEN_REISSUE_THRESHOLD = Duration.ofDays(1);
    public static final String   REFRESH_TOKEN_COOKIE_PATH       = "/api/v1/auth/refresh";
    public static final String   REFRESH_TOKEN_COOKIE_NAME       = "refresh_token";
    public static final String   REFRESH_TOKEN_COOKIE_SAME_SITE  = "Lax";
    // @formatter:on
}
