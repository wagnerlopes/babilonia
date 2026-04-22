package br.com.wagnersoft.babilonia.dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Entity
@Table(name="CIDADAO")
public class Cidadao implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String cpf;

  private String nome;

  private String pai;

  private String mae;

  private String rg;

  private String sexo;

  @Column(name = "NASCIMENTO_DATA")
  private LocalDate nascimentoData;

  @Column(name = "AUDIT_DATA")
  private LocalDateTime auditData;
  
  @ManyToOne
  @JoinColumn(name = "MUNICIPIO_NASCIMENTO_CODIGO", referencedColumnName = "CODIGO")
  private Municipio municipioNascimento;

  public static Cidadao naoCadastrado(String cpf) {
    return Cidadao.builder().cpf(cpf).build();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mae == null) ? 0 : mae.hashCode());
    result = prime * result + ((nascimentoData == null) ? 0 : nascimentoData.hashCode());
    result = prime * result + ((nome == null) ? 0 : nome.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Cidadao other = (Cidadao) obj;
    if (mae == null) {
      if (other.mae != null)
        return false;
    } else if (!mae.equals(other.mae))
      return false;
    if (nascimentoData == null) {
      if (other.nascimentoData != null)
        return false;
    } else if (!nascimentoData.equals(other.nascimentoData))
      return false;
    if (nome == null) {
      if (other.nome != null)
        return false;
    } else if (!nome.equals(other.nome))
      return false;
    return true;
  }
  
}
