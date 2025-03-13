package org.cm.domain.project;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 영상 장면의 이미지 또는 비디오
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSceneScreen {
    private Type type;
    private String url;

    private int width = 0;
    private int height = 0;

    private int posX = 0;
    private int posY = 0;

    private int opacity = 100;

    public enum Type {
        Image,
        Video,
        ;
    }
}
