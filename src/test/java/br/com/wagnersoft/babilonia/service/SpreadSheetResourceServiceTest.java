package br.com.wagnersoft.babilonia.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.wagnersoft.babilonia.data.EntityReader;

@ExtendWith(MockitoExtension.class)
class SpreadSheetResourceServiceTest {

  @Mock
  private Resource excelResource;

  @Mock
  private EntityReader<Object> entityReader;

  @Mock
  private JpaRepository<Object, Integer> repository;

  private SpreadSheetResourceService service;

  @BeforeEach
  void setup() {
    service = new SpreadSheetResourceService(List.of(entityReader));
    ReflectionTestUtils.setField(service, "excelResource", excelResource);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Test
  void processarTodasEntidades_deveProcessarEPersistirComSucesso() throws Exception {
    // Arrange
    byte[] excelContent = criarPlanilhaDummy();
    when(excelResource.getInputStream()).thenReturn(new ByteArrayInputStream(excelContent));

    when(entityReader.getRepository()).thenReturn((JpaRepository) repository);
    when(entityReader.getEntityName()).thenReturn("MinhaEntidade");

    // Simula as chaves únicas retornadas para as linhas 1, 2 e 3
    when(entityReader.extractUniqueKey(any(Row.class)))
    .thenAnswer(invocation -> {
      Row row = invocation.getArgument(0);
      return switch (row.getRowNum()) {
      case 1 -> "CHAVE1";
      case 2 -> "|CHAVE_INVALIDA"; // Deve ser ignorada por começar com |
      case 3 -> "CHAVE1";          // Duplicada, deve ser ignorada
      case 4 -> "CHAVE2";
      default -> null;
      };
    });

    Object entidade1 = new Object();
    Object entidade2 = new Object();

    when(entityReader.makeEntity(any(Row.class)))
    .thenAnswer(invocation -> {
      Row row = invocation.getArgument(0);
      return switch (row.getRowNum()) {
      case 1 -> entidade1;
      case 4 -> entidade2;
      default -> null;
      };
    });

    // Act
    service.processarTodasEntidades();

    // Assert
    verify(repository).saveAll(argThat(colecao -> assertThat(colecao).containsExactly(entidade1, entidade2) != null));
  }

  @Test
  void processarTodasEntidades_deveLancarRuntimeExceptionQuandoFalharAoLerResource() throws Exception {
    // Arrange
    when(excelResource.getInputStream()).thenThrow(new IOException("Erro de I/O simulação"));

    // Act & Assert
    assertThatThrownBy(() -> service.processarTodasEntidades())
    .isInstanceOf(RuntimeException.class)
    .hasMessageContaining("Erro ao carregar o arquivo xls do resources");

    verify(repository, never()).saveAll(any());
  }

  @Test
  void getResourceName_deveRetornarNomeDoArquivo() {
    // Arrange
    when(excelResource.getFilename()).thenReturn("IBGE-localidades-2010.xls");

    // Act
    String resourceName = service.getResourceName();

    // Assert
    assertThat(resourceName).isEqualTo("IBGE-localidades-2010.xls");
  }

  /**
   * Método utilitário para gerar uma planilha em memória para o teste.
   */
  private byte[] criarPlanilhaDummy() throws IOException {
    try (Workbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      
      Sheet sheet = workbook.createSheet("Localidades");

      // Criar 5 linhas (0 = Cabeçalho, 1..4 = Dados)
      for (int i = 0; i <= 4; i++) {
        Row row = sheet.createRow(i);
        row.createCell(0).setCellValue("Dado " + i);
      }

      workbook.write(out);
      return out.toByteArray();
    }
  }

}
