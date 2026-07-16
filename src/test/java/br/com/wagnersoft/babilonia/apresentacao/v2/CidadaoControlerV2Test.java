package br.com.wagnersoft.babilonia.apresentacao.v2;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

import br.com.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dominio.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;
import br.com.wagnersoft.babilonia.exceptions.NoDataFoundException;
import br.com.wagnersoft.babilonia.services.RemoteService;

@WebMvcTest(CidadaoController.class)
//@ImportAutoConfiguration(JacksonAutoConfiguration.class) // Garante suporte nativo para LocalDate sem quebrar
class CidadaoControllerV2Test {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @MockitoBean
  private RemoteService rmtSvc;

  @MockitoBean
  private DataSource dataSource; // Fornece o mock necessário exigido pela inicialização do contexto

  // --- TESTES DO GET /v2/cidadao ---
  @Nested
  @DisplayName("Testes para GET /v2/cidadao")
  class GetConsultarCidadao {

    @Test
    @DisplayName("Deve retornar 200 OK e o resultado quando o CPF for encontrado")
    void deveRetornarSucessoAoConsultarPorCpf() throws Exception {
      String cpfValido = "12345678901";
      WSResultDTO mockResult = WSResultDTO.builder()
          .cpf(cpfValido)
          .nome("JOÃO SILVA")
          .build();

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class))).thenReturn(mockResult);

      mockMvc.perform(get("/v2/cidadao")
          .param("cpf", cpfValido)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.cpf").value(cpfValido))
      .andExpect(jsonPath("$.nome").value("JOÃO SILVA"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando NoDataFoundException for lançada")
    void deveRetornar404QuandoCidadaoNaoForEncontrado() throws Exception {
      String cpfInexistente = "44331032007"; // CPF fictício matematicamente válido

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
          .thenThrow(new NoDataFoundException("Cidadão não encontrado"));

      mockMvc.perform(get("/v2/cidadao")
          .param("cpf", cpfInexistente)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(content().string("")); // Retorna ResponseEntity.of(Optional.empty()) que é corpo vazio
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando BabiloniaException for lançada")
    void deveRetornar400QuandoOcorrerErroDeNegocio() throws Exception {
      String cpfInvalido = "11111111111";
      String mensagemErro = "CPF inválido ou mal formatado";

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
          .thenThrow(new BabiloniaException(mensagemErro));

      mockMvc.perform(get("/v2/cidadao")
          .param("cpf", cpfInvalido)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(mensagemErro));
    }

    @Test
    @DisplayName("Deve retornar 500 Internal Server Error quando ocorrer erro inesperado")
    void deveRetornar500QuandoOcorrerErroInesperado() throws Exception {
      String cpfErro = "99999999999";
      String mensagemErro = "Erro inesperado no banco de dados";

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
          .thenThrow(new RuntimeException(mensagemErro));

      mockMvc.perform(get("/v2/cidadao")
          .param("cpf", cpfErro)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isInternalServerError())
      .andExpect(content().string(mensagemErro));
    }
  }

  // --- TESTES DO POST /v2/cidadao ---
  @Nested
  @DisplayName("Testes para POST /v2/cidadao")
  class PostConsultarCidadao {

    @Test
    @DisplayName("Deve retornar 200 OK e limpar acentos do nome ao processar a requisição com sucesso")
    void deveRetornarSucessoAoConsultarPorDadosDoCidadao() throws Exception {
      CidadaoConsultDTO requestDTO = CidadaoConsultDTO.builder()
          .cpf("12345678901")
          .nome("João Conceição") // Contém caracteres acentuados
          .nomeMae("Maria Conceição")
          .dataNascimento(LocalDate.of(1990, 5, 15))
          .build();

      WSResultDTO mockResult = WSResultDTO.builder()
          .cpf("12345678901")
          .nome("JOAO CONCEICAO") // Resultado esperado normalizado/limpo
          .build();

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class))).thenReturn(mockResult);

      mockMvc.perform(post("/v2/cidadao")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nome").value("JOAO CONCEICAO"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando NoDataFoundException for lançada via POST")
    void deveRetornar404NoPostQuandoNaoEncontrado() throws Exception {
      CidadaoConsultDTO requestDTO = CidadaoConsultDTO.builder()
          .cpf("44331032007")
          .nome("Inexistente")
          .nomeMae("Inexistente Mae")
          .dataNascimento(LocalDate.of(1970, 1, 1))
          .build();

      when(rmtSvc.consultService(any(CidadaoConsultDTO.class)))
          .thenThrow(new NoDataFoundException("Não encontrado"));

      mockMvc.perform(post("/v2/cidadao")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
      .andExpect(status().isNotFound())
      .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se a validação do corpo da requisição falhar")
    void deveRetornar400QuandoPayloadForInvalido() throws Exception {
      // Envia um payload vazio para acionar o @Valid do controller
      CidadaoConsultDTO requestInvalido = CidadaoConsultDTO.builder().build();

      mockMvc.perform(post("/v2/cidadao")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestInvalido)))
      .andExpect(status().isBadRequest());
    }
  }
}