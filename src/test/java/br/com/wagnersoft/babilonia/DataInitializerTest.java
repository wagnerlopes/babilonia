package br.com.wagnersoft.babilonia;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;

import br.com.wagnersoft.babilonia.health.DataInitializerHealthIndicator;
import br.com.wagnersoft.babilonia.init.DataInitializer;
import br.com.wagnersoft.babilonia.service.SpreadSheetResourceService;

class DataInitializerTest {

  @Test
  void testRunExecutesAllExcelTasks() throws Exception {
    
    // Mocks necessarios
    SpreadSheetResourceService mockSpreadSvc = mock(SpreadSheetResourceService.class);
    DataInitializerHealthIndicator mockhealthIndicator = mock(DataInitializerHealthIndicator.class);

    // Instancia DataInitializer com os mocks
    DataInitializer initializer = new DataInitializer(mockSpreadSvc, mockhealthIndicator);

    // Executa o método run
    initializer.run();

    // Verifica se todos os métodos foram chamados
    verify(mockSpreadSvc).getResourceName();
    verify(mockSpreadSvc).processarTodasEntidades();

    // Garante que não houve chamadas inesperadas
    verifyNoMoreInteractions(mockSpreadSvc);
  }

}
