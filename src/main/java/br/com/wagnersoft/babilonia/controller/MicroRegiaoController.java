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
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.service.MicroRegiaoService;
import br.com.wagnersoft.babilonia.service.MicroRegiaoService.MicroRegiaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações de {@link MicroRegiao microrregião}.
 * 
 * <p>Fornece as operações de consulta com base no ID ou descrição.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/microrregiao", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/microrregiao", description = "Microrregião endpoint")
@SecurityRequirement(name = "apikey")
public class MicroRegiaoController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(MicroRegiaoController.class);

  private final MicroRegiaoService svc;

  /**
   *  Injeção automática do service via construtor.
   *  
   * @param svc {@link MicroRegiaoService}
   */
  public MicroRegiaoController(MicroRegiaoService svc) {
    this.svc = svc;
  }
  
  @GetMapping("/id")
  @Operation(summary = "Consulta de microrregiao por ID.", description = "Deverá ser informado o ID da microrregião.")
  @ApiResponse(responseCode = "200", description = "Informação de microrregião.")
  @ApiStandardErrors
  public ResponseEntity<MicroRegiaoDTO> consultarPorId(@Parameter(description = "id", example = "1") @RequestParam final Integer id) {

    MicroRegiaoDTO result = svc.consultById(id);

    LOGGER.debug("Microrregião localizada: {}", result);
    
    return  ResponseEntity.ok(result);
  }

}
