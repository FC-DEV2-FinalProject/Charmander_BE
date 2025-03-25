package org.cm.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Converter
public class DurationToSecondConverter implements AttributeConverter<Duration, Integer> {
    @Override
    public Duration convertToEntityAttribute(Integer dbData) {
        return nullOrValue(dbData, d -> Duration.of(d, ChronoUnit.SECONDS));
    }

    @Override
    public Integer convertToDatabaseColumn(Duration duration) {
        return nullOrValue(duration, d -> Math.toIntExact(d.toSeconds()));
    }

    static <INPUT, OUTPUT> OUTPUT nullOrValue(INPUT input, Function<INPUT, OUTPUT> function){
        return input == null ? null : function.apply(input);
    }
}
