package br.com.wagnersoft.babilonia.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import br.com.wagnersoft.babilonia.dto.ErrorResultDTO;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container com a descrição dos status de erro retornados pela API para apresentação na {@code OpenAPI}.
 * <p>Os métodos dos controladores recebem esta anotação, centralizando e padronizando a descição das mensagens
 * de erro apresentadas na tela da OpenAPI.</p>
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Requisição inválida: verifique os parâmetros enviados.",
      content = @Content(schema = @Schema(implementation = ErrorResultDTO.class),
      examples = @ExampleObject(value = "{ \"situacaoCodigo\": 99, \"situacaoDescricao\": \"Requisição inválida: informe um ID válido.\", \"consultaData\": \"2026-07-19\" }"))),

    @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey inexistente ou inválida.",
      content = @Content(schema = @Schema(implementation = ErrorResultDTO.class),
      examples = @ExampleObject(value = "{ \"situacaoCodigo\": 99, \"situacaoDescricao\": \"Não autorizado: apiKey informada não existe ou é inválida.\", \"consultaData\": \"2026-07-19\" }"))),

    @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirada.",
      content = @Content(schema = @Schema(implementation = ErrorResultDTO.class),
      examples = @ExampleObject(value = "{ \"situacaoCodigo\": 99, \"situacaoDescricao\": \"Não permitido: apiKey expirou.\", \"consultaData\": \"2026-07-19\" }"))),

    @ApiResponse(responseCode = "404", description = "Recurso não localizado.",
      content = @Content(schema = @Schema(implementation = ErrorResultDTO.class),
      examples = @ExampleObject(value = "{ \"situacaoCodigo\": 99, \"situacaoDescricao\": \"Não existe o ID informado.\", \"consultaData\": \"2026-07-19\" }"))),

    @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.",
      content = @Content(schema = @Schema(implementation = ErrorResultDTO.class),
      examples = @ExampleObject(value = "{ \"situacaoCodigo\": 99, \"situacaoDescricao\": \"Erro interno no servidor.\", \"consultaData\": \"2026-07-19\" }")))
})
public @interface ApiStandardErrors {
}
