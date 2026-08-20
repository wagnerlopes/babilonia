package br.com.wagnersoft.babilonia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.ToString.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 
 * Distrito.
 * 
 * <p>Um distrito de um {@link Municipio município}.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class Distrito implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String descricao;

  @NotNull
  @Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "municipio_id", updatable = false, nullable = false)
  private Municipio municipio;

  @Exclude
  @OneToMany(mappedBy = "distrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Localidade> localidades = new ArrayList<>();
  
}
