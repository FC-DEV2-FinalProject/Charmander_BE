package org.cm.domain.project;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 영상 장면의 대사
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSceneTranscript {
    private String text;

    private String actor = "_default_";

    private double speed = 1.0;
    private double pitch = 1.0;

    private double postDelayMs = 0.0;
}
