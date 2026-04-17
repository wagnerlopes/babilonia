package br.mil.eb.sermil.webservices.conectagov.apresentacao.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import br.mil.eb.sermil.webservices.conectagov.dominio.dto.CidadaoConsultDTO;

class CidadaoControllerIT extends BaseIntegration {

  private static final String URL_BASE = "/cidadao/v1/";

  @Test
  void consultarSituacaoCidadao() throws Exception {
    mockMvc.perform(get(URL_BASE + "?ra=123451")
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.numeroRa").value(123451));
  }

  @Test
  void consultarSituacaoCidadaoSemResultadoCpf() throws Exception {
    mockMvc.perform(get(URL_BASE + "?cpf=14576843575")
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isNotFound());
  }

  @Test
  void consultarSituacaoCidadaoInexistente() throws Exception {
    mockMvc.perform(get(URL_BASE + "?cpf=123451588")
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().is4xxClientError())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("REGISTRO NÃO ENCONTRADO, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR"));
  }

  @Test
  void consultarSituacaoCidadaoOutros() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("01215478922").nome("nome teste").nomeMae("nome mae").dataNascimento(LocalDate.now()).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().is4xxClientError())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("REGISTRO NÃO ENCONTRADO, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR"));
  }

  @Test
  void consultarSituacaoCidadaoOutrosNome() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("76116303773").nome("nome teste").nomeMae("nome mae").dataNascimento(LocalDate.now()).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DIA COM O SERVIÇO MILITAR - DIVERGÊNCIA DE NOME: Cidadao C; DIVERGÊNCIA DE NOME DA MÃE: Mae do Cidadao C; DIVERGÊNCIA DE DATA DE NASCIMENTO: 07/09/2011"));
  }

  @Test
  void consultarSituacaoCidadaoOutrosNomeMae() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("76116303773").nome("Cidadao C").nomeMae("nome mae").dataNascimento(LocalDate.now()).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DIA COM O SERVIÇO MILITAR - DIVERGÊNCIA DE NOME DA MÃE: Mae do Cidadao C; DIVERGÊNCIA DE DATA DE NASCIMENTO: 07/09/2011"));
  }

  @Test
  void consultarSituacaoCidadaoOutrosNascimento() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("76116303773").nome("Cidadao C").nomeMae("Mae do Cidadao C").dataNascimento(LocalDate.now()).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DIA COM O SERVIÇO MILITAR - DIVERGÊNCIA DE DATA DE NASCIMENTO: 07/09/2011"));
  }

  @Test
  void consultarSituacaoCidadaoCpfEmDebito() throws Exception {
    mockMvc.perform(get(URL_BASE + "?cpf=13788433809")
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DÉBITO COM O SERVIÇO MILITAR, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR"));                
  }

  @Test
  void consultarSituacaoCidadaoCpfEmDebitoDivergencia() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("13788433809").nome("Cidadao C").nomeMae("Mae do Cidadao C").dataNascimento(LocalDate.now()).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DÉBITO COM O SERVIÇO MILITAR, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR - DIVERGÊNCIA DE NOME: Cidadao D; DIVERGÊNCIA DE NOME DA MÃE: Mae do Cidadao D; DIVERGÊNCIA DE DATA DE NASCIMENTO: 07/09/1999"));
  }

  @Test
  void consultarSituacaoCidadaoEmDebitoOutrosComCpfDivergente() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("13788433809").nome("Cidadao D").nomeMae("Mae do Cidadao D").dataNascimento(LocalDate.parse("19990907", DateTimeFormatter.ofPattern("yyyyMMdd"))).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DÉBITO COM O SERVIÇO MILITAR, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR - DIVERGÊNCIA DE CPF: 13788433809"));
  }

  @Test
  void consultarSituacaoCidadaoEmDebitoOutrosSemCpfDivergente() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().nome("Cidadao D").nomeMae("Mae do Cidadao D").dataNascimento(LocalDate.parse("19990907", DateTimeFormatter.ofPattern("yyyyMMdd"))).build(); 
    mockMvc.perform(post(URL_BASE)
    .content(toJson(dto))
    .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DÉBITO COM O SERVIÇO MILITAR, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR"));
  }

  @Test
  void consultarSituacaoCidadaoEmDebitoOutrosSemCpf() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().nome("Cidadao Q").nomeMae("Mae do Cidadao Q").dataNascimento(LocalDate.parse("19990907", DateTimeFormatter.ofPattern("yyyyMMdd"))).build(); 
    mockMvc.perform(post(URL_BASE)
        .content(toJson(dto))
        .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DÉBITO COM O SERVIÇO MILITAR, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR"));
  }

  @Test
  void consultarSituacaoCidadaoEmDebitoOutrosSemCpf2() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("1234578888").nome("Cidadao Q").nomeMae("Mae do Cidadao Q").dataNascimento(LocalDate.parse("19990907", DateTimeFormatter.ofPattern("yyyyMMdd"))).build(); 
    mockMvc.perform(post(URL_BASE)
        .content(toJson(dto))
        .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DÉBITO COM O SERVIÇO MILITAR, DIRIJA-SE À JUNTA DE SERVIÇO MILITAR - DIVERGÊNCIA DE CPF: não encontrado"));
  }

  @Test
  void consultarSituacaoCidadaoEmDiaSemCpf() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().nome("Cidadao H").nomeMae("Mae do Cidadao H").dataNascimento(LocalDate.parse("19990907", DateTimeFormatter.ofPattern("yyyyMMdd"))).build(); 
    mockMvc.perform(post(URL_BASE)
        .content(toJson(dto))
        .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DIA COM O SERVIÇO MILITAR"));
  }

  @Test
  void consultarSituacaoCidadaoEmDiaSemCpfnull() throws Exception {
    CidadaoConsultDTO dto = CidadaoConsultDTO.builder().cpf("13788433808").nome("Cidadao H").nomeMae("Mae do Cidadao H").dataNascimento(LocalDate.parse("19990907", DateTimeFormatter.ofPattern("yyyyMMdd"))).build(); 
    mockMvc.perform(post(URL_BASE)
        .content(toJson(dto))
        .contentType(contentType))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$").isNotEmpty())
    .andExpect(jsonPath("$.descricaoSituacao").value("EM DIA COM O SERVIÇO MILITAR - DIVERGÊNCIA DE CPF: não encontrado"));
  }

}
