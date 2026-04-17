package br.com.wagnersoft.babilonia.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unused")
class SituacaoHelperTest {

    public static final DateTimeFormatter FORMATTER = ofPattern("dd/MM/yyyy");
    private final SituacaoHelper situacaoHelper = new SituacaoHelper(
        true, eventoRep
    );

    @Test
    void test__debito() throws NoDataFoundException {
        Cidadao cidadao = new Cidadao();
        Evento evento = new Evento();
        cidadao.setNascimentoData(DateHelper.asDate(LocalDate.now().minusYears(23)));

        List<EventoEnum> eventos = Arrays.asList(DESAPARECIMENTO, DESERTOR);
        for (EventoEnum eventoEnum : eventos){
            evento.setId(eventoEnum.getCodigo());
            cidadao.setEvento(evento);
            SituacaoEnum situacao = situacaoHelper.verificaSituacao(cidadao);
            Assertions.assertEquals(EM_DEBITO, situacao);
        }

    }

 
}