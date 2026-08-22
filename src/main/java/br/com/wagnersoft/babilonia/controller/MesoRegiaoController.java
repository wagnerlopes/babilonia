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
import br.com.wagnersoft.babilonia.model.MesoRegiao;
import br.com.wagnersoft.babilonia.service.MesoRegiaoService;
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

  public record MicroRegiaoDTO(Integer id, String descricao) { };

  public record MesoRegiaoDTO(Integer id, String descricao, List<MicroRegiaoDTO> microrregiao) { };

  @Autowired
  private MesoRegiaoService mesoSvc;

  @GetMapping("/id")
  @Operation(summary = "Consulta de mesorregiao por ID.", description = "Deverá ser informado o ID da mesorregião.")
  @ApiResponse(responseCode = "200", description = "Informação de mesorregião.")
  @ApiStandardErrors
  public ResponseEntity<MesoRegiaoDTO> consultarPorId(@Parameter(description = "id", example = "1") @RequestParam final Integer id) {

    return mesoSvc.consultById(id)
        .map(meso -> new MesoRegiaoDTO(
            meso.getId(),
            meso.getDescricao(),
            meso.getMicroregioes().stream()
              .map(m -> new MicroRegiaoDTO(m.getId(), m.getDescricao()))
              .sorted(Comparator.comparing(MicroRegiaoDTO::descricao))
              .toList()
            ))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

}
