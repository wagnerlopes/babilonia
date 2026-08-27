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
import br.com.wagnersoft.babilonia.model.dto.LocalidadeDTO;
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
@Tag(name = "v1/localidade", description = "Localidade Endpoint")
@SecurityRequirement(name = "apikey")
public class LocalidadeController {

  protected static final Logger LOGGER = LoggerFactory.getLogger(LocalidadeController.class);

  public record DistanciaDTO(String origem, String destino, Double distancia, String unidade) { };

  private final LocalidadeService svc;

  /**
   *  Injeção automática do service via construtor.
   *  
   * @param svc {@link LocalidadeService}
   */
  public LocalidadeController(LocalidadeService svc) {
    this.svc = svc;
  }

  @GetMapping("/id")
  @Operation(summary = "Consulta a localidade por ID.", description = "Deverá ser informado o ID da localidade.")
  @ApiResponse(responseCode = "200", description = "Informação de localidade.")
  @ApiStandardErrors  
  public ResponseEntity<LocalidadeDTO> consultarLocalidade(
      @Parameter(description = "ID", example = "1")
      @RequestParam final Integer id) {
    final LocalidadeDTO result = this.svc.consultById(id);
    LOGGER.debug("{}", result);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/descricao")
  @Operation(summary = "Consulta a localidade por nome.", description = "Deverá ser informado o nome da localidade.")
  @ApiResponse(responseCode = "200", description = "Informação de localidade.")
  @ApiStandardErrors  
  public ResponseEntity<List<LocalidadeDTO>> consultarLocalidade(
      @Parameter(description = "descricao", example = "Maracá")
      @RequestParam String descricao) {
    final List<LocalidadeDTO> result = this.svc.consultByDescricao(descricao);
    LOGGER.debug("{}", result);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/distancia")
  @Operation(summary = "Calcula a distância entre duas localidades.", description = "Deverá ser informado o ID da origem e do destino.")
  @ApiResponse(responseCode = "200", description = "Distância entre duas localidades localidade.")
  @ApiStandardErrors  
  public ResponseEntity<Object> consultarDistancia(
      @Parameter(description = "ID da origem", example = "1") @RequestParam Integer origem,
      @Parameter(description = "ID do destino", example = "2") @RequestParam Integer destino) {
    LocalidadeDTO local1 = this.svc.consultById(origem);
    LocalidadeDTO local2 = this.svc.consultById(destino);
    double dkm = Math.round(this.svc.distancia(origem, destino) / 1000.0);
    LOGGER.debug("{}", dkm);
    return ResponseEntity.ok(new DistanciaDTO(local1.getDescricao(), local2.getDescricao(), dkm, "Km"));
  }

}
