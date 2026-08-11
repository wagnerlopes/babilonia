package br.com.wagnersoft.babilonia.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 
 * Categoria (urbana/rural).
 * 
 * <p>Categoria de uma {@link Localidade localidade}.
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
public class Categoria implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Include
  private String id;

  private String descricao;

}
