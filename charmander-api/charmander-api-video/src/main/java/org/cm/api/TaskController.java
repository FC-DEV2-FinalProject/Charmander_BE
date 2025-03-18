package org.cm.api;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.cm.infra.storage.ContentsLocator;
import org.cm.infra.storage.PreSignedFileUploadService;
import org.cm.infra.storage.PreSignedURLAbortCommand;
import org.cm.infra.storage.PreSignedURLCompleteCommand;
import org.cm.infra.storage.PreSignedURLGenerateCommand;
import org.cm.infra.storage.PreSignedURLIdentifier;
import org.cm.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final PreSignedFileUploadService preSignedFileUploadService;
    private final ContentsLocator contentsLocator;

    @PostMapping("/multipart-upload")
    public PreSignedURLIdentifier initiateMultipartUpload() {
        return preSignedFileUploadService.sign(contentsLocator);
    }

    @GetMapping("/pre-signed-url/{fileName}/{uploadId}/{partNumber}")
    public String generatePreSignedUrl(
            @PathVariable String uploadId,
            @PathVariable int partNumber,
            @PathVariable String fileName
    ) {
        return preSignedFileUploadService.generateURL(
                contentsLocator,
                new PreSignedURLGenerateCommand(
                        fileName,
                        uploadId,
                        partNumber
                ),
                Duration.ofMinutes(30)
        );
    }

    @PostMapping("/complete-upload")
    public void completeMultipartUpload(
            @RequestBody PreSignedURLCompleteCommand command
    ) {
        preSignedFileUploadService.complete(contentsLocator, command);
    }

    @PostMapping("/{taskId}/complete")
    public void complete(@PathVariable Long taskId, @RequestBody TaskCompleteRequest request) {
        taskService.complete(
                taskId,
                request.toTaskOutput(),
                request.preSignedURLCompleteCommand()
        );
    }

    @PostMapping("/{taskId}/abort")
    public void abort(@PathVariable Long taskId, @RequestBody PreSignedURLAbortCommand command) {
        taskService.abort(taskId, command);
    }
}
