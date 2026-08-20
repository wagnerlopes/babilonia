package br.com.wagnersoft.babilonia.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Autowired
  private UfRepository ufRep;

  public Optional<Uf> consultBySigla(String sigla) {

    if (sigla == null || sigla.isBlank()) {
      return Optional.empty();
    }

    Optional<Uf> ufOpt = this.ufRep.findBySiglaWithMesoRegiao(sigla);

    ufOpt.ifPresent(uf -> LOGGER.debug("{}", uf));

    return ufOpt;
  }

}
