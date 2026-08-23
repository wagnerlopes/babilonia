package br.com.wagnersoft.babilonia.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;

/** 
 * Serviço responsável pelo gerenciamento e pelas regras de negócio da entidade {@link MesoRegiao mesorregião}.
 * <p>
 * Centraliza as operações de cadastro, atualização, consulta e validações 
 * de domínio de mesorregião no sistema.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class MesoRegiaoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MesoRegiaoService.class);

  public record MicroRegiaoDTO(Integer id, String descricao) { };

  public record MesoRegiaoDTO(Integer id, String descricao, List<MicroRegiaoDTO> microrregiao) { };

  private final MesoRegiaoRepository rep;

  /**
   *  Injeção automática do repositório via construtor.
   *  
   * @param rep {@link MesoRegiaoRepository}
   */
  public MesoRegiaoService(MesoRegiaoRepository rep) {
    this.rep = rep;
  }  

  /**
   *  Realiza a consulta de {@link MesoRegiao} por ID.
   * 
   * @param id ID do mesorregião
   * @return Informações do {@link MesoRegiaoDTO mesorregião}
   */
  public MesoRegiaoDTO consultById(final Integer id) {

    if (id == null) {
      throw new IllegalArgumentException("O ID da mesorregião não pode ser nulo.");
    }

    MesoRegiao mesorregiao = this.rep.findByIdWithMicroregioes(id)
        .orElseThrow(() -> new NoDataFoundException("Mesorregião não localizada com os dados informados."));

    LOGGER.debug("Mesorregião localizada: {}", mesorregiao);

    List<MicroRegiaoDTO> microrregioes = mesorregiao.getMicroregioes().stream()
        .map(m -> new MicroRegiaoDTO(m.getId(), m.getDescricao())).toList();

    return new MesoRegiaoDTO(mesorregiao.getId(), mesorregiao.getDescricao(), microrregioes);
  }

}
