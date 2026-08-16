package br.com.wagnersoft.babilonia.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.repository.UfRepository;
import jakarta.transaction.Transactional;

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
public class UfService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UfService.class);

  @Autowired
  private UfRepository ufRep;

  @Transactional
  public Uf consultBySigla(final String sigla) {

    if (sigla == null || sigla.isBlank()) {
      return null;
    }

    Optional<Uf> ufOpt = this.ufRep.findBySigla(sigla);

    final Uf uf = ufOpt.orElseThrow(() -> new NoDataFoundException("UF não localizada com os dados informados."));

    LOGGER.debug("{}", uf);

    return uf;
  }

}
