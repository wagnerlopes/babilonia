package br.com.wagnersoft.babilonia;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;

import br.com.wagnersoft.babilonia.health.DataInitializerHealthIndicator;
import br.com.wagnersoft.babilonia.init.DataInitializer;
import br.com.wagnersoft.babilonia.service.ExcelResourceService;

class DataInitializerTest {

  @Test
  void testRunExecutesAllExcelTasks() throws Exception {
    
    // Mocks necessarios
    ExcelResourceService mockExcelSvc = mock(ExcelResourceService.class);
    DataInitializerHealthIndicator mockhealthIndicator = mock(DataInitializerHealthIndicator.class);

    // Instancia DataInitializer com os mocks
    DataInitializer initializer = new DataInitializer(mockExcelSvc, mockhealthIndicator);

    // Executa o método run
    initializer.run();

    // Verifica se todos os métodos foram chamados
    verify(mockExcelSvc).getResourceName();
    verify(mockExcelSvc).readCategoria();
    verify(mockExcelSvc).readMesoregiao();
    verify(mockExcelSvc).readMicroregiao();
    verify(mockExcelSvc).readMunicipio();
    verify(mockExcelSvc).readDistrito();
    verify(mockExcelSvc).readLocalidade();

    // Garante que não houve chamadas inesperadas
    verifyNoMoreInteractions(mockExcelSvc);
  }

}
