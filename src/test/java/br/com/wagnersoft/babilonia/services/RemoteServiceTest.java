package br.com.wagnersoft.babilonia.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wagnersoft.babilonia.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exception.BabiloniaException;
import br.com.wagnersoft.babilonia.model.Cidadao;
import br.com.wagnersoft.babilonia.repository.CidadaoRepository;
import br.com.wagnersoft.babilonia.service.ConsultService;

@ExtendWith(MockitoExtension.class)
class RemoteServiceTest {

  @Mock
  private CidadaoRepository cidadaoRep;

  @InjectMocks
  private ConsultService remoteService;

  // Helper para criar uma entidade Cidadão válida para o Mock
  private Cidadao criarCidadaoMock(String cpf) {
    Cidadao cidadao = new Cidadao();
    cidadao.setCpf(cpf);
    cidadao.setNome("JOÃO SILVA");
    cidadao.setMae("MARIA SILVA");
    cidadao.setPai("JOSÉ SILVA");
    cidadao.setNascimentoData(LocalDate.of(1995, 10, 10));
    return cidadao;
  }

  @Nested
  @DisplayName("Testes para consultService(CidadaoConsultDTO)")
  class ConsultServiceIndividual {

    @Test
    @DisplayName("Deve retornar dados corretos quando o cidadão for encontrado pelo CPF")
    void deveRetornarCidadaoPorCpf() throws BabiloniaException {
      // Arrange
      String cpf = "12345678901";
      CidadaoConsultDTO consultDTO = CidadaoConsultDTO.builder().cpf(cpf).build();
      Cidadao cidadaoMock = criarCidadaoMock(cpf);

      when(cidadaoRep.findByCpf(cpf)).thenReturn(Optional.of(cidadaoMock));

      // Act
      WSResultDTO result = remoteService.consultService(consultDTO);

      // Assert
      assertNotNull(result);
      assertEquals(cpf, result.getCpf());
      verify(cidadaoRep).findByCpf(cpf);
    }

    @Test
    @DisplayName("Deve retornar cidadão 'Não Cadastrado' quando o CPF informado não existir na base")
    void deveRetornarCidadaoNaoCadastradoSeNaoEncontrarCpf() throws BabiloniaException {
      // Arrange
      String cpfInexistente = "11111111111";
      CidadaoConsultDTO consultDTO = CidadaoConsultDTO.builder().cpf(cpfInexistente).build();

      when(cidadaoRep.findByCpf(cpfInexistente)).thenReturn(Optional.empty());

      // Act
      WSResultDTO result = remoteService.consultService(consultDTO);

      // Assert
      assertNotNull(result);
      assertEquals(cpfInexistente, result.getCpf());
      verify(cidadaoRep).findByCpf(cpfInexistente);
    }

    @Test
    @DisplayName("Deve buscar por nome, mãe e nascimento quando o CPF não for fornecido")
    void deveBuscarPorCamposSecundariosSeCpfEstiverVazio() throws BabiloniaException {
      // Arrange
      CidadaoConsultDTO consultDTO = CidadaoConsultDTO.builder()
          .cpf("") // CPF em branco aciona o bloco 'else'
          .nome("JOÃO SILVA")
          .nomeMae("MARIA SILVA")
          .dataNascimento(LocalDate.of(1995, 10, 10))
          .build();
      Cidadao cidadaoMock = criarCidadaoMock("99999999999");

      when(cidadaoRep.findByOutros("JOÃO SILVA", "MARIA SILVA", LocalDate.of(1995, 10, 10)))
      .thenReturn(Optional.of(cidadaoMock));

      // Act
      WSResultDTO result = remoteService.consultService(consultDTO);

      // Assert
      assertNotNull(result);
      assertEquals("99999999999", result.getCpf());
      verify(cidadaoRep).findByOutros("JOÃO SILVA", "MARIA SILVA", LocalDate.of(1995, 10, 10));
    }

    @Test
    @DisplayName("Não deve acessar o repositório se o CPF estiver vazio e dados secundários estiverem incompletos")
    void naoDeveChamarRepositórioSeDadosForemInsuficientes() throws BabiloniaException {
      // Arrange (CPF nulo e data de nascimento nula)
      CidadaoConsultDTO consultDTO = CidadaoConsultDTO.builder()
          .cpf(null)
          .nome("JOÃO SILVA")
          .nomeMae("MARIA SILVA")
          .dataNascimento(null)
          .build();

      // Act
      WSResultDTO result = remoteService.consultService(consultDTO);

      // Assert
      assertNotNull(result);
      // Garante que o fluxo caiu diretamente no orElse(Cidadao.naoCadastrado) sem bater no banco de dados
      verifyNoInteractions(cidadaoRep);
    }
  }
}
