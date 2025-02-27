package org.cm.domain.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record MemberDetail(
        String name,
        String nickname,
        String email
) {
}
