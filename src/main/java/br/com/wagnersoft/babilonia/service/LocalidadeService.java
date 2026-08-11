package br.com.wagnersoft.babilonia.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.dto.LocResultDTO;
import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Localidade;
import br.com.wagnersoft.babilonia.repository.LocalidadeRepository;
import jakarta.transaction.Transactional;

/** 
 * Serviço responsável pelo gerenciamento e pelas regras de negócio da entidade {@link Localidade}.
 * <p>
 * Centraliza as operações de cadastro, atualização, consulta e validações 
 * de domínio das localidades geográficas no sistema.
 * </p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Service
public class LocalidadeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalidadeService.class);

  @Autowired
  private LocalidadeRepository locRep;

  @Transactional
  public LocResultDTO consultService(final Integer id, String descricao) {

    // 1. Busca da Localidade
    Optional<Localidade> locOpt = Optional.empty();
    
    if (StringUtils.isBlank(descricao)) {
      locOpt = this.locRep.findById(id);
    } else {
      List<Localidade> lista = this.locRep.findByDescricao(descricao);
    }

    // 2. SE NÃO ENCONTRAR, ESTOURE A EXCEÇÃO! (O GlobalExceptionHandler vai capturar isso e gerar o 404)
    final Localidade loc = locOpt.orElseThrow(() -> new NoDataFoundException("Localidade não localizada com os dados informados."));

    LOGGER.debug("Localidade = {}", loc);

    // 3. Montagem da resposta para o caminho feliz (Localidade existe)
    return LocResultDTO.builder()
        .descricao(loc.getDescricao())
        .tipo(loc.getTipo())
        .nivel(loc.getNivel().toString())
        .bairro(loc.getBairro())
        .subdistrito(loc.getSubdistrito())
        .distrito(loc.getDistrito().getDescricao())
        .municipio(loc.getDistrito().getMunicipio().getDescricao())
        .microregiao(loc.getDistrito().getMunicipio().getMicroregiao().getDescricao())
        .mesoregiao(loc.getDistrito().getMunicipio().getMicroregiao().getMesoregiao().getDescricao())
        .uf(loc.getDistrito().getMunicipio().getMicroregiao().getMesoregiao().getUf().getSigla())
        .latitude(Objects.toString(loc.getLatitude(), ""))
        .longitude(Objects.toString(loc.getLongitude(), ""))
        .altitude(Objects.toString(loc.getAltitude(), ""))
        .build();
  }

}
