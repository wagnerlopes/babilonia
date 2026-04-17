package br.com.wagnersoft.babilonia.apresentacao.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.SneakyThrows;

class CidadaoControllerTest {

  @SuppressWarnings("removal")
  @InjectMocks
  private CidadaoController cidadaoController;

  @Mock
  private RemoteService rmtService;

  @Mock
  private WSResultDTO result;

  @Test
  @SneakyThrows
  void consultarSituacaoCidadao() {
    @SuppressWarnings("removal")
    ResponseEntity<Object> resposta = cidadaoController.consultarSituacaoCidadao("123", 0L);
    assertEquals(HttpStatus.OK, resposta.getStatusCode());
  }

  @Test
  @SneakyThrows
  void consultarSituacaoCidadaoNotFound() {
    when(rmtService.consultService(CidadaoConsultDTO.builder().cpf("123").build())).thenThrow(NoDataFoundException.class);
    @SuppressWarnings("removal")
    ResponseEntity<Object> resposta = cidadaoController.consultarSituacaoCidadao("123", 0L);
    assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
  }

  @Test
  @SneakyThrows
  void consultarSituacaoCidadao3() {
    when(rmtService.consultService(CidadaoConsultDTO.builder().cpf("123").build())).thenThrow(NoDataFoundException.class);
    @SuppressWarnings("removal")
    ResponseEntity<Object> resposta = cidadaoController.consultarSituacaoCidadao("123", 0L);
    assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
  }

  @Test
  @SneakyThrows
  void consultarSituacaoCidadaoBadRequest()  {
    when(rmtService.consultService(CidadaoConsultDTO.builder().cpf("123").build())).thenThrow(ConectagovException.class);
    @SuppressWarnings("removal")
    ResponseEntity<Object> resposta = cidadaoController.consultarSituacaoCidadao("123", 0L);
    assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
  }

}
