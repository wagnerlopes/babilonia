package br.com.wagnersoft.babilonia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.Localidade;

/**
 * Repositório de dados para a entidade {@link Localidade}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de localide geográfica.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface LocalidadeRepository extends JpaRepository<Localidade, Integer> {

  /**
   * Busca localidade cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de localidades correspondentes ao filtro.
   */
  @Query("SELECT g FROM Localidade g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT(:descricao, '%'))")
  List<Localidade> findByDescricao(@Param("descricao") String descricao);

}
