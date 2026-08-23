package br.com.wagnersoft.babilonia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wagnersoft.babilonia.model.Municipio;

/**
 * Repositório de dados para a entidade {@link Municipio}.
 * <p>
 * Provê operações de acesso ao banco de dados e consultas customizadas
 * para gerenciamento de um município.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

  /**
   * Busca distrito cujo nome inicie com o termo informado (case-insensitive).
   *
   * @param descricao O prefixo ou termo para filtragem (ex: "Mar" busca "maranhao", "Maranhão", etc.).
   * @return Lista de municípios correspondentes ao filtro.
   */
  @Query("SELECT g FROM Municipio g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT(:descricao, '%')) ORDER BY g.descricao ASC")
  List<Municipio> findByDescricao(@Param("descricao") String descricao);

  @Query("SELECT m FROM Municipio m LEFT JOIN FETCH m.distritos d WHERE m.codigo = :id ORDER BY d.descricao ASC")
  Optional<Municipio> findByIdWithDistritos(@Param("id") Integer id);
  
}
