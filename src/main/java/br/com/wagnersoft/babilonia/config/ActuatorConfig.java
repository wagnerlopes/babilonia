package br.com.wagnersoft.babilonia.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

/** 
 * Responsável por configurar os endpoints de monitoramento do Actuator.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Configuration
public class ActuatorConfig {

  @Bean
  public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
      WebEndpointsSupplier webEndpointsSupplier,
      EndpointMediaTypes endpointMediaTypes,
      CorsEndpointProperties corsProperties,
      WebEndpointProperties webEndpointProperties,
      Environment environment) {

    String basePath = webEndpointProperties.getBasePath();
    
    List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
    Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
    allEndpoints.addAll(webEndpoints);

    EndpointMapping endpointMapping = new EndpointMapping(basePath);

    EndpointLinksResolver endpointLinksResolver = new EndpointLinksResolver(allEndpoints, basePath);
    
    boolean shouldRegisterLinksMapping = shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);

    return new WebMvcEndpointHandlerMapping(
        endpointMapping,
        webEndpoints,
        endpointMediaTypes,
        corsProperties.toCorsConfiguration(),
        endpointLinksResolver,
        shouldRegisterLinksMapping);
  }

  private boolean shouldRegisterLinksMapping(WebEndpointProperties properties, Environment env, String basePath) {
    return properties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(env) == ManagementPortType.DIFFERENT);
  }

}
