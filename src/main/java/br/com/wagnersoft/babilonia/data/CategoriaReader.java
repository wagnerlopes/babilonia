package br.com.wagnersoft.babilonia.data;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.Categoria;
import br.com.wagnersoft.babilonia.model.NivelLocalidade;
import br.com.wagnersoft.babilonia.repository.CategoriaRepository;

@Service
@Order(1)
public class CategoriaReader implements EntityReader<Categoria> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaReader.class);

  private CategoriaRepository catRep;

  public CategoriaReader(CategoriaRepository catRep) {
    this.catRep = catRep;
  }

  @Override
  public Categoria makeEntity(Row row) {

    // Informações lidas da planilha
    String idStr = getCellValue(row, COL_CATEGORIA_ID);
    String descStr = getCellValue(row, COL_CATEGORIA_DESC);
    String nivelStr = getCellValue(row, COL_NIVEL);

    if (idStr.isEmpty() || descStr.isEmpty()) {
      LOGGER.warn("Categoria não encontrada na planilha: {}", descStr);
      return null;
    }

    // Garante a conversão segura do nível se a coluna não estiver vazia
    Integer nivelId = nivelStr.isEmpty() ? null : Integer.valueOf(nivelStr);
    NivelLocalidade nivelLocalidade = NivelLocalidade.getById(nivelId);
    LOGGER.debug("{}", nivelLocalidade);
    
    // Definição da categoria com base no nível
    Categoria categoria = new Categoria();
    categoria.setId(idStr);
    categoria.setNivelLocalidade(nivelLocalidade);

    return categoria;
  }

  @Override
  public String extractUniqueKey(Row row) {
    String id = getCellValue(row, COL_CATEGORIA_ID);
    // Se a coluna 10 estiver vazia, ignora a geração de chave para não "bloquear" o Map
    return id.isEmpty() ? null : id.toUpperCase();
  }

  @Override
  public JpaRepository<Categoria, ?> getRepository() {
    return catRep;
  }

  @Override
  public String getEntityName() {
    return "Categoria";
  }

}
