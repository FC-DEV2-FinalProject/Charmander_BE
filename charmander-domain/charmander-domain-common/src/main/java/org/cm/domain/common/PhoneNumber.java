package org.cm.domain.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record PhoneNumber(
        String countryCode,
        String number
) {
}
