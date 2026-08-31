package br.com.wagnersoft.babilonia.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.data.EntityReader;

/** 
 * Serviço responsável pela leitura de dados de planilha e salvamento das informações nas entidades do modelo.
 * <p>
 * Centraliza as operações de leitura, filtragem, consolidação e salvamento 
 * das informações das entidades do modelo ornecidas pela planilha de dados externa.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class SpreadSheetResourceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpreadSheetResourceService.class);

  @Value("classpath:IBGE-localidades-2010.xls")
  private Resource excelResource;

  private final List<EntityReader<?>> readers;

  @Autowired
  public SpreadSheetResourceService(List<EntityReader<?>> readers) {
    this.readers = readers;
  }

  public void processarTodasEntidades() {
    readers.forEach(this::processarPlanilha);
  }

  /**
   *  Método genérico responsável pelo ciclo de vida da leitura e salvamento.
   *  
   * @param <T> Entidade do modelo retorna após a leitura da coluna
   * @param reader leitor de entidade
   */
  private <T> void processarPlanilha(EntityReader<T> reader) {
    Map<String, T> entidadesUnicas = new LinkedHashMap<>();

    try (InputStream is = excelResource.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {

      Sheet sheet = workbook.getSheetAt(0);

      for (Row row : sheet) {
        if (row.getRowNum() == 0) continue; // pula cabeçalho

        String chaveUnica = reader.extractUniqueKey(row);
        if (chaveUnica == null || chaveUnica.startsWith("|") || chaveUnica.endsWith("|")) continue;

        if (!entidadesUnicas.containsKey(chaveUnica)) {
          T entidade = reader.makeEntity(row);
          if (entidade != null) {
            entidadesUnicas.put(chaveUnica, entidade);
          }
        }
      }

      reader.getRepository().saveAll(entidadesUnicas.values());
      LOGGER.info("Entidades {} processadas e salvas = {}", reader.getEntityName(), entidadesUnicas.size());

    } catch (Exception e) {
      throw new RuntimeException("Erro ao carregar o arquivo xls do resources", e);
    }
  }

  public String getResourceName() {
    return Objects.requireNonNull(excelResource.getFilename());
  }

}
