package org.cm.infra.mediaconvert.queue;

import lombok.RequiredArgsConstructor;
import org.cm.infra.mediaconvert.queue.WavCombineQueue.AudioSource;
import org.cm.infra.property.MediaConvertProperty;
import org.cm.infra.property.S3URLProperty;
import org.cm.infra.utils.MetadataConverter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.AacCodingMode;
import software.amazon.awssdk.services.mediaconvert.model.AacRateControlMode;
import software.amazon.awssdk.services.mediaconvert.model.AacSettings;
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
import software.amazon.awssdk.services.mediaconvert.model.DestinationSettings;
import software.amazon.awssdk.services.mediaconvert.model.FileGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.H264FramerateControl;
import software.amazon.awssdk.services.mediaconvert.model.H264FramerateConversionAlgorithm;
import software.amazon.awssdk.services.mediaconvert.model.H264RateControlMode;
import software.amazon.awssdk.services.mediaconvert.model.H264Settings;
import software.amazon.awssdk.services.mediaconvert.model.ImageInserter;
import software.amazon.awssdk.services.mediaconvert.model.InsertableImage;
import software.amazon.awssdk.services.mediaconvert.model.JobSettings;
import software.amazon.awssdk.services.mediaconvert.model.Mp4Settings;
import software.amazon.awssdk.services.mediaconvert.model.Output;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroup;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroupType;
import software.amazon.awssdk.services.mediaconvert.model.S3DestinationSettings;
import software.amazon.awssdk.services.mediaconvert.model.S3StorageClass;
import software.amazon.awssdk.services.mediaconvert.model.ScalingBehavior;
import software.amazon.awssdk.services.mediaconvert.model.StatusUpdateInterval;
import software.amazon.awssdk.services.mediaconvert.model.TimecodeConfig;
import software.amazon.awssdk.services.mediaconvert.model.TimecodeSource;
import software.amazon.awssdk.services.mediaconvert.model.VideoCodec;
import software.amazon.awssdk.services.mediaconvert.model.VideoCodecSettings;
import software.amazon.awssdk.services.mediaconvert.model.VideoDescription;
import software.amazon.awssdk.services.mediaconvert.model.VideoPreprocessor;


@Component
@RequiredArgsConstructor
public class WavImageCombineQueue {

    private final MediaConvertClient mediaConvertClient;
    private final MediaConvertProperty property;
    private final S3URLProperty s3URLProperty;

    public <T> String offer(
            AudioSource audioSource,
            ImageSource imageSource,
            String fileId,
            T metadata
    ) {

        CreateJobRequest jobRequest = CreateJobRequest.builder()
                .queue(property.queue().sceneCombine())
                .userMetadata(MetadataConverter.convert(metadata))
                .role(property.userArn())
                .userMetadata(MetadataConverter.convert(metadata))
                .settings(JobSettings.builder()
                        .timecodeConfig(TimecodeConfig.builder()
                                .source(TimecodeSource.ZEROBASED)
                                .build())
                        .outputGroups(OutputGroup.builder()
                                .name("File Group")
                                .outputGroupSettings(OutputGroupSettings.builder()
                                        .type(OutputGroupType.FILE_GROUP_SETTINGS)
                                        .fileGroupSettings(FileGroupSettings.builder()
                                                .destination(
                                                        s3URLProperty.videoContentsLocator().combineLocation(fileId))
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
                                        .videoDescription(VideoDescription.builder()
                                                .width(1280)
                                                .height(720)
                                                .scalingBehavior(ScalingBehavior.FILL)
                                                .videoPreprocessors(VideoPreprocessor.builder()
                                                        .imageInserter(ImageInserter.builder()
                                                                .insertableImages(
                                                                        InsertableImage.builder()
                                                                                .width(imageSource.width())
                                                                                .height(imageSource.height())
                                                                                .imageX(imageSource.x())
                                                                                .imageY(imageSource.y())
                                                                                .layer(1)
                                                                                .imageInserterInput(
                                                                                        imageSource.s3Location()
                                                                                )
                                                                                .startTime("00:00:00:00")
                                                                                .opacity(100)
                                                                                .build()
                                                                )
                                                                .build())
                                                        .build())
                                                .codecSettings(VideoCodecSettings.builder()
                                                        .codec(VideoCodec.H_264)
                                                        .h264Settings(H264Settings.builder()
                                                                .framerateDenominator(1001)
                                                                .bitrate(3000000)
                                                                .framerateControl(H264FramerateControl.SPECIFIED)
                                                                .rateControlMode(H264RateControlMode.CBR)
                                                                .framerateNumerator(30000)
                                                                .framerateConversionAlgorithm(
                                                                        H264FramerateConversionAlgorithm.DUPLICATE_DROP)
                                                                .build())
                                                        .build())
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
                                                                .build())
                                                        .build())
                                                .build())
                                        .extension("mp4")
                                        .nameModifier("output")
                                        .build())
                                .build())
                        .inputs(audioSource.toInput().getFirst())
                        .build())
                .accelerationSettings(AccelerationSettings.builder()
                        .mode(AccelerationMode.DISABLED)
                        .build())
                .statusUpdateInterval(StatusUpdateInterval.SECONDS_60)
                .priority(0)
                .billingTagsSource(BillingTagsSource.JOB)
                .build();

        return mediaConvertClient
                .createJob(jobRequest)
                .job()
                .id();
    }

    public record ImageSource(
            String s3Location,
            int width,
            int height,
            int x,
            int y
    ) {
    }
}
