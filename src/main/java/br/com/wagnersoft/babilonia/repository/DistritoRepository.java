package br.com.wagnersoft.babilonia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.Distrito;

/**
 * Repositório de dados para a entidade {@link Distrito}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de um distrito.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface DistritoRepository extends JpaRepository<Distrito, Integer> {

  /**
   * Busca distrito cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de distritos correspondentes ao filtro.
   */
  @Query("SELECT g FROM Distrito g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT(:descricao, '%'))")
  List<Distrito> findByDescricao(@Param("descricao") String descricao);

  @Query("SELECT g FROM Distrito g LEFT JOIN FETCH g.localidades l WHERE g.id = :id ORDER BY l.descricao ASC")
  Optional<Distrito> findByIdWithLocalidades(@Param("id") Integer id);

}
