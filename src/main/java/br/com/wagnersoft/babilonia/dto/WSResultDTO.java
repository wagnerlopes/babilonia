package br.com.wagnersoft.babilonia.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  Resultado da consulta de informações do cidadão.
 *  
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@Schema(description = "Objeto de resposta com informações cadastrais e situação do cidadão")
public class WSResultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Número do CPF formatado ou apenas dígitos", example = "22222222222")
  private String cpf;

  @Schema(description = "Nome completo do cidadão", example = "WAGNER LOPES")  
  private String nome;

  @Schema(description = "Nome completo da mãe", example = "MARIA LOPES")  
  private String mae;

  @Schema(description = "Nome completo do pai (se houver)", example = "JOSÉ LOPES")  
  private String pai;

  @Schema(description = "Município ou local de nascimento", example = "SÃO PAULO - SP")  
  private String nascimentoLocal;

  @Schema(description = "Código numérico representativo da situação cadastral", example = "1")  
  private Integer situacaoCodigo;

  @Schema(description = "Descrição textual do status da situação do cidadão", example = "REGULAR / EM DIA")
  private String situacaoDescricao;

  @Schema(description = "Data de nascimento do cidadão", example = "1998-09-07")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate nascimentoData;

  @Schema(description = "Data e hora da última atualização dos dados no banco", example = "2026-07-18T22:59:59")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDateTime atualizacaoData;

  @Schema(description = "Data em que a consulta atual foi gerada", example = "2026-07-18")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate consultaData;

}
