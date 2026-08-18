package br.com.wagnersoft.babilonia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 
 * Município.
 * 
 * <p>Um município de uma {@link MicroRegiao microregião}.
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
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

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "microregiao_id", updatable = false, nullable = false)
  private MicroRegiao microregiao;
  
  @OneToMany(mappedBy = "municipio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<Distrito> distritos = new ArrayList<>();
  
  @Override
  public String toString() {
    return Optional.ofNullable(descricao)
        .map(desc -> desc + Optional.ofNullable(uf).map(u -> " - " + u).orElse(""))
        .orElse("N/D");
  }
  
}
