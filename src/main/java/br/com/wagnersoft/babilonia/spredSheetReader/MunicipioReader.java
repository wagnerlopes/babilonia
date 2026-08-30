package br.com.wagnersoft.babilonia.spredSheetReader;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.repository.MicroRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;

@Service
@Order(4)
public class MunicipioReader implements SpredSheetEntityReader<Municipio> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioReader.class);

  private MicroRegiaoRepository microRep;

  private MunicipioRepository munRep;

  private Map<String, MicroRegiao> microRegiaoMap;

  public MunicipioReader(MicroRegiaoRepository microRep, MunicipioRepository munRep) {
    this.microRep = microRep;
    this.munRep = munRep;
  }

  @Override
  public Municipio makeEntity(Row row) {

    if (microRegiaoMap == null || microRegiaoMap.isEmpty()) {
      this.microRegiaoMap =  microRep.findAll().stream()
          .collect(Collectors.toMap(m -> m.getDescricao().trim().toUpperCase(), m -> m, (existente, novo) -> existente));
    }

    String munDesc = getCellValue(row, COL_MUNICIPIO);
    String microDesc = getCellValue(row, COL_MICRORREGIAO);

    if (munDesc.isEmpty() || microDesc.isEmpty()) {
      return null;
    }

    MicroRegiao microObj = microRegiaoMap.get(microDesc.toUpperCase());
    if (microObj == null) {
      LOGGER.warn("Microrregião não encontrada no banco: {}", microDesc);
      return null;
    }

    Municipio m = new Municipio();
    m.setDescricao(munDesc);
    m.setMicroregiao(microObj);
    m.setUf(microObj.getMesoregiao().getUf().getSigla());

    return m;
  }

  @Override
  public String extractUniqueKey(Row row) {
    return (getCellValue(row, COL_MUNICIPIO) + "|" + getCellValue(row, COL_MICRORREGIAO)).toUpperCase();
  }

  @Override
  public JpaRepository<Municipio, ?> getRepository() {
    return munRep;
  }

  @Override
  public String getEntityName() {
    return "Município";
  }

}
