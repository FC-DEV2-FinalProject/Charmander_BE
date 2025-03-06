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
}
