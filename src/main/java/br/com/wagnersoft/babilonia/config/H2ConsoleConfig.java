package br.com.wagnersoft.babilonia.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Servlet;

@Configuration
public class H2ConsoleConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(H2ConsoleConfig.class);

  @Bean
  public ServletRegistrationBean<Servlet> h2ServletRegistration() {
    Servlet servlet = new org.h2.server.web.JakartaWebServlet();
    LOGGER.info("Registrando H2 console em: /h2-console/*");
    return new ServletRegistrationBean<>(servlet, "/h2-console/*");
  }

}
