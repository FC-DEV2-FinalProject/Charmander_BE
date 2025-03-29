package org.cm.kafka;

public record CompletionNotificationRecord(
        Long taskId,
        String fileId
) {
}
