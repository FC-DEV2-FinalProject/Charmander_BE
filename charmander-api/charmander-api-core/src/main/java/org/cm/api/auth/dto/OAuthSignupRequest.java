package org.cm.api.auth.dto;

import org.jspecify.annotations.NonNull;

public record OAuthSignupRequest(
    @NonNull
    String provider,
    @NonNull
    String code,
    String state
) {
}
