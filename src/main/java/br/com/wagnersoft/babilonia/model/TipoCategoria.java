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

  CIDADE("Cidade"),
  VILA("Vila"),
  POVOADO("Povoado"),
  NUCLEO("Núcleo"),
  LUGAREJO("Lugarejo"),
  PROJETO_ASSENTAMENTO("Projeto de Assentamento"),
  ALDEIA_INDIGENA("Aldeia Indígena"),
  AREA_URBANA_INTERVENCAO("Área Urbana de Intervenção");

  private final String descricao;

  TipoCategoria(String descricao) {
    this.descricao = descricao;
  }

}
