package org.cm.domain.project;

import org.cm.domain.common.GenericJsonConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProjectData 직렬화 테스트")
@SuppressWarnings("SequencedCollectionMethodCanBeUsed")
class ProjectDataTest {
    GenericJsonConverter<ProjectData> converter = new ProjectData.Converter();

    @Test
    @DisplayName("올바른 입력이 주어질 경우 직렬화에 성공해야 함.")
    public void test00001() {
        var data = assertDoesNotThrow(() -> converter.convertToEntityAttribute(validJsonText001));

        assertNotNull(data);
        assertEquals(2, data.getScenes().size());

        assertEquals("scene1", data.getScenes().get(0).getId());
        assertEquals("안녕하세요. 반갑습니다.", data.getScenes().get(0).getTranscript().getText());
        assertEquals(1.0, data.getScenes().get(0).getTranscript().getSpeed());
        assertEquals(1.0, data.getScenes().get(0).getTranscript().getPitch());
        assertEquals(0.0, data.getScenes().get(0).getTranscript().getPostDelayMs());
    }

    // <editor-fold desc="String validJsonText001">
    String validJsonText001 = """
        {
            "scenes": [
                {
                    "id": "scene1",
                    "transcript": {
                        "text": "안녕하세요. 반갑습니다.",
                        "actor": "_default_",
                        "speed": 1.0,
                        "pitch": 1.0,
                        "postDelayMs": 0.0
                    },
                    "subtitle": {
                        "text": "안녕하세요. 반갑습니다.",
                        "fontFamily": "Arial",
                        "fontSize": "12pt",
                        "fontColor": "#000000",
                        "bgColor": "#FFFFFF",
                        "posX": 0,
                        "posY": 0
                    },
                    "screen": {
                        "type": "Image",
                        "url": "https://example.com/image.jpg",
                        "width": 1920,
                        "height": 1080,
                        "posX": 0,
                        "posY": 0,
                        "opacity": 100
                    }
                },
                {
                    "id": "scene2",
                    "transcript": {
                        "text": "안녕하세요. 반갑습니다.",
                        "actor": "_default_",
                        "speed": 1.0,
                        "pitch": 1.0,
                        "postDelayMs": 0.0
                    },
                    "subtitle": {
                        "text": "안녕하세요. 반갑습니다.",
                        "fontFamily": "Arial",
                        "fontSize": "12pt",
                        "fontColor": "#000000",
                        "bgColor": "#FFFFFF",
                        "posX": 0,
                        "posY": 0
                    },
                    "screen": {
                        "type": "Video",
                        "url": "https://example.com/image.jpg",
                        "width": 1920,
                        "height": 1080,
                        "posX": 0,
                        "posY": 0,
                        "opacity": 100
                    }
                }
            ]
        }""".strip();
    // </editor-fold>
}
