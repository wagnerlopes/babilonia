package br.mil.eb.sermil.webservices.conectagov.services;

import br.mil.eb.sermil.webservices.conectagov.dominio.Cidadao;
import br.mil.eb.sermil.webservices.conectagov.dominio.Evento;
import br.mil.eb.sermil.webservices.conectagov.dominio.enums.EventoEnum;
import br.mil.eb.sermil.webservices.conectagov.dominio.enums.SituacaoEnum;
import br.mil.eb.sermil.webservices.conectagov.exceptions.NoDataFoundException;
import br.mil.eb.sermil.webservices.conectagov.repository.CidEventoLogRepository;
import br.mil.eb.sermil.webservices.conectagov.repository.CidExarRepository;
import br.mil.eb.sermil.webservices.conectagov.repository.CidMilitarRepository;
import br.mil.eb.sermil.webservices.conectagov.utils.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static br.mil.eb.sermil.webservices.conectagov.dominio.enums.EventoEnum.*;
import static br.mil.eb.sermil.webservices.conectagov.dominio.enums.SituacaoEnum.EM_DEBITO;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unused")
class SermilSituacaoHelperTest {

    public static final DateTimeFormatter FORMATTER = ofPattern("dd/MM/yyyy");
    private final CidEventoLogRepository cidEventoRep = mock(CidEventoLogRepository.class);
    private final CidExarRepository cidExarRep = mock(CidExarRepository.class);
    private final CidMilitarRepository cidMilitarRep = mock(CidMilitarRepository.class);
    private final SermilSituacaoHelper sermilSituacaoHelper = new SermilSituacaoHelper(
        true, cidEventoRep, cidExarRep, cidMilitarRep
    );
//4,6,27,42,47,55,56,58
    @Test
    void test_eventos_em_debito() throws NoDataFoundException {
        Cidadao cidadao = new Cidadao();
        Evento evento = new Evento();
        cidadao.setNascimentoData(DateHelper.asDate(LocalDate.now().minusYears(23)));

        List<EventoEnum> eventos = Arrays.asList(DESAPARECIMENTO, DESERTOR);
        for (EventoEnum eventoEnum : eventos){
            evento.setId(eventoEnum.getCodigo());
            cidadao.setEvento(evento);
            SituacaoEnum situacao = sermilSituacaoHelper.verificaSituacao(cidadao);
            Assertions.assertEquals(EM_DEBITO, situacao);
        }

    }

    @Test
    void test_eventos_nao_em_debito() throws NoDataFoundException {
        Cidadao cidadao = new Cidadao();
        Evento evento = new Evento();
        cidadao.setNascimentoData(DateHelper.asDate(LocalDate.now().minusYears(23)));

        List<EventoEnum> eventos = Arrays.asList(RESERVISTA, RESERVA_1CLASSE, RESERVA_2CLASSE);
        for (EventoEnum eventoEnum : eventos){
            evento.setId(eventoEnum.getCodigo());
            cidadao.setEvento(evento);
            SituacaoEnum situacao = sermilSituacaoHelper.verificaSituacao(cidadao);
            Assertions.assertNotEquals(EM_DEBITO, situacao);
        }
    }
/*
    @Test
    void test_nao_em_debito(){

        List<LocalDate> licenciamentos = Arrays.asList(
            LocalDate.parse("01/12/2021", FORMATTER),
            LocalDate.parse("31/12/2021", FORMATTER),
            LocalDate.parse("12/01/2022", FORMATTER),
            LocalDate.parse("01/06/2022", FORMATTER),
            LocalDate.parse("30/11/2022", FORMATTER)
        );
        for (LocalDate licenciamentoData : licenciamentos) {

            ok(licenciamentoData, "30/11/2022", 0);
            ok(licenciamentoData, "01/12/2022", 0);
            ok(licenciamentoData, "31/01/2023", 0);
            emDebito(licenciamentoData, "01/02/2023", 0);
            emDebito(licenciamentoData, "30/11/2023", 0);
            emDebito(licenciamentoData, "01/12/2023", 0);

            ok(licenciamentoData, "30/11/2023", 1);
            ok(licenciamentoData, "01/12/2023", 1);
            ok(licenciamentoData, "31/01/2024", 1);
            emDebito(licenciamentoData, "01/02/2024", 1);
            emDebito(licenciamentoData, "31/11/2024", 1);

            ok(licenciamentoData, "30/11/2024", 2);
            ok(licenciamentoData, "01/12/2024", 2);
            ok(licenciamentoData, "31/01/2025", 2);
            emDebito(licenciamentoData, "01/02/2025", 2);
            emDebito(licenciamentoData, "31/11/2025", 2);

            ok(licenciamentoData, "30/11/2025", 3);
            ok(licenciamentoData, "01/12/2025", 3);
            ok(licenciamentoData, "31/01/2026", 3);
            emDebito(licenciamentoData, "01/02/2026", 3);
            emDebito(licenciamentoData, "31/11/2026", 3);

            ok(licenciamentoData, "30/11/2026", 4);
            ok(licenciamentoData, "01/12/2026", 4);
            ok(licenciamentoData, "31/01/2027", 4);
            emDebito(licenciamentoData, "01/02/2027", 4);
            emDebito(licenciamentoData, "31/11/2027", 4);

            ok(licenciamentoData, "30/11/2027", 5);
            ok(licenciamentoData, "01/12/2027", 5);
            ok(licenciamentoData, "31/01/2028", 5);
            ok(licenciamentoData, "01/02/2028", 5);
            ok(licenciamentoData, "31/11/2028", 5);

            emDebito(licenciamentoData, "31/11/2030", 2);
            ok(licenciamentoData, "30/11/2026", 4);
            ok(licenciamentoData, "31/01/2027", 4);
            emDebito(licenciamentoData, "01/02/2027", 4);
            ok(licenciamentoData, "01/02/2027", 5);
        }
    }

    @Test
    void test_situacao_especial(){
        LocalDate licenciamentoData = LocalDate.parse("15/12/2021", FORMATTER);
        ok(licenciamentoData, "10/04/2026", 4);
        ok(licenciamentoData, "10/12/2026", 4);
        ok(licenciamentoData, "01/01/2027", 4);
        ok(licenciamentoData, "31/01/2027", 4);
        emDebito(licenciamentoData, "01/02/2027", 4);
        ok(licenciamentoData, "01/02/2027", 5);
        ok(licenciamentoData, "31/01/2028", 5);
        ok(licenciamentoData, "31/01/2030", 5);

        licenciamentoData = LocalDate.parse("03/10/2016", FORMATTER);
        emDebito(licenciamentoData, "20/10/2022", 0);

    }

    @Test
    void teste_nao_em_debito_3(){
        LocalDate lic = LocalDate.parse("30/11/1997", FORMATTER);
        ok(lic, "19/10/2022", 5);
    }

    @Test
    void test_nao_em_debito_2(){

        List<LocalDate> licenciamentos = Arrays.asList(
            LocalDate.parse("01/12/2022", FORMATTER),
            LocalDate.parse("23/06/2023", FORMATTER),
            LocalDate.parse("31/11/2023", FORMATTER)
        );
        for (LocalDate licenciamentoData : licenciamentos) {

            ok(licenciamentoData, "10/12/2022", 0);
            ok(licenciamentoData, "31/12/2022", 0);
            ok(licenciamentoData, "31/01/2023", 0);
            ok(licenciamentoData, "01/02/2023", 0);
            ok(licenciamentoData, "30/11/2023", 0);
            ok(licenciamentoData, "01/12/2023", 0);

            ok(licenciamentoData, "30/11/2023", 0);
            ok(licenciamentoData, "01/12/2023", 0);
            ok(licenciamentoData, "31/01/2024", 0);
            emDebito(licenciamentoData, "01/02/2024", 0);
            emDebito(licenciamentoData, "31/11/2024", 0);

            ok(licenciamentoData, "30/11/2024", 1);
            ok(licenciamentoData, "01/12/2024", 1);
            ok(licenciamentoData, "31/01/2025", 1);
            emDebito(licenciamentoData, "01/02/2025", 1);
            emDebito(licenciamentoData, "31/11/2025", 1);

            emDebito(licenciamentoData, "31/11/2030", 1);
            ok(licenciamentoData, "30/11/2026", 3);
            ok(licenciamentoData, "31/01/2027", 3);
            emDebito(licenciamentoData, "01/02/2027", 3);
        }

    }

    private void emDebito(LocalDate licenciamentoData, String dataReferencia, int apresentacoesRealizadas) {
        LocalDate referencia = LocalDate.parse(dataReferencia, FORMATTER);
        assertTrue(sermilSituacaoHelper.isEmDebitoExar(licenciamentoData, apresentacoesRealizadas, referencia));
    }

    private void ok(LocalDate licenciamentoData, String dataReferencia, int apresentacoesRealizadas) {
        LocalDate referencia = LocalDate.parse(dataReferencia, FORMATTER);
        assertFalse(sermilSituacaoHelper.isEmDebitoExar(licenciamentoData, apresentacoesRealizadas, referencia));
    }
*/
}