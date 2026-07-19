package br.com.wagnersoft.babilonia.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entidade Cidadao.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "CIDADAO")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Cidadao implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String cpf;

  @Include
  private String nome;

  @Include
  private String mae;

  @Include
  @Column(name = "NASCIMENTO_DATA")
  private LocalDate nascimentoData;

  private String pai;

  private String rg;

  private String sexo;

  @Column(name = "AUDIT_DATA")
  private LocalDateTime auditData;
  
  @ManyToOne
  @JoinColumn(name = "MUNICIPIO_NASCIMENTO_CODIGO", referencedColumnName = "CODIGO")
  private Municipio municipioNascimento;

  public static Cidadao naoCadastrado(String cpf) {
    return Cidadao.builder().cpf(cpf).build();
  }
   
}
