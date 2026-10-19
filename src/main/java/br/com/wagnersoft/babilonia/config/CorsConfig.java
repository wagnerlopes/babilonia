package br.com.wagnersoft.babilonia.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Data;

/** 
 * Adiciona as propriedades de segurança do Cors.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.cors")
public class CorsConfig implements WebMvcConfigurer {

  private static final Logger LOGGER = LoggerFactory.getLogger(CorsConfig.class);

  private List<String> allowedOrigins;
  
  private List<String> allowedMethods;
  
  private List<String> allowedHeaders;
  
  private boolean allowCredentials;
  
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
    .allowedOrigins(allowedOrigins.toArray(String[]::new))
    .allowedMethods(allowedMethods.toArray(String[]::new))
    .allowedHeaders(allowedHeaders.toArray(String[]::new))
    .allowCredentials(allowCredentials);

    LOGGER.info("Cors liberado para: {}", allowedOrigins);
  }

}
