package org.cm.infra.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MetadataConverter {

    public <T> Map<String, String> convert(T input) {
        ObjectMapper objectMapper = new ObjectMapper();
        // 객체를 Map<String, String>으로 변환
        return objectMapper.convertValue(input, new TypeReference<>() {
        });
    }

}
