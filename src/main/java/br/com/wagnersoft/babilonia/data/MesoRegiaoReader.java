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
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;
import br.com.wagnersoft.babilonia.repository.UfRepository;

@Service
@Order(2)
public class MesoRegiaoReader implements EntityReader<MesoRegiao> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MesoRegiaoReader.class);

  private UfRepository ufRep;

  private MesoRegiaoRepository mesoRep;

  private Map<String, Uf> ufMap;

  public MesoRegiaoReader(UfRepository ufRep, MesoRegiaoRepository mesoRep) {
    this.ufRep = ufRep;
    this.mesoRep = mesoRep;
  }

  @Override
  public MesoRegiao makeEntity(Row row) {

    if (ufMap == null || ufMap.isEmpty()) {
      ufMap = ufRep.findAll().stream()
          .collect(Collectors.toMap(uf -> uf.getDescricao().trim().toUpperCase(), uf -> uf, (existente, novo) -> existente));
    }

    String mesoDesc = getCellValue(row, COL_MESORREGIAO);
    String ufDesc = getCellValue(row, COL_UF);

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
  }

  @Override
  public String extractUniqueKey(Row row) {
    return (getCellValue(row, COL_MESORREGIAO) + "|" + getCellValue(row, COL_UF)).toUpperCase();
  }

  @Override
  public JpaRepository<MesoRegiao, ?> getRepository() {
    return mesoRep;
  }

  @Override
  public String getEntityName() {
    return "Mesorregião";
  }

}
