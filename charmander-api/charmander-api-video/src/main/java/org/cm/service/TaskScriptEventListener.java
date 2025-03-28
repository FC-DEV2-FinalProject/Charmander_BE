package org.cm.service;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.task.SceneOutput;
import org.cm.domain.task.SceneOutputRepository;
import org.cm.domain.task.Task;
import org.cm.domain.task.TaskInputSchema.Scene;
import org.cm.domain.task.TaskInputSchema.Transcript;
import org.cm.domain.taskscript.TaskScript;
import org.cm.domain.taskscript.TaskScriptRepository;
import org.cm.infra.mediaconvert.queue.WavCombineQueue;
import org.cm.infra.mediaconvert.queue.WavCombineQueue.AudioSource;
import org.cm.infra.utils.MetadataConverter;
import org.cm.kafka.UserMetadata;
import org.cm.vo.TaskScriptGenerationCompletedEvent;
import org.cm.vo.TaskScriptGenerationStartedEvent;
import org.cm.vo.TtsCombineEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskScriptEventListener {

    private final TaskScriptService taskScriptService;
    private final TaskScriptRepository taskScriptRepository;
    private final SceneOutputRepository sceneOutputRepository;
    private final WavCombineQueue wavCombineQueue;


    @EventListener
    public void listen(TaskScriptGenerationStartedEvent event) {
        taskScriptService.start(event.taskScriptId());
    }

    @EventListener
    public void listen(TaskScriptGenerationCompletedEvent event) {
        taskScriptService.complete(event.taskScriptId(), event.fileId());
    }

    @EventListener
    @Transactional
    public void listen(TtsCombineEvent event) {
        // TODO 음성이 하나 인 경우 바로 장면 합성으로 가도록 구현
        var taskScript = taskScriptRepository.getById(event.taskScriptId());
        var count = taskScriptRepository.countNotSuccessTaskScriptsByTaskId(taskScript.getSceneId());
        if (count != 0) {
            return;
        }

        var taskScripts = taskScriptRepository.findAllSuccessTaskScriptsBySceneId(taskScript.getSceneId());
        var task = taskScript.getTask();
        var scene = task.getInputSchema().findSceneById(taskScript.getSceneId());
        var sceneOutput = findSceneOutput(task, taskScript, scene);
        var transcripts = scene.transcript();
        var audioSources = getAudioSources(taskScripts, transcripts);
        var combineAudioId = RandomKeyGenerator.generateRandomKey();
        var metadata = UserMetadata.ttsCombine(task.getId(), taskScript.getId(), scene.id());

        log.info("{}", audioSources);
        wavCombineQueue.offer(combineAudioId, audioSources, MetadataConverter.convert(metadata));
        sceneOutput.update(combineAudioId);

    }

    private SceneOutput findSceneOutput(Task task, TaskScript taskScript, Scene scene) {
        return sceneOutputRepository.findByTaskIdAndSceneId(task.getId(), taskScript.getSceneId())
                .orElseGet(() -> sceneOutputRepository.save(
                        new SceneOutput(task.getId(), taskScript.getSceneId(), scene)));
    }

    private List<AudioSource> getAudioSources(
            List<TaskScript> taskScripts,
            List<Transcript> transcripts
    ) {
        return IntStream.range(0, taskScripts.size())
                .mapToObj(i -> new AudioSource(taskScripts.get(i).getFileId(), transcripts.get(i).postDelay()))
                .toList();
    }


}
