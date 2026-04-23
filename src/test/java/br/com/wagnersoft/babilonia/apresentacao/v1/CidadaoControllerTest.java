package br.com.wagnersoft.babilonia.apresentacao.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dominio.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;
import br.com.wagnersoft.babilonia.exceptions.NoDataFoundException;
import br.com.wagnersoft.babilonia.services.RemoteService;
import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
@Disabled("Feature not yet implemented")
class CidadaoControllerTest {

  @InjectMocks
  private CidadaoController cidadaoController;

  @Mock
  private RemoteService rmtService;

  @Mock
  private WSResultDTO result;

  @Test
  @SneakyThrows
  void consultarCidadao() {
    ResponseEntity<Object> resposta = cidadaoController.consultarCidadao("123");
    assertEquals(HttpStatus.OK, resposta.getStatusCode());
  }

  @Test
  @SneakyThrows
  void consultarSituacaoCidadaoNotFound() {
    when(rmtService.consultService(CidadaoConsultDTO.builder().cpf("123").build())).thenThrow(NoDataFoundException.class);
    ResponseEntity<Object> resposta = cidadaoController.consultarCidadao("123");
    assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
  }

  @Test
  @SneakyThrows
  void consultarSituacaoCidadao3() {
    when(rmtService.consultService(CidadaoConsultDTO.builder().cpf("123").build())).thenThrow(NoDataFoundException.class);
    ResponseEntity<Object> resposta = cidadaoController.consultarCidadao("123");
    assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
  }

  @Test
  @SneakyThrows
  void consultarSituacaoCidadaoBadRequest()  {
    when(rmtService.consultService(CidadaoConsultDTO.builder().cpf("123").build())).thenThrow(BabiloniaException.class);
    ResponseEntity<Object> resposta = cidadaoController.consultarCidadao("123");
    assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
  }

}
