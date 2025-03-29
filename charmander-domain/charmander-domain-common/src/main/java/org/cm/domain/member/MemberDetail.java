package org.cm.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record MemberDetail(
    @Column(nullable = false)
    String name,

    @Column(nullable = false)
    String nickname,

    @Column(nullable = false)
    String email
) {
}
