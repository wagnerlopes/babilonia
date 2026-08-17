package br.com.wagnersoft.babilonia.model;

import lombok.Getter;

/**
 * Situacao do cidadão no cadastro ou status de erro na pesquisa.
 * 
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
 */
@Getter
public enum TipoSituacao {

  FORA_AR("SERVIÇO TEMPORARIAMENTE FORA DO AR. TENTE MAIS TARDE.", 0),
  EM_DIA("EM DIA.", 1),
  EM_DEBITO("EM DÉBITO.", 2),
  NAO_ENCONTRADO("REGISTRO NÃO ENCONTRADO.", 3),
  REQUISICAO_INVALIDA("REQUISIÇÃO INVÁLIDA.", 4),
  TOKEN_EXPIRADO("Token de acesso expirado. Faça login novamente.", 5),
  TOKEN_INVALIDO("Token de acesso inválido ou corrompido.", 6);

  private Integer codigo;

  private String descricao;

  TipoSituacao(String descricao, Integer codigo) {
    this.descricao = descricao;
    this.codigo = codigo;
  }

}
