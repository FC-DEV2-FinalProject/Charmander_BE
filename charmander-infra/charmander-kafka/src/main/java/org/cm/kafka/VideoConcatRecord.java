package org.cm.kafka;

public record VideoConcatRecord(
        Long taskId,
        String fileId
) {
}
