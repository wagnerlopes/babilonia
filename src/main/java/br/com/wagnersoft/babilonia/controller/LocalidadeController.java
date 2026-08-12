package br.com.wagnersoft.babilonia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wagnersoft.babilonia.dto.LocResultDTO;
import br.com.wagnersoft.babilonia.exception.ApiStandardErrors;
import br.com.wagnersoft.babilonia.service.LocalidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsável por gerenciar e expor os endpoints de informações de localidade.
 * 
 * <p>Fornece as operações de consulta com base no ID ou descrição.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(value = "v1/localidade", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "v1/localidade", description = "API versão 1")
@SecurityRequirement(name = "apikey")
public class LocalidadeController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(LocalidadeController.class);

  @Autowired
  private LocalidadeService locSvc;

  @GetMapping("/id")
  @Operation(summary = "Consulta a localidade por ID.", description = "Deverá ser informado o ID da localidade.")
  @ApiResponse(responseCode = "200", description = "Informação de localidade.")
  @ApiStandardErrors  
  public ResponseEntity<LocResultDTO> consultarLocalidade(
      @Parameter(description = "ID", example = "1")
      @RequestParam final Integer id) {
    final LocResultDTO result = this.locSvc.consultById(id);
    LOGGER.debug("{}", result);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/descricao")
  @Operation(summary = "Consulta a localidade por nome.", description = "Deverá ser informado o nome da localidade.")
  @ApiResponse(responseCode = "200", description = "Informação de localidade.")
  @ApiStandardErrors  
  public ResponseEntity<List<LocResultDTO>> consultarLocalidade(
      @Parameter(description = "descricao", example = "Maracá")
      @RequestParam String descricao) {
    final List<LocResultDTO> result = this.locSvc.consultByDescricao(descricao);
    LOGGER.debug("{}", result);
    return ResponseEntity.ok(result);
  }

}
