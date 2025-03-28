package org.cm.kafka;

public record UserMetadata(
        Type type,
        Long taskId,
        Long taskScriptId,
        Long sceneId
) {

    public static UserMetadata ttsCombine(Long taskId, Long taskScriptId, Long sceneId) {
        return new UserMetadata(Type.TTS_COMBINE, taskId, taskScriptId, sceneId);
    }

    public static UserMetadata sceneCombine(Long taskId, Long taskScriptId, Long sceneId) {
        return new UserMetadata(Type.SCENE_COMBINE, taskId, taskScriptId, sceneId);
    }

    public static UserMetadata videoOverlay(Long taskId, Long taskScriptId, Long sceneId) {
        return new UserMetadata(Type.VIDEO_OVERLAY, taskId, taskScriptId, sceneId);
    }

    public static UserMetadata concatVideo(Long taskId, Long sceneId) {
        return new UserMetadata(Type.CONCAT_VIDEO, taskId, null, sceneId);
    }

    public enum Type {
        TTS_COMBINE,
        VIDEO_OVERLAY,
        SCENE_COMBINE,
        CONCAT_VIDEO
    }

}
