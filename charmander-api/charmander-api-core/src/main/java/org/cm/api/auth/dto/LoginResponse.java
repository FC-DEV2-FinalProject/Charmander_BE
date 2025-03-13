package org.cm.api.auth.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {
}
