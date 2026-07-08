package br.com.wagnersoft.babilonia.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.webmvc.actuate.endpoint.web.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/** SwaggerConfig.
 * @author Abreu Lopes
 * @since 0.1
 * @version 0.1
 */
@Configuration
public class SwaggerConfig {

  @Bean
  public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
          WebEndpointsSupplier webEndpointsSupplier,
          EndpointMediaTypes endpointMediaTypes,
          CorsEndpointProperties corsProperties,
          WebEndpointProperties webEndpointProperties,
          Environment environment) {

    List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
    Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
    allEndpoints.addAll(webEndpoints);

    String basePath = webEndpointProperties.getBasePath();
    EndpointMapping endpointMapping = new EndpointMapping(basePath);

    boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);

    return new WebMvcEndpointHandlerMapping(
        endpointMapping,
        webEndpoints,
        endpointMediaTypes,
        corsProperties.toCorsConfiguration(),
        new EndpointLinksResolver(allEndpoints, basePath),
        shouldRegisterLinksMapping
        );
  }
  
  private boolean shouldRegisterLinksMapping(final WebEndpointProperties webEndpointProperties, final Environment environment, String basePath) {
    return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
  }
  
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("babilonia-public")
        .pathsToMatch("/v1/**", "/v2/**")
        .build();
  }

  @Bean
  public OpenAPI appOpenAPI() {
    final SecurityScheme securityScheme = new SecurityScheme()
        .name("x-api-key")
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER);

    final Components securityComponent = new Components().addSecuritySchemes("x-api-key", securityScheme);

    final SecurityRequirement securityItem = new SecurityRequirement().addList("x-api-key");

    final Contact contato = new Contact()
        .email("wagner.luis.alopes@gmail.com")
        .name("BABILONIA")
        .url("https://github.com/wagnerlopes");

    return new OpenAPI()
        .info(new Info().title("BABILONIA")
            .description("Open API Web Service")
            .version("v1.0.0-alpha")
            .contact(contato)
            .license(new License().name("MIT License").url("https://github.com/wagnerlopes/babilonia?tab=MIT-1-ov-file")))
        .externalDocs(new ExternalDocumentation().description("Documentação Wiki").url("https://github.com/wagnerlopes/babilonia?tab=readme-ov-file"))
        .components(securityComponent)
        .addSecurityItem(securityItem);
  }

}
