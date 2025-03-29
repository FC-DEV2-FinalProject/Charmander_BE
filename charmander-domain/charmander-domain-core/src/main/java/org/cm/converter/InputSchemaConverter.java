package org.cm.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cm.domain.task.TaskInputSchema;

@Converter
public class InputSchemaConverter implements AttributeConverter<TaskInputSchema, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TaskInputSchema taskInputSchema) {
        try {
            return objectMapper.writeValueAsString(taskInputSchema);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TaskInputSchema convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, TaskInputSchema.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
