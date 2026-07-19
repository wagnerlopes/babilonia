package br.com.wagnersoft.babilonia.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de consulta de informações de cidadão.
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
@Schema(description = "Objeto de requisição para consulta de dados do cidadão")
public class CidadaoConsultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Somente os 11 dígitos do CPF", example = "22222222222", requiredMode = RequiredMode.NOT_REQUIRED)
  @Size(max = 11, message = "CPF com 11 caracteres")
  private String cpf;
  
  @Schema(description = "Nome completo do cidadão", example = "Cidadao A", minLength = 3, maxLength = 250, requiredMode = RequiredMode.REQUIRED)
  @NotEmpty(message = "Preencha o campo nome do cidadão")
  @Size(min = 3, max = 250, message = "Nome de 3 a 250 caracteres")
  private String nome;

  @Schema(description = "Nome completo da mãe do cidadão", example = "Mae do Cidadao A", minLength = 3, maxLength = 250, requiredMode = RequiredMode.REQUIRED)  
  @NotEmpty(message = "Preencha o campo nome da mãe")
  @Size(min = 3, max = 250, message = "Nome da mãe de 3 a 250 caracteres")
  private String nomeMae;
  
  @Schema(description = "Data de nascimento no formato yyyyMMdd", example = "19980907", requiredMode = RequiredMode.REQUIRED)  
  @NotNull(message = "Preencha o campo data no formato yyyymmdd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate dataNascimento;

}
