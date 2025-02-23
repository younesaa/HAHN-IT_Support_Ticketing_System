package com.hahn.supportticketsystem.config.swagger;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
public class SwaggerConfig {

    @Value("${swagger.url}")
    private String swaggerUrl;

   @Bean
   public OpenAPI defineOpenApi() {
       
       final String securitySchemeName = "bearerAuth";
       
       Server server = new Server();
       server.setUrl(swaggerUrl);
       server.setDescription("Development");

       Contact myContact = new Contact();
       myContact.setName("younes.lakhnichy");
       myContact.setEmail("younes.lakhnichy@gmail.com");

       Info information = new Info()
               .title("IT ticket Management System API")
               .version("1.0")
               .description("This API exposes endpoints to IT ticket System.")
               .contact(myContact);
       return new OpenAPI().info(information).servers(List.of(server))
                .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
                .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
   }

}
