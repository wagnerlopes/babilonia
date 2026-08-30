package br.com.wagnersoft.babilonia.spredSheetReader;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.Categoria;
import br.com.wagnersoft.babilonia.model.Coordenada;
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.model.Localidade;
import br.com.wagnersoft.babilonia.repository.CategoriaRepository;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;
import br.com.wagnersoft.babilonia.repository.LocalidadeRepository;

@Service
@Order(6)
public class LocalidadeReader implements SpredSheetEntityReader<Localidade> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalidadeReader.class);

  private CategoriaRepository catRep;

  private DistritoRepository disRep;

  private LocalidadeRepository locRep;

  private Map<String, Categoria> categoriaMap;

  private Map<String, Distrito> distritoMap;

  public LocalidadeReader(CategoriaRepository catRep, DistritoRepository disRep, LocalidadeRepository locRep) {
    this.catRep = catRep;
    this.disRep = disRep;
    this.locRep = locRep;
  }

  @Override
  public Localidade makeEntity(Row row) {

    if (categoriaMap == null || categoriaMap.isEmpty()) {
      this.categoriaMap =  catRep.findAll().stream()
          .collect(Collectors.toMap(Categoria::getId, c -> c, (existente, novo) -> existente));
    }

    if (distritoMap == null || distritoMap.isEmpty()) {
      this.distritoMap =  disRep.findAll().stream()
          .collect(Collectors.toMap(dis -> dis.getDescricao().trim().toUpperCase(), dis -> dis, (existente, novo) -> existente));
    }

    String disDesc = getCellValue(row, COL_DISTRITO);
    String locDesc = getCellValue(row, COL_LOCALIDADE);
    String catId = getCellValue(row, COL_CATEGORIA_ID);

    if (locDesc.isEmpty() || disDesc.isEmpty()) {
      return null;
    }

    Distrito disObj = this.distritoMap.get(disDesc.toUpperCase());
    if (disObj == null) {
      LOGGER.warn("Distrito não encontrado no banco: {}", disDesc);
      return null;
    }

    Categoria catObj = categoriaMap.get(catId);

    String longitude = this.getCellValue(row, COL_LONGITUDE).replace(",", ".");
    String latitude = this.getCellValue(row, COL_LATITUDE).replace(",", ".");
    String altitude = this.getCellValue(row, COL_ALTITUDE).replace(",", ".");

    Coordenada coor = new Coordenada();
    coor.setLongitude(Double.valueOf(longitude));
    coor.setLatitude(Double.valueOf(latitude));
    coor.setAltitude(Double.valueOf(altitude));

    Integer nivel = Integer.valueOf(getCellValue(row, COL_NIVEL));

    Localidade loc = new Localidade();
    loc.setDescricao(locDesc);
    loc.setTipo(this.getCellValue(row, COL_TIPO));
    loc.setBairro(this.getCellValue(row, COL_BAIRRO));
    loc.setSubdistrito(getCellValue(row, COL_SUBDISTRITO));
    loc.setDistrito(disObj);
    loc.setNivel(nivel);
    loc.setCategoria(catObj);
    loc.setCoordenada(coor);
    return loc;
  }

  @Override
  public String extractUniqueKey(Row row) {
    return (getCellValue(row, COL_DISTRITO) + "|" + getCellValue(row, COL_LOCALIDADE)).toUpperCase();
  }

  @Override
  public JpaRepository<Localidade, ?> getRepository() {
    return locRep;
  }

  @Override
  public String getEntityName() {
    return "Localidade";
  }

}
