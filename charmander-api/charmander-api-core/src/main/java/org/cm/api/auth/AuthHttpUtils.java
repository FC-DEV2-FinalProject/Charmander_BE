package org.cm.api.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.cm.jwt.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

@UtilityClass
public class AuthHttpUtils {
    public void addRefreshTokenCookie(HttpServletResponse servletResponse, String token) {
        var cookie = ResponseCookie
            .from(JwtConstants.REFRESH_TOKEN_COOKIE_NAME)
            .value(token)
            .secure(true)
            .httpOnly(true)
            .path(JwtConstants.REFRESH_TOKEN_COOKIE_PATH)
            .maxAge(JwtConstants.REFRESH_TOKEN_DURATION)
            .sameSite(JwtConstants.REFRESH_TOKEN_COOKIE_SAME_SITE)
            .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void revokeRefreshTokenCookie(HttpServletResponse servletResponse) {
        var cookie = ResponseCookie
            .from(JwtConstants.REFRESH_TOKEN_COOKIE_NAME)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(Duration.ZERO)
            .sameSite(JwtConstants.REFRESH_TOKEN_COOKIE_SAME_SITE)
            .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
