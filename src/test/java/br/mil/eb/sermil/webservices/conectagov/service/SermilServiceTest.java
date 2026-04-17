package br.mil.eb.sermil.webservices.conectagov.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.mil.eb.sermil.webservices.conectagov.dominio.CidDocMilitar;
import br.mil.eb.sermil.webservices.conectagov.dominio.Cidadao;
import br.mil.eb.sermil.webservices.conectagov.dominio.Evento;
import br.mil.eb.sermil.webservices.conectagov.dominio.Municipio;
import br.mil.eb.sermil.webservices.conectagov.dominio.dto.CidadaoConsultDTO;
import br.mil.eb.sermil.webservices.conectagov.dominio.dto.WSResultDTO;
import br.mil.eb.sermil.webservices.conectagov.dominio.enums.SituacaoEnum;
import br.mil.eb.sermil.webservices.conectagov.dominio.id.CidDocMilitarId;
import br.mil.eb.sermil.webservices.conectagov.repository.CertificadoRepository;
import br.mil.eb.sermil.webservices.conectagov.repository.CidadaoRepository;
import br.mil.eb.sermil.webservices.conectagov.services.SermilService;
import br.mil.eb.sermil.webservices.conectagov.services.SermilSituacaoHelper;
import io.jsonwebtoken.lang.Strings;

@TestInstance(Lifecycle.PER_CLASS)
class SermilServiceTest {

  @InjectMocks
  private SermilService sermilSvc;

  @Mock
  private CidadaoRepository cidadaoRep;

  @Mock
  private CertificadoRepository certRep;
  
  @Mock
  private Cidadao cidadao;

  @Mock
  private CidDocMilitar doc;

  @Mock
  private Evento evento;

  @Mock
  private SermilSituacaoHelper situacaoHelper;

  private AutoCloseable closeable;

  @BeforeAll
  public void initMocks() {
    this.closeable = MockitoAnnotations.openMocks(this);
  }

  @AfterAll
  public void releaseMocks() throws Exception {
    this.closeable.close();
  }
  
  @BeforeEach
  public void setUp() {
    when(cidadaoRep.findByRa(anyLong())).thenReturn(Optional.ofNullable(cidadao));
    when(cidadaoRep.findByCpf(anyString())).thenReturn(Optional.ofNullable(cidadao));
    when(cidadaoRep.findByOutros(anyString(), anyString(), any())).thenReturn(Optional.ofNullable(cidadao));
    when(certRep.findDoc(anyLong())).thenReturn(Optional.ofNullable(doc));
    when(cidadao.getCpf()).thenReturn("123");
    when(cidadao.getRa()).thenReturn(6L);
    when(cidadao.getNome()).thenReturn("Nome");
    when(cidadao.getMae()).thenReturn("Mae");
    when(cidadao.getNascimentoData()).thenReturn(new Date());
    when(cidadao.getMunicipioNascimento()).thenReturn(new Municipio(1, "N/D", "KK"));
    when(cidadao.getAuditData()).thenReturn(new Date());
    when(cidadao.getEvento()).thenReturn(evento);
    when(doc.getAuditData()).thenReturn(new Date());
    when(doc.getTipo()).thenReturn(1);
    when(doc.getId()).thenReturn(new CidDocMilitarId(1L, new Date()));
  }

  @Test
  void consultServiceFound() throws Exception {
    when(situacaoHelper.verificaSituacao(any())).thenReturn(SituacaoEnum.EM_DIA);
    final WSResultDTO result = sermilSvc.consultService(CidadaoConsultDTO.builder().cpf("123").build());
    assertNotNull(result);
  }

  @Test
  void consultServiceNotFound() throws Exception {
    when(cidadaoRep.findByCpf(anyString())).thenReturn(Optional.ofNullable(null));
    when(situacaoHelper.verificaSituacao(any())).thenReturn(SituacaoEnum.NAO_ENCONTRADO);
    final WSResultDTO result = sermilSvc.consultService(CidadaoConsultDTO.builder().cpf("123").build());
    assertEquals(3, result.getSituacaoCodigo());
  }

  @Test
  void consultServiceListCpf() throws Exception {
    when(situacaoHelper.verificaSituacao(any())).thenReturn(SituacaoEnum.EM_DIA);
    final List<WSResultDTO> result = sermilSvc.consultService(Strings.commaDelimitedListToStringArray("07484463701,07708054770,07736419710"));
    assertNotNull(result);
  }
  
}
