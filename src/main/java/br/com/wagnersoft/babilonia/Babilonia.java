package br.mil.eb.wagnersoft.babilonia;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class Babilonia implements CommandLineRunner {

  protected static final Logger LOGGER = LoggerFactory.getLogger(Babilonia.class);

  @Autowired
  DataSource dataSource;

  public static void main(String[] args) {
    LOGGER.info("Babilonia iniciando...");
    SpringApplication.run(Babilonia.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    /*
     * final PoolDataSource ds = (PoolDataSource) dataSource;
     * final DatabaseMetaData metadata = ds.getConnection().getMetaData();
     * LOGGER.info("Database Product Name: {}", metadata.getDatabaseProductName());
     * LOGGER.info("Database Product Version: {}", metadata.getDatabaseProductVersion());
     * LOGGER.info("Logged User: {}", metadata.getUserName());
     * LOGGER.info("JDBC Driver: {}", metadata.getDriverName());
     * LOGGER.info("Driver Version: {}", metadata.getDriverVersion());
     * LOGGER.info("DataSource: {}", ds.getClass());
     */
  }

  @Bean
  public UndertowServletWebServerFactory undertowServletWebServerFactory() {
    final UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
    LOGGER.debug("contextPath = {}", factory.getContextPath());
    // Ativar para debug de Request/Response no console
    //factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addInitialHandlerChainWrapper(handler -> {return new RequestDumpingHandler(handler);}));
    return factory;
  }

}
