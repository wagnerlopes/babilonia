package br.com.wagnersoft.babilonia.service;

import java.util.ArrayList;
import java.util.Collections;
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
  public LocResultDTO consultById(final Integer id) {

    // 1. Busca da Localidade
    if (id == null) {
      return LocResultDTO.empty();
    }

    Optional<Localidade> locOpt = this.locRep.findById(id);

    // 2. SE NÃO ENCONTRAR, ESTOURE A EXCEÇÃO! (O GlobalExceptionHandler vai capturar isso e gerar o 404)
    final Localidade loc = locOpt.orElseThrow(() -> new NoDataFoundException("Localidade não localizada com os dados informados."));

    LOGGER.debug("Localidade = {}", loc);

    // 3. Montagem da resposta para o caminho feliz (Localidade existe)
    return LocResultDTO.builder()
        .descricao(loc.getDescricao())
        .tipo(loc.getTipo())
        .nivel(loc.getNivel().toString())
        .categoria(loc.getCategoria().getDescricao().getDescricao())
        .bairro(loc.getBairro())
        .subdistrito(loc.getSubdistrito())
        .distrito(loc.getDistrito().getDescricao())
        .municipio(loc.getDistrito().getMunicipio().getDescricao())
        .microregiao(loc.getDistrito().getMunicipio().getMicroregiao().getDescricao())
        .mesoregiao(loc.getDistrito().getMunicipio().getMicroregiao().getMesoregiao().getDescricao())
        .uf(loc.getDistrito().getMunicipio().getMicroregiao().getMesoregiao().getUf().getSigla())
        .latitude(Objects.toString(loc.getCoordenada().getLatitude(), ""))
        .longitude(Objects.toString(loc.getCoordenada().getLongitude(), ""))
        .altitude(Objects.toString(loc.getCoordenada().getAltitude(), ""))
        .build();
  }

  @Transactional
  public List<LocResultDTO> consultByDescricao(String descricao) {

    // 1. Busca da Localidade
    if (StringUtils.isBlank(descricao)) {
      return Collections.emptyList();
    }

    List<Localidade> lista = this.locRep.findByDescricao(descricao);

    // 2. SE NÃO ENCONTRAR, ESTOURE A EXCEÇÃO! (O GlobalExceptionHandler vai capturar isso e gerar o 404)
    if (lista.isEmpty()) {
      throw new NoDataFoundException("Localidade não localizada com os dados informados.");
    }

    LOGGER.debug("Localidade = {}", lista);

    // 3. Montagem da resposta para o caminho feliz (Localidade existe)
    final List<LocResultDTO> result = new ArrayList<>(lista.size());

    for (Localidade loc : lista) {
      result.add(LocResultDTO.builder()
          .descricao(loc.getDescricao())
          .tipo(loc.getTipo())
          .nivel(loc.getNivel().toString())
          .categoria(loc.getCategoria().getDescricao().getDescricao())
          .bairro(loc.getBairro())
          .subdistrito(loc.getSubdistrito())
          .distrito(loc.getDistrito().getDescricao())
          .municipio(loc.getDistrito().getMunicipio().getDescricao())
          .microregiao(loc.getDistrito().getMunicipio().getMicroregiao().getDescricao())
          .mesoregiao(loc.getDistrito().getMunicipio().getMicroregiao().getMesoregiao().getDescricao())
          .uf(loc.getDistrito().getMunicipio().getMicroregiao().getMesoregiao().getUf().getSigla())
          .latitude(Objects.toString(loc.getCoordenada().getLatitude(), ""))
          .longitude(Objects.toString(loc.getCoordenada().getLongitude(), ""))
          .altitude(Objects.toString(loc.getCoordenada().getAltitude(), ""))
          .build());
    }
    return result;
  }

  public double distancia(final Integer localId, final Integer remoteid) {
    
    Localidade origem = locRep.findById(localId).orElseThrow(() -> new NoDataFoundException("Origem não encontrada"));

    Localidade destino = locRep.findById(remoteid).orElseThrow(() -> new NoDataFoundException("Destino não encontrado"));
    
    return origem.getCoordenada().distancia(destino.getCoordenada());
    
  }

}
