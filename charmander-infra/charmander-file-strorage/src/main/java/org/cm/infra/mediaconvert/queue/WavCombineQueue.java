package org.cm.infra.mediaconvert.queue;

import java.util.List;
import java.util.Map;
import org.cm.infra.storage.ContentsLocator;
import software.amazon.awssdk.services.mediaconvert.model.AudioDefaultSelection;
import software.amazon.awssdk.services.mediaconvert.model.AudioSelector;
import software.amazon.awssdk.services.mediaconvert.model.Input;
import software.amazon.awssdk.services.mediaconvert.model.InputTimecodeSource;
import software.amazon.awssdk.services.mediaconvert.model.InputVideoGenerator;
import software.amazon.awssdk.services.mediaconvert.model.VideoSelector;

@FunctionalInterface
public interface WavCombineQueue {

    <T> String offer(String fileId, List<AudioSource> audioSources, T metadata);

    record AudioSource(String s3AudioLocation, int duration) {

        public List<Input> toInput() {
            var audioInput = Input.builder()
                    .audioSelectors(Map.of("Audio Selector 1", AudioSelector.builder()
                            .defaultSelection(AudioDefaultSelection.DEFAULT)
                            .externalAudioFileInput(s3AudioLocation)
                            .build()))
                    .videoSelector(VideoSelector.builder().build())
                    .timecodeSource(InputTimecodeSource.ZEROBASED)
                    .build();
            if (duration >= 50) {
                var delayInput = Input.builder()
                        .audioSelectors(Map.of("Audio Selector 1", AudioSelector.builder()
                                .defaultSelection(AudioDefaultSelection.DEFAULT)
                                .build()))
                        .videoSelector(VideoSelector.builder().build())
                        .videoGenerator(InputVideoGenerator.builder().duration(duration).build())
                        .build();
                return List.of(audioInput, delayInput);
            }
            return List.of(audioInput);
        }

        public List<Input> toInput(ContentsLocator contentsLocator) {
            var audioInput = Input.builder()
                    .audioSelectors(Map.of("Audio Selector 1", AudioSelector.builder()
                            .defaultSelection(AudioDefaultSelection.DEFAULT)
                            .externalAudioFileInput(contentsLocator.combineLocation(s3AudioLocation))
                            .build()))
                    .videoSelector(VideoSelector.builder().build())
                    .timecodeSource(InputTimecodeSource.ZEROBASED)
                    .build();
            if (duration >= 50) {
                var delayInput = Input.builder()
                        .audioSelectors(Map.of("Audio Selector 1", AudioSelector.builder()
                                .defaultSelection(AudioDefaultSelection.DEFAULT)
                                .build()))
                        .videoSelector(VideoSelector.builder().build())
                        .videoGenerator(InputVideoGenerator.builder().duration(duration).build())
                        .build();
                return List.of(audioInput, delayInput);
            }
            return List.of(audioInput);
        }
    }

}
