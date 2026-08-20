package br.com.wagnersoft.babilonia.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.MicroRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;
import br.com.wagnersoft.babilonia.repository.UfRepository;

@Service
@Transactional(readOnly = true)
public class ExcelResourceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelResourceService.class);

  @Value("classpath:ibge-localidades-2010.xls")
  private Resource excelResource;

  @Autowired
  private UfRepository ufRep;

  @Autowired
  private MesoRegiaoRepository mesoRep;

  @Autowired
  private MicroRegiaoRepository microRep;

  @Autowired
  private MunicipioRepository munRep;

  @Autowired
  private DistritoRepository disRep;
  
  /**
   * Leitura de Mesorregião associada a UF.
   */
  public void readMesoregiao() {
    
    // Mapa de UF (Descrição, Uf)
    Map<String, Uf> ufMap = ufRep.findAll().stream()
        .collect(Collectors.toMap(
            uf -> uf.getDescricao().trim().toUpperCase(), 
            uf -> uf, 
            (existente, novo) -> existente));

    processarPlanilha(
        row -> getCellValue(row, 7), // mesoDesc
        row -> getCellValue(row, 8), // ufDesc
        (mesoDesc, ufDesc) -> {
          Uf ufObj = ufMap.get(ufDesc.toUpperCase());
          if (ufObj == null) {
            LOGGER.warn("UF não encontrada no banco: {}", ufDesc);
            return null;
          }
          MesoRegiao m = new MesoRegiao();
          m.setDescricao(mesoDesc);
          m.setUf(ufObj);
          return m;
        },
        mesoRep,
        "Mesorregião"
        );
  }

  /**
   * Leitura de Microrregião associada a uma Mesorregião.
   */
  public void readMicroregiao() {
    
    Map<String, MesoRegiao> mesoMap = mesoRep.findAll().stream()
        .collect(Collectors.toMap(
            m -> m.getDescricao().trim().toUpperCase(), 
            m -> m, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        row -> getCellValue(row, 6), // microDesc
        row -> getCellValue(row, 7), // mesoDesc
        (microDesc, mesoDesc) -> {
          MesoRegiao mesoObj = mesoMap.get(mesoDesc.toUpperCase());
          if (mesoObj == null) {
            LOGGER.warn("Mesorregião não encontrada no banco: {}", mesoDesc);
            return null;
          }
          MicroRegiao m = new MicroRegiao();
          m.setDescricao(microDesc);
          m.setMesoregiao(mesoObj);
          return m;
        },
        microRep,
        "Microrregião"
        );
  }

  /**
   * Leitura de Município associado a uma Microrregião.
   */
  public void readMunicipio() {
    
    Map<String, MicroRegiao> microMap = microRep.findAll().stream()
        .collect(Collectors.toMap(
            m -> m.getDescricao().trim().toUpperCase(), 
            m -> m, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        row -> getCellValue(row, 5), // munDesc
        row -> getCellValue(row, 6), // microDesc
        (munDesc, microDesc) -> {
          MicroRegiao microObj = microMap.get(microDesc.toUpperCase());
          if (microObj == null) {
            LOGGER.warn("Microrregião não encontrada no banco: {}", microDesc);
            return null;
          }
          Municipio m = new Municipio();
          m.setDescricao(munDesc);
          m.setMicroregiao(microObj);
          m.setUf(microObj.getMesoregiao().getUf().getSigla());
          return m;
        },
        munRep,
        "Município"
        );
  }

  /**
   * Leitura de Distrito associado a um Município.
   */
  public void readDistrito() {
    
    Map<String, Municipio> munMap = munRep.findAll().stream()
        .collect(Collectors.toMap(
            mun -> mun.getDescricao().trim().toUpperCase(), 
            mun -> mun, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        row -> getCellValue(row, 4), // disDesc
        row -> getCellValue(row, 5), // munDesc
        (disDesc, munDesc) -> {
          Municipio munObj = munMap.get(munDesc.toUpperCase());
          if (munObj == null) {
            LOGGER.warn("Município não encontrada no banco: {}", munDesc);
            return null;
          }
          Distrito dis = new Distrito();
          dis.setDescricao(disDesc);
          dis.setMunicipio(munObj);
          return dis;
        },
        disRep,
        "Distrito"
        );
  }
  
  /**
   *  Método genérico responsável pelo ciclo de vida da leitura e salvamento.
   *  
   * @param <T> Entidade do modelo retorna após a leitura da coluna
   * @param extrairFilho função lendo célula filha chamando {@link ExcelResourceService#getCellValue getCellValue}
   * @param extrairPai função lendo célula pai chamando {@link ExcelResourceService#getCellValue getCellValue}
   * @param criarEntidade Entidades filha e pai do modelo a serem manipuladas retornado a entidade filha criada
   * @param repository repositório da entidade a ser manipulada
   */
  private <T> void processarPlanilha(
      Function<Row, String> extrairFilho,
      Function<Row, String> extrairPai,
      BiFunction<String, String, T> criarEntidade,
      JpaRepository<T, ?> repository,
      String nomeEntidade) {

    Map<String, T> entidadesUnicas = new LinkedHashMap<>();

    try (InputStream is = excelResource.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
      
      Sheet sheet = workbook.getSheetAt(0);

      for (Row row : sheet) {
        if (row.getRowNum() == 0) continue; // Pula cabeçalho

        String descFilho = extrairFilho.apply(row);
        String descPai = extrairPai.apply(row);

        if (descFilho.isEmpty() || descPai.isEmpty()) continue; // pula células vazias

        String chaveUnica = (descPai + "|" + descFilho).toUpperCase();

        if (!entidadesUnicas.containsKey(chaveUnica)) {
          T entidade = criarEntidade.apply(descFilho, descPai);
          if (entidade != null) {
            entidadesUnicas.put(chaveUnica, entidade);
          }
        }
      }

      repository.saveAll(entidadesUnicas.values());
      LOGGER.info("Entidades {} processadas e salvas = {}", nomeEntidade, entidadesUnicas.size());

    } catch (Exception e) {
      throw new RuntimeException("Erro ao carregar o arquivo xls do resources", e);
    }
  }

  /**
   *  Método para leitura de célula pai e filho na planilha.
   *  
   * @param row linha da planilha
   * @param cellIndex célula a ser lida
   * @return String com o valor da célula lida
   */
  private String getCellValue(Row row, int cellIndex) {
    Cell cell = row.getCell(cellIndex);
    return cell != null ? cell.getStringCellValue().trim() : "";
  }

}
