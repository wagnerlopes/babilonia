package br.com.wagnersoft.babilonia.model;

/**
 * Nível da localidade.
 * 
 * <p>Nível varia de 1 a 6 de acordo com o tamanho da localidade.</p>.
 * 
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
 */
public enum NivelLocalidade {

  CIDADE(1,"Cidade"),
  VILA(2,"Vila"),
  POVOADO(3,"Povoado"),
  PROJETO_ASSENTAMENTO(4,"Projeto de Assentamento"),
  ALDEIA_INDIGENA(5,"Aldeia Indígena"),
  AREA_URBANA_INTERVENCAO(6,"Área Urbana de Intervenção");

  private final Integer id;

  private final String descricao;

  NivelLocalidade(Integer id, String descricao) {
    this.id = id;
    this.descricao = descricao;
  }

  public Integer getId() {
    return id;
  }

  public String getDescricao() {
    return descricao;
  }

  public static NivelLocalidade getById(Integer id) {
    if (id == null) {
      return null;
    }

    for (NivelLocalidade tipo : values()) {
      if (tipo.getId().equals(id)) {
        return tipo;
      }
    }

    throw new IllegalArgumentException("ID de categoria inválido: " + id);
  }  

}
