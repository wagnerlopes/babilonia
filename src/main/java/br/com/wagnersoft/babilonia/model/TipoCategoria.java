package br.com.wagnersoft.babilonia.model;

import lombok.Getter;

/**
 * Categoria da localidade.
 * 
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
 */
@Getter
public enum TipoCategoria {

  CIDADE(1,"Cidade"),
  VILA(2,"Vila"),
  POVOADO(3,"Povoado"),
  PROJETO_ASSENTAMENTO(4,"Projeto de Assentamento"),
  ALDEIA_INDIGENA(5,"Aldeia Indígena"),
  AREA_URBANA_INTERVENCAO(6,"Área Urbana de Intervenção");

  private final Integer id;
  
  private final String descricao;

  TipoCategoria(Integer id, String descricao) {
    this.id = id;
    this.descricao = descricao;
  }

  public static TipoCategoria getById(Integer id) {
    if (id == null) {
      return null;
    }
    
    for (TipoCategoria tipo : values()) {
      if (tipo.getId().equals(id)) {
        return tipo;
      }
    }
    
    throw new IllegalArgumentException("ID de categoria inválido: " + id);
  }  

}
