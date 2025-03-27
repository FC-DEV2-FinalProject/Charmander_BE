package org.cm.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cm.domain.scene.Scene;

@Converter
public class SceneConverter implements AttributeConverter<Scene, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Scene scene) {
        try {
            return objectMapper.writeValueAsString(scene);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Scene convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, Scene.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
