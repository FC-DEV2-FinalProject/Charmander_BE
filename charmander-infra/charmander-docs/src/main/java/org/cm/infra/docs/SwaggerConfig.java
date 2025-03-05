package org.cm.infra.docs;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@Configuration
public class SwaggerConfig {

    @Bean
    @ConditionalOnMissingBean
    public DocumentMetadata defaultDocumentSpec(){
        return new DocumentMetadata(
                "스웨거 설정",
                "문서 메타데이터를 정의해주세요",
                "v0.1.0",
                false
        );
    }

    @Bean
    public OpenAPI customOpenAPI(@Autowired DocumentMetadata documentMetadata) {
        var openAPI = new OpenAPI();

        openAPI.setInfo(documentMetadata.toInfo());

        if(documentMetadata.enableJWT()) {
            openAPI.setComponents(bearerAuthenticationComponents());
            openAPI.addSecurityItem(new SecurityRequirement().addList("JWT"));
        }

        return openAPI;
    }

    private static Components bearerAuthenticationComponents() {
        var component = new Components();
        var schema = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        component.addSecuritySchemes("JWT", schema);

        return component;
    }
}
