package org.cm.api.task.mapper;

import lombok.RequiredArgsConstructor;
import org.cm.api.task.dto.TaskResponse;
import org.cm.domain.task.Task;
import org.cm.infra.utils.S3Utils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TaskResponseMapper {
    private final S3Utils s3Utils;

    public TaskResponse map(final Task task) {
        return new TaskResponse(
            task.getId(),
            task.getProject().getId(),
            task.getJobId(),
            task.getType(),
            task.getStatus(),
            mapTaskOutput(task),
            task.getRetryCount(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }

    private TaskResponse.TaskOutputDTO mapTaskOutput(final Task task) {
        var e = task.getOutput();
        if (e == null) {
            return null;
        }
        return new TaskResponse.TaskOutputDTO(
            resolveFileUrl(e.fileName()),
            e.fileName(),
            e.playtime(),
            e.getDownloadCount()
        );
    }

    private String resolveFileUrl(final String fileUrl) {
        // TODO: 추상화 하기
        var regex = "^s3://([^/]+)/(.+)$";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(fileUrl);
        if (matcher.find()) {
            String bucketName = matcher.group(1);
            String filePath = matcher.group(2);
            var region = s3Utils.getBucketRegion(bucketName);
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, filePath);
        }
        return null;
    }
}
