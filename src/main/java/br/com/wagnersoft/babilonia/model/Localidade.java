package br.com.wagnersoft.babilonia.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

/** 
 * Localidade geográfica.
 * 
 * <p>Um local geográfico habitado.</p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class Localidade implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NonNull
  private String descricao;

  @NotNull
  @NonNull
  private String tipo;

  @NotNull
  @NonNull
  private Integer nivel;

  private String bairro;

  private String subdistrito;

  private Double latitude;

  private Double longitude;

  private Double altitude;

  @NotNull
  @Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "distrito_id", updatable = false, nullable = false)
  private Distrito distrito;

  @NotNull
  @Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "categoria_id", updatable = false, nullable = false)
  private Categoria categoria;

}
