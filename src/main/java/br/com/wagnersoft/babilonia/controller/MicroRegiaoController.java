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
import br.com.wagnersoft.babilonia.model.MicroRegiao;
import br.com.wagnersoft.babilonia.service.MicroRegiaoService;
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

  public record MunicipioDTO(Integer id, String descricao) {};

  public record MicroRegiaoDTO(Integer id, String descricao, List<MunicipioDTO> municipios) {};

  @Autowired
  private MicroRegiaoService microSvc;

  @GetMapping("/id")
  @Operation(summary = "Consulta de microrregiao por ID.", description = "Deverá ser informado o ID da microrregião.")
  @ApiResponse(responseCode = "200", description = "Informação de microrregião.")
  @ApiStandardErrors
  public ResponseEntity<MicroRegiaoDTO> consultarPorId(@Parameter(description = "id", example = "1") @RequestParam final Integer id) {

    return microSvc.consultById(id)
        .map(micro -> new MicroRegiaoDTO(
            micro.getId(),
            micro.getDescricao(),
            micro.getMunicipios().stream()
              .map(m -> new MunicipioDTO(m.getCodigo(), m.getDescricao()))
              .sorted(Comparator.comparing(MunicipioDTO::descricao))
              .toList()
            ))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

}
