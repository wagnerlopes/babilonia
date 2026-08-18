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
import br.com.wagnersoft.babilonia.model.Uf;
import br.com.wagnersoft.babilonia.service.UfService;
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

  public record MesoRegiaoDTO(Integer id, String descricao) {};

  public record UfDTO(String uf, String descricao, List<MesoRegiaoDTO> mesorregiao) {};

  @Autowired
  private UfService ufSvc;

  @GetMapping("/sigla")
  @Operation(summary = "Consulta de UF por sigla.", description = "Deverá ser informado a sigla da UF.")
  @ApiResponse(responseCode = "200", description = "Informação de UF.")
  @ApiStandardErrors
  public ResponseEntity<UfDTO> consultarPorSigla(@Parameter(description = "sigla", example = "DF") @RequestParam final String sigla) {

    return ufSvc.consultBySigla(sigla)
        .map(u -> new UfDTO(
            u.getSigla(),
            u.getDescricao(),
            u.getMesoregioes().stream()
              .map(m -> new MesoRegiaoDTO(m.getId(), m.getDescricao()))
              .sorted(Comparator.comparing(MesoRegiaoDTO::descricao))
              .toList()))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

}
