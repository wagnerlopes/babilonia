package br.com.wagnersoft.babilonia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.MicroRegiao;

/**
 * Repositório de dados para a entidade {@link MicroRegiao microregião}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de uma microrregião.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface MicroRegiaoRepository extends JpaRepository<MicroRegiao, Integer> {

  /**
   * Busca distrito cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de microregiões correspondentes ao filtro.
   */
  @Query("SELECT g FROM MicroRegiao g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT(:descricao, '%')) ORDER BY g.descricao ASC")
  List<MicroRegiao> findByDescricao(@Param("descricao") String descricao);

  @Query("SELECT m FROM MicroRegiao m LEFT JOIN FETCH m.municipios u WHERE m.id = :id ORDER BY u.descricao ASC")
  Optional<MicroRegiao> findByIdWithMunicipios(@Param("id") Integer id);
  
}
