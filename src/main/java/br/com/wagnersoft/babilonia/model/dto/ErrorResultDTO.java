package br.com.wagnersoft.babilonia.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

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
 *  Resultado da consulta com erro.
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
@Schema(description = "Objeto de resposta das informações de localidade")
public class ErrorResultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Valor pesquisado", example = "Rio de Janeiro")
  private String descricao;

  @Schema(description = "Código do erro", example = "1, 2, 3 ...")
  private Integer situacaoCodigo;

  @Schema(description = "Descrição do erro", example = "Expirado")
  private String situacaoDescricao;

  @Schema(description = "Data em que a consulta atual foi gerada", example = "2026-07-18")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate consultaData;

  public static ErrorResultDTO empty() {
    return ErrorResultDTO.builder().build();
  }
  
}
