package br.com.wagnersoft.babilonia.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Localidade;
import br.com.wagnersoft.babilonia.model.dto.LocalidadeDTO;
import br.com.wagnersoft.babilonia.repository.LocalidadeRepository;

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
@Transactional(readOnly = true)
public class LocalidadeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalidadeService.class);

  private final LocalidadeRepository rep;

  /**
   *  Injeção automática do repositório via construtor.
   *  
   * @param rep {@link LocalidadeRepository}
   */
  public LocalidadeService(LocalidadeRepository rep) {
    this.rep = rep;
  }  

  /** 
   * Realiza a consulta de {@link Localidade} por ID.
   * 
   * @param id ID da localidade
   * @return Informações do {@link LocalidadeDTO localidade}
   */
  public LocalidadeDTO consultById(final Integer id) {

    if (id == null) {
      throw new IllegalArgumentException("O ID da localidade não pode ser nulo.");
    }

    Localidade loc = this.rep.findById(id)
        .orElseThrow(() -> new NoDataFoundException("Localidade não localizada com os dados informados."));

    LOGGER.debug("Localidade = {}", loc);

    return LocalidadeDTO.builder()
        .id(loc.getId())
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

  /** 
   * Realiza a consulta de {@link Localidade} pela descrição.
   * 
   * @param descricao nome da localidade
   * @return Informações da {@link LocalidadeDTO localidade}
   */
  public List<LocalidadeDTO> consultByDescricao(String descricao) {

    if (StringUtils.isBlank(descricao)) {
      return Collections.emptyList();
    }

    List<Localidade> lista = this.rep.findByDescricao(descricao);

    if (lista.isEmpty()) {
      throw new NoDataFoundException("Localidade não localizada com os dados informados.");
    }

    LOGGER.debug("Localidade = {}", lista);

    final List<LocalidadeDTO> result = new ArrayList<>(lista.size());

    for (Localidade loc : lista) {
      result.add(LocalidadeDTO.builder()
          .id(loc.getId())
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

  /**
   * Calcula a distância entre duas localidades
   * 
   * @param localId ID da origem
   * @param remoteid Id do destino
   * @return distância em Km
   */
  public double distancia(final Integer localId, final Integer remoteid) {

    Localidade origem = rep.findById(localId).orElseThrow(() -> new NoDataFoundException("Origem não encontrada"));

    Localidade destino = rep.findById(remoteid).orElseThrow(() -> new NoDataFoundException("Destino não encontrado"));

    return origem.getCoordenada().distancia(destino.getCoordenada());

  }

}
