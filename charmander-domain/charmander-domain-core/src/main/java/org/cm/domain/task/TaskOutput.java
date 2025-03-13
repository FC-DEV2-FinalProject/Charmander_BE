package org.cm.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.time.Duration;
import org.cm.converter.DurationToSecondConverter;

@Embeddable
public record TaskOutput(
    @Column
    String fileUrl,

    @Column
    String fileName,

    @Convert(converter = DurationToSecondConverter.class)
    @Column
    Duration playtime,

    @Column
    Integer downloadCount
) {

}
