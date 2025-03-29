package org.cm.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;

@Embeddable
public record MemberPrincipal(
    @Column(name = "login_id", nullable = false, updatable = false)
    String id,

    @Column(name = "login_credential")
    String credential,

    @Convert(converter = MemberPrincipalType.Converter.class)
    @Column(name = "login_type", nullable = false, updatable = false)
    MemberPrincipalType type
) {
    public static String createScopedPrincipalId(String scope, String id) {
        if (scope.contains(":")) {
            throw new IllegalArgumentException("Scope cannot contain ':'");
        }
        return String.format("{%s}:{%s}", scope, id);
    }
}
