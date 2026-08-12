package br.com.wagnersoft.babilonia.dto;

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
 *  Resultado da consulta de informações de localidade.
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
public class LocResultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Nome da localidade", example = "Rio de Janeiro")
  private String descricao;

  @Schema(description = "Tipo", example = "URBANO ou RURAL")
  private String tipo;

  @Schema(description = "Nível", example = "1, 2, 3, ...")
  private String nivel;

  @Schema(description = "Categoria", example = "Cidade, Vila, Povoado")
  private String categoria;
  
  @Schema(description = "Bairro", example = "Jardim Alegria")
  private String bairro;

  @Schema(description = "Subdistrito", example = "Alegre")
  private String subdistrito;

  @Schema(description = "Distrito", example = "Central")
  private String distrito;

  @Schema(description = "Município", example = "Rio de Janeiro")
  private String municipio;
  
  @Schema(description = "Microrregião", example = "Centro-Leste")
  private String microregiao;

  @Schema(description = "Mesorregião", example = "Leste Fluminense")
  private String mesoregiao;

  @Schema(description = "UF", example = "RJ")
  private String uf;

  @Schema(description = "Latitude", example = "-23.8987")
  private String latitude;

  @Schema(description = "Longitude", example = "-49.8987")
  private String longitude;

  @Schema(description = "Altitude", example = "138.2345")
  private String altitude;
  
  @Schema(description = "Data em que a consulta atual foi gerada", example = "2026-07-18")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate consultaData;

  public static LocResultDTO empty() {
    return LocResultDTO.builder().build();
  }
  
}
