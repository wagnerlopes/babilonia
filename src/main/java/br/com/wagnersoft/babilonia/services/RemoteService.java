package br.com.wagnersoft.babilonia.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wagnersoft.babilonia.dominio.Cidadao;
import br.com.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dominio.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;
import br.com.wagnersoft.babilonia.repository.CidadaoRepository;
import br.com.wagnersoft.babilonia.utils.DateHelper;

/** Serviço de pesquisa de cidadão.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
@Service
public class RemoteService implements BabiloniaService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RemoteService.class);
  
  @Autowired
  private CidadaoRepository cidadaoRep;

  @Override
  public WSResultDTO consultService(final CidadaoConsultDTO consult) throws BabiloniaException {
    
    // Busca do cidadao
    Optional<Cidadao> cidOpt = Optional.empty();
    if (StringUtils.isNotBlank(consult.getCpf())) {
      cidOpt = this.cidadaoRep.findByCpf(consult.getCpf());
    } else if (StringUtils.isNotBlank(consult.getNome())) {
      cidOpt = this.cidadaoRep.findByNome(id);
    } else {
      if (!StringUtils.isAnyBlank(consult.getNome(), consult.getNomeMae()) && consult.getDataNascimento() != null) {
        cidOpt = this.cidadaoRep.findByOutros(consult.getNome(), consult.getNomeMae(), DateHelper.asDate(consult.getDataNascimento()));
      }
    }
    
    final Cidadao cidadao = cidOpt.orElse(Cidadao.naoCadastrado(consult.getCpf(), id));
    LOGGER.debug("Cidadao = {}", cidadao);
    
    WSResultDTO result = null;
    final LocalDate dtNasc = DateHelper.asLocalDate(cidadao.getNascimentoData());
    
    // Montagem da resposta
    result = WSResultDTO.builder()
        .cpf(cidadao.getCpf())
        .nome(cidadao.getNome())
        .mae(cidadao.getMae())
        .pai(cidadao.getPai())
        .nascimentoData(DateHelper.asLocalDate(cidadao.getNascimentoData()))
        .certificadoSigla(docEnum.getSigla())
        .certificadoDescricao(docEnum.getDescricao())
        .certificadoData(certData)
        .validadeData(valData)
        .nascimentoLocal(cidadao.getMunicipioNascimento().toString())
        .atualizacaoData(DateHelper.asLocalDate(cidadao.getAuditData()))
        .consultaData(LocalDate.now())
        .build();
    
    return result;
  }

  public List<WSResultDTO> consultService(final String[] listaCpf) throws BabiloniaException {
    return this.consultService(listaCpf, 0);
  }

  private Long testNumber(String param) {
    return StringUtils.isNotEmpty(param) && ra.matches("-?\\d+(\\.\\d+)?") ? Long.valueOf(param) : 0;
  }
  
} 
