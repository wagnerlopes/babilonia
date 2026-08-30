package br.com.wagnersoft.babilonia.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;

/** 
 * Serviço responsável pelo gerenciamento e pelas regras de negócio da entidade {@link Distrito distrito}.
 * <p>
 * Centraliza as operações de cadastro, atualização, consulta e validações 
 * de domínio de distrito no sistema.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class DistritoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistritoService.class);

  public record LocalidadeDTO(Integer id, String descricao) { };

  public record DistritoDTO(Integer id, String descricao, List<LocalidadeDTO> localidades) { };

  private final DistritoRepository rep;

  /**
   *  Injeção automática do repositório via construtor.
   *  
   * @param rep {@link DistritoRepository}
   */
  public DistritoService(DistritoRepository rep) {
    this.rep = rep;
  }  

  /** 
   * Realiza a consulta de {@link Distrito} por ID.
   * 
   * @param id ID do distrito
   * @return Informações do {@link DistritoDTO distrito}
   */
  public DistritoDTO consultById(final Integer id) {

    if (id == null) {
      throw new IllegalArgumentException("O ID do distrito não pode ser nulo.");
    }

    Distrito distrito = this.rep.findByIdWithLocalidades(id)
        .orElseThrow(() -> new NoDataFoundException("Distrito não localizado com os dados informados."));

    LOGGER.debug("Distrito localizado: {}", distrito);

    List<LocalidadeDTO> localidades = distrito.getLocalidades().stream()
        .map(m -> new LocalidadeDTO(m.getId(), m.getDescricao()))
        .toList();

    return new DistritoDTO(distrito.getId(), distrito.getDescricao(), localidades);
  }

  /** 
   * Realiza a consulta de {@link Distrito} pela descrição.
   * 
   * @param descricao nome do distrito
   * @return Informações do {@link DistritoDTO distrito}
   */
  public List<DistritoDTO> consultByDescricao(String descricao) {

    if (StringUtils.isBlank(descricao)) {
      return Collections.emptyList();
    }

    List<Distrito> lista = this.rep.findByDescricao(descricao);

    if (lista.isEmpty()) {
      throw new NoDataFoundException("Distrito não localizado com os dados informados.");
    }

    LOGGER.debug("Distritos = {}", lista);

    final List<DistritoDTO> result = new ArrayList<>(lista.size());

    for (Distrito d : lista) {
      result.add(new DistritoDTO(d.getId(), d.getDescricao(),
          d.getLocalidades().stream().map(l -> new LocalidadeDTO(l.getId(), l.getDescricao())).toList()));
    }
    return result;
  }

}
