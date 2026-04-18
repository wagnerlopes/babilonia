package br.com.wagnersoft.babilonia.dominio;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entidade Municipio.
 * @author Abreu Lopes
 * @since 0.1
 * @version 0.1
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Municipio implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Integer codigo;
  
  private String descricao;

  @Column(name = "UF_SIGLA")
  private String uf;

  @Override
  public String toString() {
    return this.descricao != null ? this.descricao + " - " + this.uf : "N/D";
  }
  
}
