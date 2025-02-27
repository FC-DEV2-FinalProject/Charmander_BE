package org.cm.domain.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record PersonalInfo(
        String name,
        String nickname,
        String email
) {
}
