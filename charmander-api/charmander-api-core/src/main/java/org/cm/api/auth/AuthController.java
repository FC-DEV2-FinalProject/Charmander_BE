package org.cm.api.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.cm.api.auth.dto.LoginRequest;
import org.cm.api.auth.dto.LoginResponse;
import org.cm.jwt.JwtConstants;
import org.jspecify.annotations.Nullable;
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
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse servletResponse) {
        var loginResponse = authService.login(loginRequest);
        AuthHttpUtils.addRefreshTokenCookie(servletResponse, loginResponse.refreshToken());
        return loginResponse;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse servletResponse) {
        authService.logout();
        AuthHttpUtils.revokeRefreshTokenCookie(servletResponse);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        var loginResponse = Optional.ofNullable(servletRequest.getCookies())
            .stream()
            .flatMap(Arrays::stream)
            .filter((cookie) -> JwtConstants.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .map(authService::refresh)
            .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        AuthHttpUtils.addRefreshTokenCookie(servletResponse, loginResponse.refreshToken());
        return loginResponse;
    }
}
