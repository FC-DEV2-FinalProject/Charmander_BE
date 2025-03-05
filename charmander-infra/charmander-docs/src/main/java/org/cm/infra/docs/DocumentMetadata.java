package org.cm.infra.docs;

import io.swagger.v3.oas.models.info.Info;
import lombok.Builder;

@Builder
public record DocumentMetadata(
        String title,
        String description,
        String version,
        boolean enableJWT

){
    public Info toInfo(){
        return new Info().title(title)
                .description(description)
                .version(version);
    }
}
