package br.com.wagnersoft.babilonia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.MesoRegiao;

/**
 * Repositório de dados para a entidade {@link MesoRegiao mesorregião}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de uma mesorregião.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface MesoRegiaoRepository extends JpaRepository<MesoRegiao, Integer> {

  /**
   * Busca distrito cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de mesoregiões correspondentes ao filtro.
   */
  @Query("SELECT g FROM MesoRegiao g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT(:descricao, '%'))")
  List<MesoRegiao> findByDescricao(@Param("descricao") String descricao);

  @Query("SELECT m FROM MesoRegiao m LEFT JOIN FETCH m.microregioes WHERE m.id = :id")
  Optional<MesoRegiao> findByIdWithMicroregioes(@Param("id") Integer id);

}
