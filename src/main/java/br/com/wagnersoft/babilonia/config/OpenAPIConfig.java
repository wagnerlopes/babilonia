package br.com.wagnersoft.babilonia.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/** 
 * Responsável pela configuração da documentação da OpenAPI.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Configuration
public class OpenAPIConfig {

  private static final String APP_NAME = "Projeto Babilônia";
  private static final String APP_VERSION = "v1.0.0-alpha";
  private static final String API_KEY_HEADER = "x-api-key";
  private static final String GROUP_NAME = "babilonia-public";
  private static final String GITHUB_URL = "https://github.com/wagnerlopes";

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group(GROUP_NAME)
        .pathsToMatch("/v1/**")
        .build();
  }

  @Bean
  public OpenAPI appOpenAPI() {

    final SecurityScheme securityScheme = new SecurityScheme()
        .description("API Key necessária para autenticação")
        .name(API_KEY_HEADER)
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER);

    final Components securityComponent = new Components().addSecuritySchemes(API_KEY_HEADER, securityScheme);

    final SecurityRequirement securityItem = new SecurityRequirement().addList(API_KEY_HEADER);

    final Contact contato = new Contact()
        .name(APP_NAME)
        .email("wagner.luis.alopes@gmail.com")
        .url(GITHUB_URL + "/babilonia?tab=readme-ov-file");

    final License licenca = new License()
        .name("MIT License")
        .url(GITHUB_URL + "/babilonia?tab=MIT-1-ov-file");

    final Info info = new Info()
        .title(APP_NAME)
        .version(APP_VERSION)
        .description("Rest Web Service for Brazilian InfoGeo")
        .contact(contato)
        .license(licenca);

    final ExternalDocumentation docs = new ExternalDocumentation()
        .description("Portal de Qualidade do Projeto Babilônia")
        .url("https://wagnerlopes.github.io/babilonia/index.html");

    return new OpenAPI()
        .info(info)
        .addSecurityItem(securityItem)
        .components(securityComponent)
        .externalDocs(docs);
  }

}
