package br.com.wagnersoft.babilonia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.Microregiao;

/**
 * Repositório de dados para a entidade {@link Microregiao}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de uma microregião.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface MicroregiaoRepository extends JpaRepository<Microregiao, Integer> {

  /**
   * Busca distrito cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de microregiões correspondentes ao filtro.
   */
  @Query("SELECT g FROM Microregiao g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT(:descricao, '%'))")
  List<Microregiao> findByDescricao(@Param("descricao") String descricao);

}
