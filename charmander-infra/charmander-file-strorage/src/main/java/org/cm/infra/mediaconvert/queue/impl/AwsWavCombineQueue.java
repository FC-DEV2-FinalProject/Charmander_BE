package org.cm.infra.mediaconvert.queue.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cm.infra.mediaconvert.queue.WavCombineQueue;
import org.cm.infra.property.MediaConvertProperty;
import org.cm.infra.property.S3URLProperty;
import org.cm.infra.utils.MetadataConverter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.AacCodingMode;
import software.amazon.awssdk.services.mediaconvert.model.AacRateControlMode;
import software.amazon.awssdk.services.mediaconvert.model.AacSettings;
import software.amazon.awssdk.services.mediaconvert.model.AacSpecification;
import software.amazon.awssdk.services.mediaconvert.model.AacVbrQuality;
import software.amazon.awssdk.services.mediaconvert.model.AccelerationMode;
import software.amazon.awssdk.services.mediaconvert.model.AccelerationSettings;
import software.amazon.awssdk.services.mediaconvert.model.AudioCodec;
import software.amazon.awssdk.services.mediaconvert.model.AudioCodecSettings;
import software.amazon.awssdk.services.mediaconvert.model.AudioDescription;
import software.amazon.awssdk.services.mediaconvert.model.BillingTagsSource;
import software.amazon.awssdk.services.mediaconvert.model.ContainerSettings;
import software.amazon.awssdk.services.mediaconvert.model.ContainerType;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobRequest;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobResponse;
import software.amazon.awssdk.services.mediaconvert.model.DestinationSettings;
import software.amazon.awssdk.services.mediaconvert.model.FileGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.JobSettings;
import software.amazon.awssdk.services.mediaconvert.model.Mp4Settings;
import software.amazon.awssdk.services.mediaconvert.model.Output;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroup;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroupType;
import software.amazon.awssdk.services.mediaconvert.model.S3DestinationSettings;
import software.amazon.awssdk.services.mediaconvert.model.S3StorageClass;
import software.amazon.awssdk.services.mediaconvert.model.StatusUpdateInterval;
import software.amazon.awssdk.services.mediaconvert.model.TimecodeConfig;
import software.amazon.awssdk.services.mediaconvert.model.TimecodeSource;

@Component
@RequiredArgsConstructor
public class AwsWavCombineQueue implements WavCombineQueue {

    private final MediaConvertClient mediaConvertClient;
    private final S3URLProperty s3UrlProperty;
    private final MediaConvertProperty mediaConvertProperty;

    public <T> String offer(
            String fileId,
            List<WavCombineQueue.AudioSource> audioSources,
            T input
    ) {
        CreateJobRequest jobRequest = CreateJobRequest.builder()
                .queue(mediaConvertProperty.queue().voiceCombine())
                .role(mediaConvertProperty.userArn())
                .userMetadata(MetadataConverter.convert(input))
                .settings(JobSettings.builder()
                        .timecodeConfig(TimecodeConfig.builder()
                                .source(TimecodeSource.ZEROBASED)
                                .build())
                        .outputGroups(OutputGroup.builder()
                                .name("File Group")
                                .outputGroupSettings(OutputGroupSettings.builder()
                                        .type(OutputGroupType.FILE_GROUP_SETTINGS)
                                        .fileGroupSettings(FileGroupSettings.builder()
                                                .destination(s3UrlProperty.ttsContentsLocator().combineLocation(fileId))
                                                .destinationSettings(DestinationSettings.builder()
                                                        .s3Settings(S3DestinationSettings.builder()
                                                                .storageClass(S3StorageClass.STANDARD)
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .outputs(Output.builder()
                                        .containerSettings(ContainerSettings.builder()
                                                .container(ContainerType.MP4)
                                                .mp4Settings(Mp4Settings.builder().build())
                                                .build())
                                        .audioDescriptions(AudioDescription.builder()
                                                .audioSourceName("Audio Selector 1")
                                                .codecSettings(AudioCodecSettings.builder()
                                                        .codec(AudioCodec.AAC)
                                                        .aacSettings(AacSettings.builder()
                                                                .vbrQuality(AacVbrQuality.HIGH)
                                                                .rateControlMode(AacRateControlMode.VBR)
                                                                .codingMode(AacCodingMode.CODING_MODE_2_0)
                                                                .sampleRate(48000)
                                                                .specification(AacSpecification.MPEG4)
                                                                .build())
                                                        .build())
                                                .build())
                                        .extension("wav")
                                        .nameModifier("output")
                                        .build())
                                .build())
                        .inputs(audioSources.stream().map(WavCombineQueue.AudioSource::toInput).flatMap(List::stream)
                                .toList())
                        .build())
                .accelerationSettings(AccelerationSettings.builder()
                        .mode(AccelerationMode.DISABLED)
                        .build())
                .statusUpdateInterval(StatusUpdateInterval.SECONDS_60)
                .priority(0)
                .billingTagsSource(BillingTagsSource.JOB)
                .build();

        CreateJobResponse response = mediaConvertClient.createJob(jobRequest);

        return response.job().id();
    }
}
