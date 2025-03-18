package org.cm.vo;

import org.cm.infra.storage.PreSignedURLIdentifier;

public record TtsCreateCommand(
        Long taskId,
        PreSignedURLIdentifier identifier,
        Object data
){
}
