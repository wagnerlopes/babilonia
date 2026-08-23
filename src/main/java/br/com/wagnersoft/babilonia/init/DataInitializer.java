package br.com.wagnersoft.babilonia.init;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.wagnersoft.babilonia.health.DataInitializerHealthIndicator;
import br.com.wagnersoft.babilonia.service.ExcelResourceService;

/**
 * Implementação de {@link CommandLineRunner} para carga de dados de planilha.
 * 
 * <p>Realiza a carga de: messorregião, microrregião, município, distrito e localidade.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

  private final DataInitializerHealthIndicator healthIndicator;

  private final ExcelResourceService excelSvc;

  public DataInitializer(ExcelResourceService excelSvc, DataInitializerHealthIndicator healthIndicator) {
    LOGGER.info("Iniciando carregamento da planilha de dados: {}", excelSvc.getResourceName());
    this.excelSvc = excelSvc;
    this.healthIndicator = healthIndicator;
  }

  @Override
  public void run(String... args) throws Exception {

    List<Runnable> tasks = List.of(
        excelSvc::readCategoria,
        excelSvc::readMesoregiao,
        excelSvc::readMicroregiao,
        excelSvc::readMunicipio,
        excelSvc::readDistrito,
        excelSvc::readLocalidade);

    tasks.forEach(Runnable::run);

    healthIndicator.markInitialized();
  }

}
