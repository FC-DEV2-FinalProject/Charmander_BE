package org.cm.api.account.dto;

public record VerifyEmailRequest(
    String email,
    String code
) {
}
