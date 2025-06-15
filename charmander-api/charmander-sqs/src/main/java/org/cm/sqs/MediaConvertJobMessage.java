package org.cm.sqs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.cm.kafka.UserMetadata;

public record MediaConvertJobMessage(
        String version,
        String id,
        @JsonProperty("detail-type") String detailType,
        String source,
        String account,
        String time,
        String region,
        List<String> resources,
        Detail detail
) {

    public record Detail(
            long timestamp,
            String accountId,
            String queue,
            String jobId,
            String status,
            UserMetadata userMetadata,
            List<OutputGroupDetail> outputGroupDetails
    ) {
    }


    public record OutputGroupDetail(
            List<OutputDetail> outputDetails,
            String type
    ) {
    }

    public record OutputDetail(
            List<String> outputFilePaths,
            long durationInMs
    ) {
    }

}

