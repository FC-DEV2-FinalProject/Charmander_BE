package org.cm.converter;

import jakarta.persistence.AttributeConverter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationToSecondConverter implements AttributeConverter<Duration, Integer> {
    @Override
    public Duration convertToEntityAttribute(Integer dbData) {
        return Duration.of(dbData, ChronoUnit.SECONDS);
    }

    @Override
    public Integer convertToDatabaseColumn(Duration duration) {
        return Math.toIntExact(duration.toSeconds());
    }
}
