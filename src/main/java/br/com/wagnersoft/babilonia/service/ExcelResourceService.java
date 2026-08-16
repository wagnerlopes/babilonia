package br.com.wagnersoft.babilonia.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

import br.com.wagnersoft.babilonia.model.Mesoregiao;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.MesoregiaoRepository;
import br.com.wagnersoft.babilonia.repository.UfRepository;

@Service
public class ExcelResourceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelResourceService.class);

  @Value("classpath:ibge-localidades-2010.xls")
  private Resource excelResource;

  @Autowired
  private UfRepository ufRep;

  @Autowired
  private MesoregiaoRepository mesoRep;

  public void readMesoregiao() {

    Map<String, Uf> ufMap = ufRep.findAll().stream()
        .collect(Collectors.toMap(uf -> uf.getDescricao().trim().toUpperCase(), uf -> uf, (existente, novo) -> existente));

    Map<String, Mesoregiao> mesorregioesUnicas = new LinkedHashMap<>();

    try (InputStream is = excelResource.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {

      Sheet sheet = workbook.getSheetAt(0);

      for (Row row : sheet) {
        if (row.getRowNum() == 0) continue; // Pula o cabeçalho

        String mesoDesc = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
        String ufDesc = row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "";

        if (mesoDesc.isEmpty() || ufDesc.isEmpty()) continue;

        String chaveUnica = (mesoDesc + "|" + ufDesc).toUpperCase();

        if (!mesorregioesUnicas.containsKey(chaveUnica)) {
          
          Uf ufObj = ufMap.get(ufDesc.toUpperCase());

          if (ufObj != null) {
            Mesoregiao m = new Mesoregiao();
            m.setDescricao(mesoDesc);
            m.setUf(ufObj);
            mesorregioesUnicas.put(chaveUnica, m);
          } else {
            LOGGER.warn("UF não encontrada no banco: {}", ufDesc);
          }
          
        }

      }
      
      mesoRep.saveAll(mesorregioesUnicas.values());
      
      LOGGER.info("Mesorregioes = {}", mesorregioesUnicas.size());

    } catch (Exception e) {
      throw new RuntimeException("Erro ao carregar o arquivo xls do resources", e);
    }

  }

}
