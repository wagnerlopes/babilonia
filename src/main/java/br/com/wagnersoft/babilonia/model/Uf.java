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
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 
 * Distrito.
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
public class Uf implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String descricao;

  private String sigla;
  
  @OneToMany(mappedBy = "uf", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Mesoregiao> mesoregioes = new ArrayList<>();
  
}
