package br.com.sermil.webservices.conectagov.dominio.enums;

import lombok.Getter;

@Getter
public enum SituacaoEnum {

  FORA_AR("SERVIÇO TEMPORARIAMENTE FORA DO AR. TENTE MAIS TARDE.", 0),
  EM_DIA("EM DIA.", 1),
  EM_DEBITO("EM DÉBITO.", 2),
  NAO_ENCONTRADO("REGISTRO NÃO ENCONTRADO.", 3);

  private Integer codigo;

  private String descricao;

  SituacaoEnum(String descricao, Integer codigo) {
    this.descricao = descricao;
    this.codigo = codigo;
  }

}
