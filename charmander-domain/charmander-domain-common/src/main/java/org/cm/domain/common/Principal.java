package org.cm.domain.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record Principal(
        String loginId,
        String loginCredential,
        RegistrationType registrationType
) {
}
