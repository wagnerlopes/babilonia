package br.com.wagnersoft.babilonia.v1;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import br.com.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dominio.dto.WSResultDTO;


/** Controlador da pesquisa de cidadão.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
@RestController("cidadaoController")
@RequestMapping(value = "v1/cidadao", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
@Tag(name = "v1/cidadao", description = "API versão 1")
public class CidadaoController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(CidadaoController.class);

  @Autowired
  private RemoteService rmtSvc;

  @GetMapping
  @Operation(summary = "Consulta o cidadão.",
  description = "Deverá ser informado o CPF do cidadão.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Informação de cidadão cadastrado.",
                   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = WSResultDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Requisição inválida: informe um CPF válido."),
      @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey informada não existe ou é válida."),
      @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirou e deverá ser solicitada uma nova chave de acesso."),
      @ApiResponse(responseCode = "404", description = "Não existe cidadão com o CPF informado"),
      @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.")
  })
  @SecurityRequirement(name = "apikey")
  public ResponseEntity<Object> consultarCidadao(@RequestParam(name="cpf", required=true) @Parameter(description = "Somente os 11 dígitos do CPF", example = "00000000000") final String cpf) {
    final CidadaoConsultDTO consult = CidadaoConsultDTO.builder().cpf(cpf).build();
    try {
      final WSResultDTO result = findInfo(consult);
      return ResponseEntity.ok(result);
    } catch (NoDataFoundException e) {
      LOGGER.debug("{}", e.getMessage());
      return returnError(consult, SituacaoEnum.NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
    } catch (BabiloniaException e) {
      LOGGER.debug("{}", e.getMessage());
      return returnError(consult, SituacaoEnum.NAO_ENCONTRADO, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      LOGGER.error("{}", e.getMessage());
      return returnError(consult, SituacaoEnum.FORA_AR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  @Operation(summary = "Consulta o cidadão pelo CPF, nome, nome da mãe, data de nascimento e Força Armada (default 0 = nenhuma).",
  description = "Deverão ser informados os dados completos do cidadão.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cidadão cadastrado no SERMIL.",
                   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = WSResultDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Requisição inválida: informe um CPF válido."),
      @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey informada não existe ou é válida."),
      @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirou e deverá ser solicitada uma nova chave de acesso."),
      @ApiResponse(responseCode = "404", description = "Não existe cidadão com o CPF informado"),
      @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.")
  })
  public ResponseEntity<Object> consultarCidadao(@Valid @RequestBody(required=true) @Parameter(description = "Informações do cidadão sendo obrigatório: nome, mae e data nascimento.") CidadaoConsultDTO consult) {
    try {
      consult.setNome(StringCleanup.cleanAccent(consult.getNome()));
      consult.setNomeMae(StringCleanup.cleanAccent(consult.getNomeMae()));
      WSResultDTO result = findInfo(consult);
      return ResponseEntity.ok(result);
    } catch (NoDataFoundException ndf) {
      LOGGER.debug("{}", ndf.getMessage());
      return returnError(consult, SituacaoEnum.NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
    } catch (BabiloniaException e) {
      LOGGER.debug("{}", e.getMessage());
      return returnError(consult, SituacaoEnum.NAO_ENCONTRADO, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      LOGGER.error("{}", e.getMessage());
      return returnError(consult, SituacaoEnum.FORA_AR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private WSResultDTO findInfo(final CidadaoConsultDTO consult) throws BabiloniaException {
    final WSResultDTO result = this.rmtSvc.consultService(consult);
    LOGGER.debug("{}", result);
    return result;
  }

  private ResponseEntity<Object> returnError(final CidadaoConsultDTO consult, final SituacaoEnum situacao, final HttpStatus httpStatus) {
    final WSResultDTO result = WSResultDTO.builder()
        .cpf(consult.getCpf())
        .nome(consult.getNome())
        .mae(consult.getNomeMae())
        .nascimentoData(consult.getDataNascimento())
        .consultaData(LocalDate.now())
        .situacaoCodigo(situacao.getCodigo())
        .situacaoDescricao(situacao.getDescricao())
        .build();
    return ResponseEntity.status(httpStatus).body(result);
  }

}
