package org.cm.api.auth.dto;

public record LoginRequest(
    String username,
    String password
) {
}
