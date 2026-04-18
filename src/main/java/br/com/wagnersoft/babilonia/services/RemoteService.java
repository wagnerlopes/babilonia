package br.com.wagnersoft.babilonia.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
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
import jakarta.xml.bind.DatatypeConverter;

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
    
    // Busca do certificado
    final Optional<CidDoc> docOpt = this.certRep.findDoc(cidadao.getRa());
    String certData = docOpt.map(doc -> DateHelper.asFormatDate(doc.getId().getData(),"yyyyMMdd")).orElse(dtNasc.plusYears(18).getYear() + "0101");
    String valData = docOpt.map(doc -> DateHelper.asFormatDate(doc.getValidadeData(),"yyyyMMdd")).orElse(dtNasc.plusYears(18).getYear() + "1231");

    //Retornando NI quando nao tem nenhum
    final DocEnum docEnum = docOpt.map(doc -> DocEnum.fromCodigo(doc.getTipo())).orElse(DocEnum.NI);

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
    
    final SituacaoEnum situacao = this.situacaoHelper.verificaSituacao(cidadao);
    String divergencias = this.situacaoHelper.avaliaDivergencias(result, consult);
    result.setSituacaoCodigo(situacao.getCodigo());
    result.setSituacaoDescricao(situacao.getDescricao() + divergencias);

    return result;
  }
  
  /** Recupera a imagem do certificado.
   * @param cpf
   * @return
   * @throws ConectagovException
   */
  public String consultDoc(final String cpf) throws BabiloniaException {
    Optional<CidDoc> optional = Optional.empty();
    if (!cpf.equals("")) {
      optional = this.certRep.findDoc(cpf);
    }
    final CidDoc doc = optional.orElse(new CidDoc(id));
    final DocEnum docEnum = DocEnum.fromCodigo(doc.getTipo());
    if (docEnum == DocEnum.IDT || docEnum == DocEnum.NI) {
      return DocEnum.fromCodigo(doc.getTipo()).getDescricao();
    }
    
    if (doclog.getDocumento() == null || doclog.getDocumento().length == 0) {   
      return "Não há certificado.";
    }

    try {
      File file = new File("tempFile");
      FileUtils.writeByteArrayToFile(file, doclog.getDocumento());
      PDDocument docPdf = Loader.loadPDF(file);
      final PDFRenderer pdfRenderer = new PDFRenderer(docPdf);
      final BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
      try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        ImageIO.write(bim, "png", bos);
        String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(bos.toByteArray());
        LOGGER.debug("{}", imagem);
        return imagem;
      }
    } catch (IOException e) {
      throw new ConectagovException("Imagem não está disponível.", e);
    }
  }

  public List<WSResultDTO> consultService(final String[] listaCpf) throws BabiloniaException {
    return this.consultService(listaCpf, 0);
  }

  private Long testaRa(String ra) {
    return StringUtils.isNotEmpty(ra) && ra.matches("-?\\d+(\\.\\d+)?") ? Long.valueOf(ra) : 0;
  }
  
} 
