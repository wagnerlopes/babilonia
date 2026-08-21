package br.com.wagnersoft.babilonia;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import br.com.wagnersoft.babilonia.service.ExcelResourceService;
import jakarta.servlet.Servlet;

/** 
 * Babilonia runner application.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Babilonia implements CommandLineRunner {

  protected static final Logger LOGGER = LoggerFactory.getLogger(Babilonia.class);

  @Autowired
  private DataSource dataSource;

  @Autowired
  private ExcelResourceService excelSvc;
  
  @Bean
  public ServletRegistrationBean<Servlet> h2ServletRegistration() {
    final Servlet servlet = new org.h2.server.web.JakartaWebServlet();
    LOGGER.info("{}", servlet);
    return new ServletRegistrationBean<>(servlet, "/h2-console/*");
  }
  
  public static void main(String[] args) {
    LOGGER.warn("Babilonia iniciando...");
    SpringApplication.run(Babilonia.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    final DataSource ds = dataSource;
    LOGGER.info("DB: {}", ds.getConnection());
    LOGGER.info("Timeout = {}", ds.getLoginTimeout());
    excelSvc.readCategoria();
    excelSvc.readMesoregiao();
    excelSvc.readMicroregiao();
    excelSvc.readMunicipio();
    excelSvc.readDistrito();
    excelSvc.readLocalidade();
  }

  /*
  @Bean
  public UndertowServletWebServerFactory undertowServletWebServerFactory() {
    final UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
    LOGGER.debug("contextPath = {}", factory.getContextPath());
    // Ativar para debug de Request/Response no console
    //factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addInitialHandlerChainWrapper(handler -> {return new RequestDumpingHandler(handler);}));
    return factory;
  }
  */

}
