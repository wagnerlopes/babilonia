package br.com.wagnersoft.babilonia.data;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.MicroRegiaoRepository;

@Service
@Order(3)
public class MicroRegiaoReader implements EntityReader<MicroRegiao> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MicroRegiaoReader.class);

  private MicroRegiaoRepository microRep;

  private MesoRegiaoRepository mesoRep;

  private Map<String, MesoRegiao> mesoRegiaoMap;

  public MicroRegiaoReader(MicroRegiaoRepository microRep, MesoRegiaoRepository mesoRep) {
    this.microRep = microRep;
    this.mesoRep = mesoRep;
  }

  @Override
  public MicroRegiao makeEntity(Row row) {

    if (mesoRegiaoMap == null || mesoRegiaoMap.isEmpty()) {
      this.mesoRegiaoMap =  mesoRep.findAll().stream()
          .collect(Collectors.toMap(m -> m.getDescricao().trim().toUpperCase(), m -> m, (existente, novo) -> existente));
    }

    String microDesc = getCellValue(row, COL_MICRORREGIAO);
    String mesoDesc = getCellValue(row, COL_MESORREGIAO);

    if (mesoDesc.isEmpty() || microDesc.isEmpty()) {
      return null;
    }

    MesoRegiao mesoObj = mesoRegiaoMap.get(mesoDesc.toUpperCase());
    if (mesoObj == null) {
      LOGGER.warn("Mesorregião não encontrada no banco: {}", mesoDesc);
      return null;
    }

    MicroRegiao m = new MicroRegiao();
    m.setDescricao(microDesc);
    m.setMesoregiao(mesoObj);

    return m;
  }

  @Override
  public String extractUniqueKey(Row row) {
    return (getCellValue(row, COL_MICRORREGIAO) + "|" + getCellValue(row, COL_MESORREGIAO)).toUpperCase();
  }

  @Override
  public JpaRepository<MicroRegiao, ?> getRepository() {
    return microRep;
  }

  @Override
  public String getEntityName() {
    return "Microrregião";
  }

}
