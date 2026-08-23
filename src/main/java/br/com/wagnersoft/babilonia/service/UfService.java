package br.com.wagnersoft.babilonia.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.UfRepository;

/** 
 * Serviço responsável pelo gerenciamento e pelas regras de negócio da entidade {@link Uf}.
 * <p>
 * Centraliza as operações de cadastro, atualização, consulta e validações 
 * de domínio das unidades da federação no sistema.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class UfService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UfService.class);

  public record MesoRegiaoDTO(Integer id, String descricao) { };

  public record UfDTO(String uf, String descricao, List<MesoRegiaoDTO> mesorregiao) { };

  private final UfRepository rep;

  /**
   *  Injeção automática do repositório via construtor.
   *  
   * @param rep {@link UfRepository}
   */
  public UfService(UfRepository rep) {
    this.rep = rep;
  }  

  /**
   *  Realiza a consulta de {@link Uf} por sigla.
   * 
   * @param sigla Sigla de UF
   * @return Informações da {@link UfDTO UF}
   */
  public UfDTO consultBySigla(String sigla) {

    if (sigla == null || sigla.isBlank()) {
      throw new IllegalArgumentException("A sigla da UF não pode ser nula ou vazia.");
    }

    Uf uf = this.rep.findBySiglaWithMesoRegiao(sigla)
        .orElseThrow(() -> new NoDataFoundException("UF não localizada com os dados informados."));

    LOGGER.debug("UF localizada: {}", uf);

    List<MesoRegiaoDTO> mesorregioes = uf.getMesoregioes().stream()
        .map(m -> new MesoRegiaoDTO(m.getId(), m.getDescricao()))
        .toList();

    return new UfDTO(uf.getSigla(), uf.getDescricao(), mesorregioes);
  }

}
