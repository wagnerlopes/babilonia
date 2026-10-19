package br.com.wagnersoft.babilonia.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 
 * Adiciona as propriedades de segurança do Cors.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  private static final Logger LOGGER = LoggerFactory.getLogger(CorsConfig.class);

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
    .allowedOrigins("http://localhost:8080")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowedHeaders("*")
    .allowCredentials(true);

    LOGGER.info("Cors liberado para: {}", "http://localhost:8080");
  }

}
