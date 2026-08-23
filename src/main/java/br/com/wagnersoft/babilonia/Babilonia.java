package br.com.wagnersoft.babilonia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 
 * Spring Boot {@link ApplicationRunner} implementation.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@SpringBootApplication
public class Babilonia implements ApplicationRunner {

  protected static final Logger LOGGER = LoggerFactory.getLogger(Babilonia.class);

  @Override
  public void run(ApplicationArguments args) throws Exception {
    try {
      if (args.containsOption("skipInit")) {
        LOGGER.info("Inicialização de dados ignorada por parâmetro.");
        return;
      }
    } catch (Exception e) {
      LOGGER.error("Erro na inicialização da aplicação: {}", e);
    }
  }

  public static void main(String[] args) {
    LOGGER.info("Babilonia iniciando...");
    SpringApplication.run(Babilonia.class, args);
  }

}
