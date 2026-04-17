package br.mil.eb.wagnersoft.babilonia.v2;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/** Controlador da pesquisa de certificado do cidadão.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
@RestController("certificadoController3")
@RequestMapping(value = "v2/certificado", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.TEXT_XML_VALUE})
@Tag(name = "v2/certificado", description = "API versão 2")
public class CertificadoController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(CertificadoController.class);

  @Autowired
  private SermilService sermilSvc;

  @GetMapping
  @Operation(summary = "Consulta o certificado do cidadão.",
  description = "Deverá ser informado o cidadão e, caso exista, será retornada uma imagem PNG do certificado no format String Base64.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Requisição inválida: informe um RA válido."),
      @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey informada não existe ou é válida."),
      @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirou e deverá ser solicitada uma nova chave de acesso."),
      @ApiResponse(responseCode = "404", description = "Não existe cidadão com o RA informado"),
      @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.")
  })
  @SecurityRequirement(name = "apikey")
  public ResponseEntity<Object> consultarCertificado(@RequestParam(name="ra", required=true) @Parameter(description = "Somente os 12 dígitos do RA", example = "000000000000") final String ra) {
    try {
      String result = this.sermilSvc.consultDoc(ra);
      return ResponseEntity.ok(result);
    } catch (NoDataFoundException e) {
      return ResponseEntity.of(Optional.empty());
    } catch (ConectagovException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      LOGGER.error("{}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
