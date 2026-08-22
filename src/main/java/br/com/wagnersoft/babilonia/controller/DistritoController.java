package br.com.wagnersoft.babilonia.controller;

import java.util.Comparator;
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

import br.com.wagnersoft.babilonia.exception.ApiStandardErrors;
import br.com.wagnersoft.babilonia.model.Distrito;
import br.com.wagnersoft.babilonia.service.DistritoService;
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

  public record LocalidadeDTO(Integer id, String descricao) { };

  public record DistritoDTO(Integer id, String descricao, List<LocalidadeDTO> localidades) { };

  @Autowired
  private DistritoService disSvc;

  @GetMapping("/id")
  @Operation(summary = "Consulta de distrito por ID.", description = "Deverá ser informado o ID do distrito.")
  @ApiResponse(responseCode = "200", description = "Informação de distrito.")
  @ApiStandardErrors
  public ResponseEntity<DistritoDTO> consultarPorId(@Parameter(description = "id", example = "1") @RequestParam final Integer id) {

    return disSvc.consultById(id)
        .map(dis -> new DistritoDTO(
            dis.getId(),
            dis.getDescricao(),
            dis.getLocalidades().stream()
              .map(m -> new LocalidadeDTO(m.getId(), m.getDescricao()))
              .sorted(Comparator.comparing(LocalidadeDTO::descricao))
              .toList()
            ))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

}
