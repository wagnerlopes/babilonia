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
import br.com.wagnersoft.babilonia.model.Municipio;
import br.com.wagnersoft.babilonia.service.MunicipioService;
import br.com.wagnersoft.babilonia.service.MunicipioService.MunicipioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações de {@link Municipio município}.
 * 
 * <p>Fornece as operações de consulta com base no ID ou descrição.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/municipio", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/municipio", description = "Município endpoint")
@SecurityRequirement(name = "apikey")
public class MunicipioController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(MunicipioController.class);

  private final MunicipioService svc;

  /**
   *  Injeção automática do service via construtor.
   *  
   * @param svc {@link MunicipioService}
   */
  public MunicipioController(MunicipioService svc) {
    this.svc = svc;
  }

  @GetMapping("/id")
  @Operation(summary = "Consulta de município por ID.", description = "Deverá ser informado o ID do município.")
  @ApiResponse(responseCode = "200", description = "Informação de município.")
  @ApiStandardErrors
  public ResponseEntity<MunicipioDTO> consultarPorId(@Parameter(description = "id", example = "1") @RequestParam final Integer id) {

    MunicipioDTO result = svc.consultById(id);

    LOGGER.debug("Municipío localizado: {}", result);

    return ResponseEntity.ok(result);
  }

}
