package br.com.wagnersoft.babilonia.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
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

import br.com.wagnersoft.babilonia.model.Categoria;
import br.com.wagnersoft.babilonia.model.Coordenada;
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.model.Localidade;
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.model.TipoCategoria;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.CategoriaRepository;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;
import br.com.wagnersoft.babilonia.repository.LocalidadeRepository;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.MicroRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;
import br.com.wagnersoft.babilonia.repository.UfRepository;

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
public class ExcelResourceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelResourceService.class);

  private final DataFormatter dataFormatter = new DataFormatter();

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

  @Autowired
  private LocalidadeRepository locRep;

  @Autowired
  private CategoriaRepository catRep;

  @Transactional
  public void readCategoria() {
    processarPlanilha(
        row -> {
          String idStr = getCellValue(row, 10);
          String descStr = getCellValue(row, 11);
          String nivelStr = getCellValue(row, 9);

          if (idStr.isEmpty() || descStr.isEmpty()) {
            return null;
          }

          // Garante a conversão segura do nível se a coluna não estiver vazia
          Integer nivel = nivelStr.isEmpty() ? null : Integer.valueOf(nivelStr);
          TipoCategoria tipo = TipoCategoria.getById(nivel);

          Categoria categoria = new Categoria();
          categoria.setId(idStr);
          categoria.setDescricao(tipo);
          return categoria;
        },
        // Se a coluna 10 estiver vazia, ignora a geração de chave para não "bloquear" o Map
        row -> {
          String id = getCellValue(row, 10);
          return id.isEmpty() ? null : id.toUpperCase();
        },
        catRep,
        "Categoria"
        );
  }

  /**
   * Leitura de Mesorregião associada a UF. (Col 7 = meso - Col 8 = UF)
   */
  public void readMesoregiao() {

    // Mapa de entidades Uf (Descrição, Uf)
    Map<String, Uf> ufMap = ufRep.findAll().stream()
        .collect(Collectors.toMap(
            uf -> uf.getDescricao().trim().toUpperCase(), 
            uf -> uf, 
            (existente, novo) -> existente));

    processarPlanilha(
        // criarEntidade
        row -> {
          String mesoDesc = getCellValue(row, 7);
          String ufDesc = getCellValue(row, 8);

          if (mesoDesc.isEmpty() || ufDesc.isEmpty()) {
            return null;
          }

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
        // extrairChaveUnica
        row -> (getCellValue(row, 7) + "|" + getCellValue(row, 8)).toUpperCase(),
        mesoRep,
        "Mesorregião"
        );
  }

  /**
   * Leitura de Microrregião associada a uma Mesorregião. (col 6 = micro - col 7 = meso)
   */
  public void readMicroregiao() {

    Map<String, MesoRegiao> mesoMap = mesoRep.findAll().stream()
        .collect(Collectors.toMap(
            m -> m.getDescricao().trim().toUpperCase(), 
            m -> m, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        // criarEntidade
        row -> {
          String microDesc = getCellValue(row, 6);
          String mesoDesc = getCellValue(row, 7);

          if (mesoDesc.isEmpty() || microDesc.isEmpty()) {
            return null;
          }

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
        // extrairChaveUnica
        row -> (getCellValue(row, 6) + "|" + getCellValue(row, 7)).toUpperCase(),
        microRep,
        "Microrregião"
        );
  }

  /**
   * Leitura de Município associado a uma Microrregião. (col 5 = mun - col 6 = micro)
   */
  public void readMunicipio() {

    Map<String, MicroRegiao> microMap = microRep.findAll().stream()
        .collect(Collectors.toMap(
            m -> m.getDescricao().trim().toUpperCase(), 
            m -> m, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        // criarEntidade
        row -> {
          String munDesc = getCellValue(row, 5);
          String microDesc = getCellValue(row, 6);

          if (munDesc.isEmpty() || microDesc.isEmpty()) {
            return null;
          }

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
        // extrairChaveUnica
        row -> (getCellValue(row, 5) + "|" + getCellValue(row, 6)).toUpperCase(),
        munRep,
        "Município"
        );
  }

  /**
   * Leitura de Distrito associado a um Município. (col 4 = dis - col 5 = mun)
   */
  public void readDistrito() {

    Map<String, Municipio> munMap = munRep.findAll().stream()
        .collect(Collectors.toMap(
            mun -> mun.getDescricao().trim().toUpperCase(), 
            mun -> mun, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        // criarEntidade
        row -> {
          String disDesc = getCellValue(row, 4);
          String munDesc = getCellValue(row, 5);

          if (munDesc.isEmpty() || disDesc.isEmpty()) {
            return null;
          }

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
        // extrairChaveUnica
        row -> (getCellValue(row, 4) + "|" + getCellValue(row, 5)).toUpperCase(),
        disRep,
        "Distrito"
        );
  }

  /**
   * Leitura de Distrito associado a um Localidade.
   */
  public void readLocalidade() {

    Map<String, Categoria> catMap = catRep.findAll().stream()
        .collect(Collectors.toMap(
            Categoria::getId, 
            c -> c,
            (existente, novo) -> existente
            ));

    Map<String, Distrito> disMap = disRep.findAll().stream()
        .collect(Collectors.toMap(
            dis -> dis.getDescricao().trim().toUpperCase(), 
            dis -> dis, 
            (existente, novo) -> existente
            ));

    processarPlanilha(
        row -> {
          String disDesc = getCellValue(row, 4);
          String locDesc = getCellValue(row, 12);
          String catId = getCellValue(row, 10);

          if (locDesc.isEmpty() || disDesc.isEmpty()) {
            return null;
          }

          Distrito disObj = disMap.get(disDesc.toUpperCase());
          if (disObj == null) {
            LOGGER.warn("Distrito não encontrado no banco: {}", disDesc);
            return null;
          }

          String longitude = this.getCellValue(row, 13).replace(",", ".");
          String latitude = this.getCellValue(row, 14).replace(",", ".");
          String altitude = this.getCellValue(row, 15).replace(",", ".");

          Coordenada coor = new Coordenada();
          coor.setLongitude(Double.valueOf(longitude));
          coor.setLatitude(Double.valueOf(latitude));
          coor.setAltitude(Double.valueOf(altitude));

          Integer nivel =Integer.valueOf(this.getCellValue(row, 9));

          Categoria catObj = catMap.get(catId);

          Localidade loc = new Localidade();
          loc.setDescricao(locDesc);
          loc.setTipo(this.getCellValue(row, 1));
          loc.setBairro(this.getCellValue(row, 2));
          loc.setSubdistrito(getCellValue(row, 3));
          loc.setDistrito(disObj);
          loc.setNivel(nivel);
          loc.setCategoria(catObj);
          loc.setCoordenada(coor);
          return loc;
        },
        // extrairChaveUnica
        row -> (getCellValue(row, 4) + "|" + getCellValue(row, 12)).toUpperCase(),
        locRep,
        "Localidade"
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
      Function<Row, T> criarEntidade,
      Function<Row, String> extrairChaveUnica,
      JpaRepository<T, ?> repository,
      String nomeEntidade) {

    Map<String, T> entidadesUnicas = new LinkedHashMap<>();

    try (InputStream is = excelResource.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {

      Sheet sheet = workbook.getSheetAt(0);

      for (Row row : sheet) {

        if (row.getRowNum() == 0) continue; // Pula cabeçalho

        String chaveUnica = extrairChaveUnica.apply(row);

        if (chaveUnica.startsWith("|") || chaveUnica.endsWith("|")) continue; // Pula chave inválida

        if (!entidadesUnicas.containsKey(chaveUnica)) {
          T entidade = criarEntidade.apply(row);
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

    if (row == null) return "";

    Cell cell = row.getCell(cellIndex);
    if (cell == null) return "";

    return dataFormatter.formatCellValue(cell).trim();
  }

}
