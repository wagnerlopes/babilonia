package br.com.wagnersoft.babilonia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wagnersoft.babilonia.exception.ApiStandardErrors;
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.service.UfService;
import br.com.wagnersoft.babilonia.service.UfService.UfDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações de {@link Uf unidade da federação}.
 * 
 * <p>Fornece as operações de consulta com base na sigla da UF.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/uf", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/uf", description = "Unidade da Federação endpoint")
@SecurityRequirement(name = "apikey")
public class UfController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(UfController.class);

  private final UfService svc;

  /**
   *  Injeção automática do service via construtor.
   *  
   * @param svc {@link UfService}
   */
  public UfController(UfService svc) {
    this.svc = svc;
  }
  
  @GetMapping("/sigla")
  @Operation(summary = "Consulta de UF por sigla.", description = "Deverá ser informado a sigla da UF.")
  @ApiResponse(responseCode = "200", description = "Informação de UF.")
  @ApiStandardErrors
  public ResponseEntity<UfDTO> consultarPorSigla(@Parameter(description = "sigla", example = "DF") @RequestParam final String sigla) {

    UfDTO result = svc.consultBySigla(sigla);

    LOGGER.debug("{}", result);

    return ResponseEntity.ok(result);
  }

}
