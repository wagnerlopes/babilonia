package br.com.wagnersoft.babilonia.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.wagnersoft.babilonia.health.DataInitializerHealthIndicator;
import br.com.wagnersoft.babilonia.service.SpreadSheetResourceService;

/**
 * Implementação de {@link CommandLineRunner} para carga de dados de planilha.
 * 
 * <p>Realiza a carga de: messorregião, microrregião, município, distrito e localidade.</p>
 *
 * <p>Para executar várias tarefas pode-se criar uma lista Runnable:<br/>

 *  <code>
 *   List<Runnable> tasks = List.of(svc::readCategoria, svc::readMesoregiao, svc::readMicroregiao);
 *   tasks.forEach(Runnable::run);
 *  </code>
 *  
 * </p>
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

  private final DataInitializerHealthIndicator healthIndicator;

  private final SpreadSheetResourceService spreadSheetSvc;
  
  public DataInitializer(SpreadSheetResourceService excelSvc, DataInitializerHealthIndicator healthIndicator) {
    LOGGER.info("Iniciando carregamento da planilha de dados: {}", excelSvc.getResourceName());
    this.spreadSheetSvc = excelSvc;
    this.healthIndicator = healthIndicator;
  }

  @Override
  public void run(String... args) throws Exception {
    spreadSheetSvc.processarTodasEntidades();
    healthIndicator.markInitialized();
    LOGGER.info("Carregamento da planilha finalizado");
  }

}
