package br.com.wagnersoft.babilonia.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;

/** 
 * Serviço responsável pelo gerenciamento e pelas regras de negócio da entidade {@link Municipio município}.
 * <p>
 * Centraliza as operações de cadastro, atualização, consulta e validações 
 * de domínio de município no sistema.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class MunicipioService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioService.class);

  public record DistritoDTO(Integer id, String descricao) { };

  public record MunicipioDTO(Integer id, String descricao, List<DistritoDTO> distritos) { };

  private final MunicipioRepository rep;

  /**
   *  Injeção automática do repositório via construtor.
   *  
   * @param rep {@link MunicipioRepository}
   */
  public MunicipioService(MunicipioRepository rep) {
    this.rep = rep;
  }  
  
  /** 
   * Realiza a consulta de {@link Municipio} por ID.
   * 
   * @param id ID do município
   * @return Informações do {@link MunicipioDTO município}
   */
  public MunicipioDTO consultById(final Integer id) {

    if (id == null) {
      throw new IllegalArgumentException("O ID do município não pode ser nulo.");
    }

    Municipio municipio = this.rep.findByIdWithDistritos(id)
        .orElseThrow(() -> new NoDataFoundException("Município não localizado com os dados informados."));

    LOGGER.debug("Município localizado: {}", municipio);

    List<DistritoDTO> distritos = municipio.getDistritos().stream()
        .map(m -> new DistritoDTO(m.getId(), m.getDescricao()))
        .toList();
    
    return new MunicipioDTO(municipio.getCodigo(), municipio.getDescricao(), distritos);
  }

}
