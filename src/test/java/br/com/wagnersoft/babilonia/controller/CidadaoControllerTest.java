package br.com.wagnersoft.babilonia.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.wagnersoft.babilonia.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exception.BabiloniaException;
import br.com.wagnersoft.babilonia.exception.NoDataFoundException;
import br.com.wagnersoft.babilonia.model.TipoSituacao;
import br.com.wagnersoft.babilonia.service.ConsultService;

@WebMvcTest(CidadaoController.class)
class CidadaoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @MockitoBean
  private ConsultService rmtSvc;

  @MockitoBean
  private DataSource dataSource;

  // --- TESTES DO GET (Consultar Cidadão por CPF) ---
  @Nested
  @DisplayName("Testes para GET /v1/cidadao")
  class GetConsultarCidadao {

    @Test
    @DisplayName("Deve retornar 200 OK e o resultado quando o CPF for encontrado")
    void deveRetornarSucessoAoConsultarPorCpf() throws Exception {
      String cpfValido = "12345678901";
      WSResultDTO mockResult = WSResultDTO.builder()
          .cpf(cpfValido)
          .situacaoCodigo(TipoSituacao.EM_DEBITO.getCodigo()) // Altere para um Enum real do seu projeto
          .situacaoDescricao(TipoSituacao.EM_DEBITO.getDescricao())
          .build();

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class))).thenReturn(mockResult);

      mockMvc.perform(get("/v1/cidadao")
          .param("cpf", cpfValido)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.cpf").value(cpfValido));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando NoDataFoundException for lançada")
    void deveRetornar404QuandoCidadaoNaoForEncontrado() throws Exception {
      String cpfInexistente = "00000000000";

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
      .thenThrow(new NoDataFoundException("Cidadão não encontrado"));

      mockMvc.perform(get("/v1/cidadao")
          .param("cpf", cpfInexistente)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.cpf").value(cpfInexistente))
      .andExpect(jsonPath("$.situacaoCodigo").value(TipoSituacao.NAO_ENCONTRADO.getCodigo()));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando BabiloniaException for lançada")
    void deveRetornar400QuandoOcorrerErroDeNegocio() throws Exception {
      String cpfInvalido = "11111111111";

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
      .thenThrow(new BabiloniaException("CPF inválido ou mal formatado"));

      mockMvc.perform(get("/v1/cidadao")
          .param("cpf", cpfInvalido)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.situacaoCodigo").value(TipoSituacao.REQUISICAO_INVALIDA.getCodigo()));
    }

    @Test
    @DisplayName("Deve retornar 500 Internal Server Error quando ocorrer uma exceção genérica")
    void deveRetornar500QuandoOcorrerErroInesperado() throws Exception {
      String cpfError = "99999999999";

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
      .thenThrow(new RuntimeException("Erro inesperado no servidor remoto"));

      mockMvc.perform(get("/v1/cidadao")
          .param("cpf", cpfError)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.situacaoCodigo").value(TipoSituacao.FORA_AR.getCodigo()));
    }
  }

  // --- TESTES DO POST (Consultar Cidadão por Corpo de Requisição) ---
  @Nested
  @DisplayName("Testes para POST /v1/cidadao")
  class PostConsultarCidadao {

    @Test
    @DisplayName("Deve retornar 200 OK e limpar acentos do nome ao processar a requisição com sucesso")
    void deveRetornarSucessoAoConsultarPorDadosDoCidadao() throws Exception {
      CidadaoConsultDTO requestDTO = CidadaoConsultDTO.builder()
          .cpf("12345678901")
          .nome("João Conceição") // Contém acentos que devem ser limpos
          .nomeMae("Maria Conceição")
          .dataNascimento(LocalDate.of(1990, 5, 15))
          .build();

      WSResultDTO mockResult = WSResultDTO.builder()
          .cpf("12345678901")
          .nome("JOAO CONCEICAO") // Resultado esperado limpo
          .mae("MARIA CONCEICAO")
          .build();

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class))).thenReturn(mockResult);

      mockMvc.perform(post("/v1/cidadao")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("JOAO CONCEICAO"))
      .andExpect(jsonPath("$.mae").value("MARIA CONCEICAO"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando NoDataFoundException for lançada via POST")
    void deveRetornar404NoPostQuandoNaoEncontrado() throws Exception {
      CidadaoConsultDTO requestDTO = CidadaoConsultDTO.builder()
          .cpf("00000000000")
          .nome("Inexistente")
          .nomeMae("Inexistente Mae")
          .dataNascimento(LocalDate.of(1970, 1, 1))
          .build();

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
      .thenThrow(new NoDataFoundException("Não encontrado"));

      mockMvc.perform(post("/v1/cidadao")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.situacaoCodigo").value(TipoSituacao.NAO_ENCONTRADO.getCodigo()));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se a validação do corpo da requisição falhar")
    void deveRetornar400QuandoPayloadForInvalido() throws Exception {
      // Se CidadaoConsultDTO possuir validações do jakarta.validation (ex: @NotNull)
      // Enviar um objeto vazio ou inválido deve falhar antes de chamar o service
      CidadaoConsultDTO requestInvalido = CidadaoConsultDTO.builder().build();

      mockMvc.perform(post("/v1/cidadao")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestInvalido)))
      .andExpect(status().isBadRequest());
    }

  }

}
