package org.cm.infra.mediaconvert.queue;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.cm.infra.mediaconvert.queue.WavVideoCombineQueue.VideoSource;
import org.cm.infra.property.MediaConvertProperty;
import org.cm.infra.property.S3URLProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.AacCodingMode;
import software.amazon.awssdk.services.mediaconvert.model.AacSettings;
import software.amazon.awssdk.services.mediaconvert.model.AccelerationMode;
import software.amazon.awssdk.services.mediaconvert.model.AccelerationSettings;
import software.amazon.awssdk.services.mediaconvert.model.AudioCodec;
import software.amazon.awssdk.services.mediaconvert.model.AudioCodecSettings;
import software.amazon.awssdk.services.mediaconvert.model.AudioDefaultSelection;
import software.amazon.awssdk.services.mediaconvert.model.AudioDescription;
import software.amazon.awssdk.services.mediaconvert.model.AudioSelector;
import software.amazon.awssdk.services.mediaconvert.model.BillingTagsSource;
import software.amazon.awssdk.services.mediaconvert.model.ContainerSettings;
import software.amazon.awssdk.services.mediaconvert.model.ContainerType;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobRequest;
import software.amazon.awssdk.services.mediaconvert.model.DestinationSettings;
import software.amazon.awssdk.services.mediaconvert.model.FileGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.H264RateControlMode;
import software.amazon.awssdk.services.mediaconvert.model.H264SceneChangeDetect;
import software.amazon.awssdk.services.mediaconvert.model.H264Settings;
import software.amazon.awssdk.services.mediaconvert.model.Input;
import software.amazon.awssdk.services.mediaconvert.model.InputTimecodeSource;
import software.amazon.awssdk.services.mediaconvert.model.JobSettings;
import software.amazon.awssdk.services.mediaconvert.model.MotionImageInserter;
import software.amazon.awssdk.services.mediaconvert.model.MotionImageInsertionMode;
import software.amazon.awssdk.services.mediaconvert.model.MotionImageInsertionOffset;
import software.amazon.awssdk.services.mediaconvert.model.MotionImagePlayback;
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


@Component
@RequiredArgsConstructor
public class VideoOverlayCombineQueue {

    private final MediaConvertClient mediaConvertClient;
    private final S3URLProperty s3UrlProperty;
    private final MediaConvertProperty mediaConvertProperty;

    public String offer(
            String fileId,
            VideoSource videoSource,
            VideoSource overlaySource
    ) {
        CreateJobRequest jobRequest = CreateJobRequest.builder()
                // TODO 적절한 큐로 바꿀 것
                .queue(mediaConvertProperty.queue().sceneCombine())
                .role(mediaConvertProperty.userArn())
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
                                                        s3UrlProperty.videoContentsLocator().combineLocation(fileId))
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
                                                .scalingBehavior(ScalingBehavior.FIT)
                                                .sharpness(100)
                                                .codecSettings(VideoCodecSettings.builder()
                                                        .codec(VideoCodec.H_264)
                                                        .h264Settings(H264Settings.builder()
                                                                .maxBitrate(6000000)
                                                                .rateControlMode(H264RateControlMode.QVBR)
                                                                .sceneChangeDetect(
                                                                        H264SceneChangeDetect.TRANSITION_DETECTION)
                                                                .build())
                                                        .build())
                                                .build())
                                        .audioDescriptions(AudioDescription.builder()
                                                .audioSourceName("Audio Selector 1")
                                                .codecSettings(AudioCodecSettings.builder()
                                                        .codec(AudioCodec.AAC)
                                                        .aacSettings(AacSettings.builder()
                                                                .bitrate(96000)
                                                                .codingMode(AacCodingMode.CODING_MODE_2_0)
                                                                .sampleRate(48000)
                                                                .build())
                                                        .build())
                                                .build())
                                        .extension("mp4")
                                        .build())
                                .build())
                        .inputs(Input.builder()
                                .audioSelectors(new HashMap<>() {{
                                    put("Audio Selector 1", AudioSelector.builder()
                                            .defaultSelection(AudioDefaultSelection.DEFAULT)
                                            .build());
                                }})
                                .timecodeSource(InputTimecodeSource.ZEROBASED)
                                .fileInput(videoSource.s3Location())
                                .build())
                        .motionImageInserter(MotionImageInserter.builder()
                                .insertionMode(MotionImageInsertionMode.MOV)
                                .input(overlaySource.s3Location())
                                .offset(MotionImageInsertionOffset.builder()
                                        .imageX(overlaySource.x())
                                        .imageY(overlaySource.y())
                                        .build())
                                .playback(MotionImagePlayback.REPEAT)
                                .build())
                        .followSource(1)
                        .build())
                .billingTagsSource(BillingTagsSource.JOB)
                .accelerationSettings(AccelerationSettings.builder()
                        .mode(AccelerationMode.DISABLED)
                        .build())
                .statusUpdateInterval(StatusUpdateInterval.SECONDS_10)
                .priority(0)
                .build();

        return mediaConvertClient.createJob(jobRequest)
                .job()
                .id();
    }
}
