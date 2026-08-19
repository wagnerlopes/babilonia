package br.com.wagnersoft.babilonia.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.repository.DistritoRepository;
import jakarta.transaction.Transactional;

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
public class DistritoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistritoService.class);

  @Autowired
  private DistritoRepository rep;

  @Transactional
  public Optional<Distrito> consultById(final Integer id) {

    if (id == null) {
      Optional.empty();
    }

    Optional<Distrito> disOpt = this.rep.findByIdWithLocalidades(id);

    disOpt.ifPresent(meso -> LOGGER.debug("{}", meso));

    return disOpt;

  }

}
