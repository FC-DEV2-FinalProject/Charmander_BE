package org.cm.domain.project;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 영상 장면의 자막
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSceneSubtitle {
    private String text;

    private String fontFamily;
    private String fontSize;
    private String fontColor;

    private String bgColor;

    private int posX = 0;
    private int posY = 0;
}
