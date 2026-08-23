package br.com.wagnersoft.babilonia.model;

import lombok.Getter;

/**
 * Status da pesquisa de informações.
 * 
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
 */
@Getter
public enum TipoSituacao {

  ERRO_INTERNO("Erro interno na aplicação.", 0),
  REQUESICAO_VALIDA("Requisição válida.", 1),
  ERRO_VALIDACAO("Erro na validação de parâmetro.", 2),
  NAO_ENCONTRADO("Informação não encontrada.", 3),
  REQUISICAO_INVALIDA("Requisição inválida.", 4),
  TOKEN_EXPIRADO("Token de acesso expirado. Faça login novamente.", 5),
  TOKEN_INVALIDO("Token de acesso inválido ou corrompido.", 6);

  private Integer codigo;

  private String descricao;

  TipoSituacao(String descricao, Integer codigo) {
    this.descricao = descricao;
    this.codigo = codigo;
  }

}
