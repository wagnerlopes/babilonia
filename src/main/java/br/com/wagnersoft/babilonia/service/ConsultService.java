package br.com.wagnersoft.babilonia.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exception.BabiloniaException;
import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.Cidadao;
import br.com.wagnersoft.babilonia.model.SituacaoEnum;
import br.com.wagnersoft.babilonia.repository.CidadaoRepository;

/** Consult service.
 * @author Wagner Lopes
 * @since 1.0.0
 * @version 1.0.0
 */
@Service
public class ConsultService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsultService.class);

  @Autowired
  private CidadaoRepository cidadaoRep;

  public WSResultDTO consultService(final CidadaoConsultDTO consult) {

    // 1. Busca do cidadão
    Optional<Cidadao> cidOpt = Optional.empty();
    if (StringUtils.isNotBlank(consult.getCpf())) {
      cidOpt = this.cidadaoRep.findByCpf(consult.getCpf());
    } else if (!StringUtils.isAnyBlank(consult.getNome(), consult.getNomeMae()) && consult.getDataNascimento() != null) {
      cidOpt = this.cidadaoRep.findByOutros(consult.getNome(), consult.getNomeMae(), consult.getDataNascimento());
    }

    // 2. SE NÃO ENCONTRAR, ESTOURE A EXCEÇÃO! (O GlobalExceptionHandler vai capturar isso e gerar o 404)
    final Cidadao cidadao = cidOpt.orElseThrow(() -> new NoDataFoundException("Cidadão não localizado com os dados informados."));

    LOGGER.debug("Cidadao localizado = {}", cidadao);

    // 3. Montagem da resposta para o caminho feliz (Cidadão existe)
    return WSResultDTO.builder()
        .cpf(cidadao.getCpf())
        .nome(cidadao.getNome())
        .mae(cidadao.getMae())
        .pai(cidadao.getPai())
        .nascimentoData(cidadao.getNascimentoData())
        .nascimentoLocal(cidadao.getMunicipioNascimento() == null ? "" : cidadao.getMunicipioNascimento().toString())
        .situacaoCodigo(1) 
        .situacaoDescricao(SituacaoEnum.EM_DIA.getDescricao())
        .atualizacaoData(cidadao.getAuditData())
        .consultaData(LocalDate.now())
        .build();
  }

  public List<WSResultDTO> consultService(final String[] listaCpf) {
    if (listaCpf == null || listaCpf.length == 0) {
      throw new BabiloniaException("Lista de CPF vazia"); 
    }

    final List<WSResultDTO> lista = new ArrayList<>(listaCpf.length);

    for (String cpf : listaCpf) {
      try {
        lista.add(this.consultService(CidadaoConsultDTO.builder().cpf(cpf).build()));
      } catch (NoDataFoundException e) {
        lista.add(WSResultDTO.builder()
            .cpf(cpf)
            .consultaData(LocalDate.now())
            .situacaoCodigo(SituacaoEnum.NAO_ENCONTRADO.getCodigo())
            .situacaoDescricao(e.getMessage())
            .build());
      }
    }
    return lista;
  }

}
