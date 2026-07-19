package br.com.wagnersoft.babilonia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wagnersoft.babilonia.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exception.ApiStandardErrors;
import br.com.wagnersoft.babilonia.service.ConsultService;
import br.com.wagnersoft.babilonia.util.ApplicationUtilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações do cidadão.
 * 
 * <p>Fornece as operações de consulta com base no CPF ou outros dados de identificação.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/cidadao", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/cidadao", description = "API versão 1")
@SecurityRequirement(name = "apikey")
public class CidadaoController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(CidadaoController.class);

  @Autowired
  private ConsultService rmtSvc;

  @GetMapping
  @Operation(summary = "Consulta o cidadão.", description = "Deverá ser informado o CPF do cidadão.")
  @ApiResponse(responseCode = "200", description = "Informação de cidadão cadastrado.")
  @ApiStandardErrors  
  public ResponseEntity<WSResultDTO> consultarCidadao(
      @Parameter(description = "Somente os 11 dígitos do CPF", example = "00000000000")
      @RequestParam final String cpf) {
    final CidadaoConsultDTO consult = CidadaoConsultDTO.builder().cpf(cpf).build();
    final WSResultDTO result = this.rmtSvc.consultService(consult);
    LOGGER.debug("{}", result);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  @Operation(summary = "Consulta o cidadão pelo CPF, nome, nome da mãe, data de nascimento.", description = "Deverão ser informados os dados completos do cidadão.")
  @ApiResponse(responseCode = "200", description = "Cidadão cadastrado localizado com sucesso.")
  @ApiStandardErrors
  public ResponseEntity<WSResultDTO> consultarCidadao(@Valid @RequestBody CidadaoConsultDTO consult) {
    consult.setNome(ApplicationUtilities.cleanAccent(consult.getNome()));
    consult.setNomeMae(ApplicationUtilities.cleanAccent(consult.getNomeMae()));
    final WSResultDTO result = this.rmtSvc.consultService(consult);
    return ResponseEntity.ok(result);
  }

}
