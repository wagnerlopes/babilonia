package br.com.wagnersoft.babilonia.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Data;

/** 
 * Configura comportamento do Spring MVC.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Data
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

  public WebMvcConfig () {
	  LOGGER.info("{} iniciado", WebMvcConfig.class.getName());
  }

}
