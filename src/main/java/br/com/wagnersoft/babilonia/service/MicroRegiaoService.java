package br.com.wagnersoft.babilonia.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Autowired
  private MicroRegiaoRepository rep;

  public Optional<MicroRegiao> consultById(final Integer id) {

    if (id == null) {
      Optional.empty();
    }

    Optional<MicroRegiao> mesoOpt = this.rep.findByIdWithMunicipios(id);

    mesoOpt.ifPresent(meso -> LOGGER.debug("{}", meso));

    return mesoOpt;

  }

}
