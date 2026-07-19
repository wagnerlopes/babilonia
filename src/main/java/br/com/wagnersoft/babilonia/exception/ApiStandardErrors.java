package br.com.wagnersoft.babilonia.exception;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Requisição inválida: verifique os parâmetros enviados."),
    @ApiResponse(responseCode = "401", description = "Não autorizado: apiKey inexistente ou inválida."),
    @ApiResponse(responseCode = "403", description = "Não permitido: apiKey expirada."),
    @ApiResponse(responseCode = "404", description = "Recurso não localizado."),
    @ApiResponse(responseCode = "500", description = "Serviço indisponível, tente mais tarde.")
})
public @interface ApiStandardErrors {
}
