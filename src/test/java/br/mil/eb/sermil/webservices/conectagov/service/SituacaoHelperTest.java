package br.mil.eb.sermil.webservices.conectagov.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.mil.eb.sermil.webservices.conectagov.dominio.Cidadao;
import br.mil.eb.sermil.webservices.conectagov.dominio.Evento;
import br.mil.eb.sermil.webservices.conectagov.dominio.enums.SituacaoEnum;
import br.mil.eb.sermil.webservices.conectagov.services.SermilSituacaoHelper;
import br.mil.eb.sermil.webservices.conectagov.utils.DateHelper;
import lombok.SneakyThrows;

@TestInstance(Lifecycle.PER_CLASS)
class SituacaoHelperTest {

    @InjectMocks
    private SermilSituacaoHelper situacaoHelper;

    private Cidadao cidadao;
    
    private AutoCloseable closeable;

    @BeforeAll
    public void setUp() throws ParseException {
      this.closeable = MockitoAnnotations.openMocks(this);
      this.cidadao = Cidadao.builder()
          .evento(Evento.builder().id(1).build())
          .nome("Teste")
          .mae("Mae")
          .nascimentoData(DateHelper.asDate("19950101", "yyyyMMdd")).build();
    }
    
    @AfterAll
    public void releaseMocks() throws Exception {
      this.closeable.close();
    }
    
    @Test
    @SneakyThrows
    void verificaSituacao1() {
      SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
      assertEquals(SituacaoEnum.EM_DIA, situacao);
    }

    @Test
    @SneakyThrows
    void verificaSituacao2() {
      cidadao.setEvento(Evento.builder().id(4).build());
      SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
      assertEquals(SituacaoEnum.EM_DEBITO, situacao);
    }

    @Test
    void verificaSituacao16anos() throws Exception {
      LocalDate data = LocalDate.now();
      data = data.minusYears(16);
      cidadao.setNascimentoData(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
      SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
      assertNotNull(situacao);
      assertEquals(SituacaoEnum.EM_DIA, situacao);
    }
    
    @Test
    void verificaSituacao17anos() throws Exception {
    	LocalDate data = LocalDate.now();
    	data = data.minusYears(17);
      cidadao.setNascimentoData(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
      SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
      assertNotNull(situacao);
      assertEquals(SituacaoEnum.EM_DIA, situacao);
    }
    
    @Test
    void verificaSituacao45anos() throws Exception {
    	LocalDate data = LocalDate.now();
    	data = data.minusYears(45);
      cidadao.setNascimentoData(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
      cidadao.setEvento(Evento.builder().id(4).build());
      SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
      assertNotNull(situacao);
      assertEquals(SituacaoEnum.EM_DEBITO, situacao);
    }
    
    @Test
    void verificaSituacao46anos() throws Exception {
    	LocalDate data = LocalDate.now();
    	data = data.minusYears(46);
      cidadao.setNascimentoData(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
      cidadao.setEvento(Evento.builder().id(6).build());
      SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
      assertNotNull(situacao);
      assertEquals(SituacaoEnum.EM_DIA, situacao);
    }

}
