package br.com.wagnersoft.babilonia.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.repository.MicroRegiaoRepository;

/** 
 * Serviço responsável pelo gerenciamento e pelas regras de negócio da entidade {@link MicroRegiao microrregião}.
 * <p>
 * Centraliza as operações de cadastro, atualização, consulta e validações 
 * de domínio de microrregião no sistema.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class MicroRegiaoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MicroRegiaoService.class);

  public record MunicipioDTO(Integer id, String descricao) { };

  public record MicroRegiaoDTO(Integer id, String descricao, List<MunicipioDTO> municipios) { };
  
  private final MicroRegiaoRepository rep;

  /**
   *  Injeção automática do repositório via construtor.
   *  
   * @param rep {@link MicroRegiaoRepository}
   */
  public MicroRegiaoService(MicroRegiaoRepository rep) {
    this.rep = rep;
  }  

  /** 
   * Realiza a consulta de {@link MicroRegiao} por ID.
   * 
   * @param id ID da microrregião
   * @return Informações da {@link MicroRegiaoDTO microrregião}
   */
  public MicroRegiaoDTO consultById(final Integer id) {

    if (id == null) {
      throw new IllegalArgumentException("O ID da microrregião não pode ser nulo.");
    }

    MicroRegiao microrregiao = this.rep.findByIdWithMunicipios(id)
        .orElseThrow(() -> new NoDataFoundException("Microrregião não localizada com os dados informados."));

    LOGGER.debug("Microrregião localizada: {}", microrregiao);

    List<MunicipioDTO> municipios = microrregiao.getMunicipios().stream()
        .map(m -> new MunicipioDTO(m.getCodigo(), m.getDescricao()))
        .toList();

    return new MicroRegiaoDTO(microrregiao.getId(), microrregiao.getDescricao(), municipios);
  }

}
