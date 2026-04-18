package br.com.wagnersoft.babilonia.domini.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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

/** DTO de consulta de informações de cidadão.
 * @author WagnerSoft
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
@JsonInclude(Include.NON_NULL)
public class CidadaoConsultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Size(max=11, message="CPF com 11 caracteres")
  private String cpf;
  
  @NotEmpty(message="Preencha o campo nome do cidadão")
  @Size(min=3, max=250, message="Nome de 3 a 250 caracteres")
  private String nome;

  @NotEmpty(message="Preencha o campo nome da mãe")
  @Size(min=3, max=250, message="Nome da mãe de 3 a 250 caracteres")
  private String nomeMae;
  
  @NotNull(message="Preencha o campo data no formato yyyymmdd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", locale = "pt-BR", timezone = "Brazil/East")
  private LocalDate dataNascimento;

  @NotNull(message="Preencha o campo Força")
  private Integer forca;
  
}
