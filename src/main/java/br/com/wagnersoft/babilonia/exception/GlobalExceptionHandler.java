package br.com.wagnersoft.babilonia.exception;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.wagnersoft.babilonia.dto.WSResultDTO;
import br.com.wagnersoft.babilonia.model.SituacaoEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

/**
 *  Central global de tratamento das exceções lançadas pela API.
 *  
 * @author Wagner Lopes
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<WSResultDTO> handleGeneralException(Exception e, HttpServletRequest request) {
    LOGGER.debug("Erro interno no servidor: ", e);
    String cpf = request.getParameter("cpf");
    return buildErrorResponse(cpf, SituacaoEnum.FORA_AR, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(BabiloniaException.class)
  public ResponseEntity<WSResultDTO> handleBabilonia(BabiloniaException e, HttpServletRequest request) {
    LOGGER.debug("Erro na API Babilonia: {}", e.getMessage());
    String cpf = request.getParameter("cpf");
    return buildErrorResponse(cpf, SituacaoEnum.REQUISICAO_INVALIDA, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<WSResultDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
    LOGGER.debug("Falha na validação dos campos da requisição: {}", e.getMessage());
    String cpf = request.getParameter("cpf");
    
    // Se preferir tentar pegar o CPF de dentro do DTO que falhou (opcional):
    // if (e.getBindingResult().getTarget() instanceof CidadaoConsultDTO) {
    //     cpf = ((CidadaoConsultDTO) e.getBindingResult().getTarget()).getCpf();
    // }

    return buildErrorResponse(cpf, SituacaoEnum.REQUISICAO_INVALIDA, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoDataFoundException.class)
  public ResponseEntity<WSResultDTO> handleNoDataFound(NoDataFoundException e, HttpServletRequest request) {
    LOGGER.debug("Cidadão não encontrado: {}", e.getMessage());
    String cpf = request.getParameter("cpf"); 
    return buildErrorResponse(cpf, SituacaoEnum.NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
  }
  
  /** Captura especificamente tokens que passaram da validade.
   * @param e @link ExpiredJwtException}
   * @param request @code HttpServletRequest}
   * @return {@code ResponseEntity<WSResultDTO>}
   */
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<WSResultDTO> handleExpiredJwt(ExpiredJwtException e, HttpServletRequest request) {
    LOGGER.error("Token JWT expirado: {}", e.getMessage());
    String cpf = request.getParameter("cpf");
    return buildErrorResponse(cpf, SituacaoEnum.TOKEN_EXPIRADO, HttpStatus.UNAUTHORIZED);
  }

  /** Captura tokens adulterados, assinaturas erradas ou malformados.
   * @param e {@link Exception}
   * @param request {@link HttpServletRequest}
   * @return {@code ResponseEntity<WSResultDTO>}
   */
  @ExceptionHandler({MalformedJwtException.class, SignatureException.class, IllegalArgumentException.class})
  public ResponseEntity<WSResultDTO> handleInvalidJwt(Exception e, HttpServletRequest request) {
    LOGGER.error("Tentativa de acesso com Token JWT inválido: {}", e.getMessage());
    String cpf = request.getParameter("cpf");
    return buildErrorResponse(cpf, SituacaoEnum.TOKEN_INVALIDO, HttpStatus.UNAUTHORIZED);
  }

  private ResponseEntity<WSResultDTO> buildErrorResponse(String cpf, SituacaoEnum situacao, HttpStatus status) {
    WSResultDTO result = WSResultDTO.builder()
        .cpf(cpf)
        .consultaData(LocalDate.now())
        .situacaoCodigo(situacao.getCodigo())
        .situacaoDescricao(situacao.getDescricao())
        .build();

    return ResponseEntity.status(status).body(result);
  }

}
