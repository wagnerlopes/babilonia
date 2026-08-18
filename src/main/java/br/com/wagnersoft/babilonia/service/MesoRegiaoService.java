package br.com.wagnersoft.babilonia.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.repository.MesoRegiaoRepository;
import jakarta.transaction.Transactional;

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
public class MesoRegiaoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MesoRegiaoService.class);

  @Autowired
  private MesoRegiaoRepository rep;

  @Transactional
  public Optional<MesoRegiao> consultById(final Integer id) {

    if (id == null) {
      Optional.empty();
    }

    Optional<MesoRegiao> mesoOpt = this.rep.findByIdWithMicroregioes(id);

    mesoOpt.ifPresent(meso -> LOGGER.debug("{}", meso));

    return mesoOpt;

  }

}
