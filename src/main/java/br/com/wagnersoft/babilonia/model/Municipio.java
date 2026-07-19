package br.com.wagnersoft.babilonia.model;

import java.io.Serializable;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode.Include;

/** Entidade Municipio.
 * @author Wagner Lopes
 * @since 1.0.0
 * @version 1.0.0
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Municipio implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Include
  private Integer codigo;
  
  private String descricao;

  @Column(name = "UF_SIGLA")
  private String uf;

  @Override
  public String toString() {
    return Optional.ofNullable(descricao)
        .map(desc -> desc + Optional.ofNullable(uf).map(u -> " - " + u).orElse(""))
        .orElse("N/D");
  }
  
}
