package br.com.wagnersoft.babilonia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.Uf;

/**
 * Repositório de dados para a entidade {@link Uf}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de uma unidade da federação.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface UfRepository extends JpaRepository<Uf, String> {

  /**
   * Busca distrito cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de municípios correspondentes ao filtro.
   */
  @Query("SELECT u FROM Uf u WHERE LOWER(u.descricao) LIKE LOWER(CONCAT(:descricao, '%')) ORDER BY u.sigla ASC")
  List<Uf> findByDescricao(@Param("descricao") String descricao);

  @Query("SELECT u FROM Uf u LEFT JOIN FETCH u.mesoregioes m WHERE u.sigla = :sigla ORDER BY m.descricao ASC")
  Optional<Uf> findBySiglaWithMesoRegiao(@Param("sigla") String sigla);
  
}
