package br.com.wagnersoft.babilonia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wagnersoft.babilonia.exception.ApiStandardErrors;
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.service.DistritoService;
import br.com.wagnersoft.babilonia.service.DistritoService.DistritoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações de {@link Distrito distrito}.
 * 
 * <p>Fornece as operações de consulta com base no ID ou descrição.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/distrito", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/distrito", description = "Distrito endpoint")
@SecurityRequirement(name = "apikey")
public class DistritoController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(DistritoController.class);

  private final DistritoService svc;

  /**
   *  Injeção automática do service via construtor.
   *  
   * @param svc {@link DistritoService}
   */
  public DistritoController(DistritoService svc) {
    this.svc = svc;
  }

  @GetMapping("/id")
  @Operation(summary = "Consulta de distrito por ID.", description = "Deverá ser informado o ID do distrito.")
  @ApiResponse(responseCode = "200", description = "Informação de distrito.")
  @ApiStandardErrors
  public ResponseEntity<DistritoDTO> consultarPorId(@Parameter(description = "id", example = "1234") @RequestParam final Integer id) {

    final DistritoDTO result = svc.consultById(id);

    LOGGER.debug("{}", result);

    return ResponseEntity.ok(result);
  }

  @GetMapping("/descricao")
  @Operation(summary = "Consulta o distrito por nome.", description = "Deverá ser informado o nome do distrito.")
  @ApiResponse(responseCode = "200", description = "Informação de distrito.")
  @ApiStandardErrors  
  public ResponseEntity<List<DistritoDTO>> consultarDistrito(
      @Parameter(description = "descricao", example = "Maracá")
      @RequestParam String descricao) {
    final List<DistritoDTO> result = this.svc.consultByDescricao(descricao);
    LOGGER.debug("{}", result);
    return ResponseEntity.ok(result);
  }

}
