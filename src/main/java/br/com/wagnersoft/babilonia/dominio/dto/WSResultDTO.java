package br.mil.eb.wagnersoft.babilonia.domini.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** DTO resultado da consulta de informações em um subsistema.
 * @author Abreu Lopes
 * @since 0.1
 * @version 0.1
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WSResultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String cpf;
  
  private String nome;

  private String mae;

  private String pai;

  private Integer situacaoCodigo;

  private String situacaoDescricao;

  private String certificadoSigla;

  private String certificadoDescricao;

  private String certificadoData;

  private String validadeData;
  
  private String nascimentoLocal;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate nascimentoData;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate atualizacaoData;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate consultaData;
  
}
