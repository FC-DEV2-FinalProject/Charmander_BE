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
        var taskScript = taskScriptRepository.getById(event.taskScriptId());
        // 씬의 스크립트가 작업이 완료되지 않은 경우 넘어감
        if (!taskScriptRepository.areAllSubTasksDone(taskScript.getSceneId())) {
            return;
        }
        // TODO 음성이 하나 인 경우 바로 장면 합성으로 가도록 구현 할 필요 있나..? 딜레이 부여해야 하니 그냥 넘어가야할 듯
        var taskScripts = taskScriptRepository.findAllSuccessTaskScriptsBySceneId(taskScript.getSceneId());
        var task = taskScript.getTask();
        var scene = task.getInputSchema().findSceneById(taskScript.getSceneId());

        createNewSceneOutput(task, taskScript, scene);

        wavCombineQueue.offer(
                RandomKeyGenerator.generateRandomKey(),
                getAudioSources(taskScripts, scene.transcript()),
                UserMetadata.ttsCombine(task.getId(), taskScript.getId(), scene.id())
        );
    }

    private void createNewSceneOutput(Task task, TaskScript taskScript, Scene scene) {
        var sceneOutput = new SceneOutput(task.getId(), taskScript.getSceneId(), scene);
        sceneOutputRepository.findByTaskIdAndSceneId(task.getId(), taskScript.getSceneId())
                .ifPresentOrElse(_ -> {
                    // no action
                }, () -> sceneOutputRepository.save(sceneOutput));
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
