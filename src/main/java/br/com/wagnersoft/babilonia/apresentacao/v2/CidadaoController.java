package br.com.wagnersoft.babilonia.apresentacao.v2;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wagnersoft.babilonia.dominio.dto.CidadaoConsultDTO;
import br.com.wagnersoft.babilonia.dominio.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;
import br.com.wagnersoft.babilonia.exceptions.NoDataFoundException;
import br.com.wagnersoft.babilonia.services.RemoteService;
import br.com.wagnersoft.babilonia.utils.StringCleanup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/** Controlador da pesquisa do cidadão.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
@RestController("cidadaoController2")
@RequestMapping(value = "v2/cidadao", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
@Tag(name = "v2/cidadao", description = "API versão 2")
public class CidadaoController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CidadaoController.class);

  @Autowired
  private RemoteService rmtSvc;

  @GetMapping
  @Operation(summary = "Consulta o cidadão.",
             description = "Deverá ser informado o cidadão.",
             security = {@SecurityRequirement(name = "x-api-key")})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cidadão cadastrado.",
                   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = WSResultDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Requisição inválida: informe um CPF válido."),
      @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey informada não existe ou é válida."),
      @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirou e deverá ser solicitada uma nova chave de acesso."),
      @ApiResponse(responseCode = "404", description = "Não existe cidadão com o CPF informado"),
      @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.")
  })
  public ResponseEntity<Object> consultarCidadao(@RequestParam(name="cpf", required=true) @Parameter(description = "Somente os 11 dígitos do CPF", example = "00000000000") final String cpf) {
    CidadaoConsultDTO consult = null;
    try {
      consult = CidadaoConsultDTO.builder().cpf(cpf).build();
      WSResultDTO result = this.rmtSvc.consultService(consult);
      return ResponseEntity.ok(result);
    } catch (NoDataFoundException e) {
      LOGGER.debug("{}", e.getMessage());
      return ResponseEntity.of(Optional.empty());
    } catch (BabiloniaException e) {
      LOGGER.debug("{}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      LOGGER.error("{}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping
  @Operation(summary = "Consulta o cidadão, nome, nome da mãe e data de nascimento.",
             description = "Deverão ser informados os dados completos do cidadão.",
             security = {@SecurityRequirement(name = "apiKey")})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cidadão cadastrado.",
                   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = WSResultDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Requisição inválida: informe CPF, nome, mãe e data de nascimento."),
      @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey informada não existe ou é válida."),
      @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirou e deverá ser solicitada uma nova chave de acesso."),
      @ApiResponse(responseCode = "404", description = "Não existe cidadão com os dados informados."),
      @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.")
  })
  @SecurityRequirement(name = "apikey")
  public ResponseEntity<Object> consultarCidadao(@Valid @RequestBody(required=true) @Parameter(description = "Informações do cidadão sendo obrigatório: nome, mae e data nascimento.") CidadaoConsultDTO consult) {
    try {
      consult.setNome(StringCleanup.cleanAccent(consult.getNome()));
      consult.setNomeMae(StringCleanup.cleanAccent(consult.getNomeMae()));
      WSResultDTO result = this.rmtSvc.consultService(consult);
      return ResponseEntity.ok(result);
    } catch (NoDataFoundException ndf) {
      LOGGER.debug("{}", ndf.getMessage());
      return ResponseEntity.of(Optional.empty());
    } catch (BabiloniaException e) {
      LOGGER.debug("{}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      LOGGER.error("{}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
