package br.com.wagnersoft.babilonia.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.repository.MunicipioRepository;
import jakarta.transaction.Transactional;

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
public class MunicipioService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioService.class);

  @Autowired
  private MunicipioRepository rep;

  @Transactional
  public Optional<Municipio> consultById(final Integer id) {

    if (id == null) {
      Optional.empty();
    }

    Optional<Municipio> munOpt = this.rep.findByIdWithDistritos(id);

    munOpt.ifPresent(meso -> LOGGER.debug("{}", meso));

    return munOpt;

  }

}
