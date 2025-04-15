package org.cm.api.auth;

import jakarta.servlet.http.Cookie;
import org.cm.api.auth.dto.LoginResponse;
import org.cm.jwt.JwtConstants;
import org.cm.test.config.BaseWebMvcUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends BaseWebMvcUnitTest {
    @MockitoBean
    AuthService authService;

    @Test
    @DisplayName("올바른 token으로 재발급 요청 시 재발급에 성공해야 함")
    void test_token_refresh() throws Exception {
        // given
        var validRefreshToken = "valid-refresh-token";
        Mockito.when(authService.refresh(Mockito.any()))
            .thenAnswer(invocation -> Optional
                .ofNullable(invocation.getArgument(0, String.class))
                .filter(token -> token.equals(validRefreshToken))
                .map(token -> new LoginResponse("access-token", token))
                .orElseThrow(() -> new IllegalArgumentException("Invalid token")));

        // when
        var req = post("/api/v1/auth/refresh")
            .cookie(new Cookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, validRefreshToken));
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value(validRefreshToken));

        // then
        Mockito.verify(authService, Mockito.times(1)).refresh(validRefreshToken);
    }

    @Test
    @DisplayName("올바르지 않은 token으로 재발급 요청 시 재발급에 실패해야 함")
    void should_throw_401_when_refresh_with_invalid_token() throws Exception {
        // given
        var validRefreshToken = "valid-refresh-token";
        var invalidRefreshToken = "invalid-refresh-token";
        Mockito.when(authService.refresh(Mockito.any()))
            .thenAnswer(invocation -> Optional
                .ofNullable(invocation.getArgument(0, String.class))
                .filter(token -> token.equals(validRefreshToken))
                .map(token -> new LoginResponse("access-token", token))
                .orElseThrow(() -> new IllegalArgumentException("Invalid token")));

        // when
        var req = post("/api/v1/auth/refresh")
            .cookie(new Cookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, invalidRefreshToken));
        mvc.perform(req)
            .andExpect(status().is5xxServerError());

        // then
        Mockito.verify(authService, Mockito.times(1)).refresh(invalidRefreshToken);
    }
}
