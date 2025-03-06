package org.cm.email;

import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailDto {
    String from;
    String to;
    String subject;
    String template;
    Map<String, Object> variables;
}
