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
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.service.MesoRegiaoService;
import br.com.wagnersoft.babilonia.service.MesoRegiaoService.MesoRegiaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações de {@link MesoRegiao mesorregião}.
 * 
 * <p>Fornece as operações de consulta com base no ID ou descrição.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/mesorregiao", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/mesorregiao", description = "Mesorregião endpoint")
@SecurityRequirement(name = "apikey")
public class MesoRegiaoController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(MesoRegiaoController.class);

  private final MesoRegiaoService svc;

  /**
   *  Injeção automática do service via construtor.
   *  
   * @param svc {@link MesoRegiaoService}
   */
  public MesoRegiaoController(MesoRegiaoService svc) {
    this.svc = svc;
  }
  
  @GetMapping("/id")
  @Operation(summary = "Consulta de mesorregiao por ID.", description = "Deverá ser informado o ID da mesorregião.")
  @ApiResponse(responseCode = "200", description = "Informação de mesorregião.")
  @ApiStandardErrors
  public ResponseEntity<MesoRegiaoDTO> consultarPorId(@Parameter(description = "id", example = "1") @RequestParam final Integer id) {

    MesoRegiaoDTO result = svc.consultById(id);

    LOGGER.debug("Messoregião localizada: ", result);
    
    return  ResponseEntity.ok(result);
  }

}
