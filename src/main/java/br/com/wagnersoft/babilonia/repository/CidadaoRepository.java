package br.mil.eb.wagnersoft.babilonia.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.mil.eb.wagnersoft.babilonia.dominio.Cidadao;

@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {

  @Query("SELECT c FROM Cidadao c JOIN c.evento e JOIN c.municipioNascimento m WHERE c.cpf = :cpf")
  Optional<Cidadao> findByCpf(@Param("cpf") String cpf);

  @Query("SELECT c FROM Cidadao c JOIN c.evento e WHERE c.nome = :nome AND c.mae = :mae AND c.nascimentoData = :nascimentoData")
  Optional<Cidadao> findByOutros(@Param("nome") String nome, @Param("mae") String mae, @Param("nascimentoData") Date nascimentoData);

}
