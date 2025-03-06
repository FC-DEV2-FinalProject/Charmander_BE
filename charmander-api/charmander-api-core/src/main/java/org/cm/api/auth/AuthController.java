package org.cm.api.auth;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.cm.api.auth.dto.LoginRequest;
import org.cm.api.auth.dto.LoginResponse;
import org.cm.jwt.JwtConstants;
import org.cm.jwt.JwtProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtProperty jwtProperty;

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse servletResponse) {
        var loginResponse = authService.login(loginRequest);
        addRefreshTokenCookie(servletResponse, loginResponse.refreshToken());
        return loginResponse;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse servletResponse) {
        authService.logout();
        revokeRefreshTokenCookie(servletResponse);
    }

    private void addRefreshTokenCookie(HttpServletResponse servletResponse, String token) {
        var cookie = ResponseCookie
            .from(JwtConstants.REFRESH_TOKEN_COOKIE_NAME)
            .value(token)
            .secure(true)
            .httpOnly(true)
            .domain(jwtProperty.getCookie().getDomain())
            .path(JwtConstants.REFRESH_TOKEN_COOKIE_PATH)
            .maxAge(JwtConstants.REFRESH_TOKEN_DURATION)
            .sameSite(JwtConstants.REFRESH_TOKEN_COOKIE_SAME_SITE)
            .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void revokeRefreshTokenCookie(HttpServletResponse servletResponse) {
        var cookie = ResponseCookie
            .from(JwtConstants.REFRESH_TOKEN_COOKIE_NAME)
            .secure(true)
            .httpOnly(true)
            .domain(jwtProperty.getCookie().getDomain())
            .path("/")
            .maxAge(Duration.ZERO)
            .sameSite(JwtConstants.REFRESH_TOKEN_COOKIE_SAME_SITE)
            .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
