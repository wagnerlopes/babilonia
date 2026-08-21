package br.com.wagnersoft.babilonia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wagnersoft.babilonia.model.Categoria;

/**
 * Repositório de dados para a entidade {@link Categoria}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de uma categoria.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface CategoriaRepository extends JpaRepository<Categoria, String> {
  
}
