package br.com.wagnersoft.babilonia.data;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;

/**
 * DistritoReader.
 * @since 1.0.7
 * @version 1.0.7
 * @author Wagner Lopes
 */
@Service
@Order(5)
public class DistritoReader implements EntityReader<Distrito> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistritoReader.class);

  private DistritoRepository disRep;

  private MunicipioRepository munRep;

  private Map<String, Municipio> municipioMap;

  public DistritoReader(DistritoRepository disRep, MunicipioRepository munRep) {
    this.disRep = disRep;
    this.munRep = munRep;
  }

  @Override
  public Distrito makeEntity(Row row) {

    if (municipioMap == null || municipioMap.isEmpty()) {
      this.municipioMap =  munRep.findAll().stream()
          .collect(Collectors.toMap(m -> m.getDescricao().trim().toUpperCase() +
              m.getMicroregiao().getDescricao().trim().toUpperCase() +
              m.getMicroregiao().getMesoregiao().getDescricao().trim().toUpperCase() +
              m.getMicroregiao().getMesoregiao().getUf().getDescricao().trim().toUpperCase()
              , m -> m, (existente, novo) -> existente));
    }

    String ufDesc = getCellValue(row, COL_UF);
    String mesoDesc = getCellValue(row, COL_MESORREGIAO);
    String microDesc = getCellValue(row, COL_MICRORREGIAO);
    String munDesc = getCellValue(row, COL_MUNICIPIO);
    String disDesc = getCellValue(row, COL_DISTRITO);

    if (ufDesc.isEmpty() || mesoDesc.isEmpty() || microDesc.isEmpty() || munDesc.isEmpty() || disDesc.isEmpty()) {
      return null;
    }

    Municipio munObj = municipioMap.get(munDesc.toUpperCase() + microDesc.toUpperCase() + mesoDesc.toUpperCase() + ufDesc.toUpperCase());
    if (munObj == null) {
      LOGGER.info("Município não encontrada no banco: {}", munDesc);
      return null;
    }

    Distrito dis = new Distrito();
    dis.setDescricao(disDesc);
    dis.setMunicipio(munObj);

    return dis;

  }

  @Override
  public String extractUniqueKey(Row row) {
    return (getCellValue(row, COL_DISTRITO) + "|" + getCellValue(row, COL_MUNICIPIO) + "|" + getCellValue(row, COL_MICRORREGIAO) + "|" + getCellValue(row, COL_MESORREGIAO) + "|" + getCellValue(row, COL_UF)).toUpperCase();
  }

  @Override
  public JpaRepository<Distrito, ?> getRepository() {
    return disRep;
  }

  @Override
  public String getEntityName() {
    return "Distrito";
  }

}
