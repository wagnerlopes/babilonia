package br.com.wagnersoft.babilonia.exception;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.wagnersoft.babilonia.model.TipoSituacao;
import br.com.wagnersoft.babilonia.model.dto.ErrorResultDTO;
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
  public ResponseEntity<ErrorResultDTO> handleGeneralException(Exception e, HttpServletRequest request) {
    LOGGER.debug("Erro interno na aplicação: ", e);
    String msg = e.getCause().getMessage();
    return buildErrorResponse(msg, TipoSituacao.ERRO_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(BabiloniaException.class)
  public ResponseEntity<ErrorResultDTO> handleBabilonia(BabiloniaException e, HttpServletRequest request) {
    LOGGER.debug("Requisição inválida: {}", e.getMessage());
    String msg = e.getCause().getMessage();
    return buildErrorResponse(msg, TipoSituacao.REQUISICAO_INVALIDA, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResultDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
    LOGGER.debug("Falha na validação de parâmetro: {}", e.getMessage());
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    return buildErrorResponse(fieldErrors.toString() , TipoSituacao.ERRO_VALIDACAO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoDataFoundException.class)
  public ResponseEntity<ErrorResultDTO> handleNoDataFound(NoDataFoundException e, HttpServletRequest request) {
    LOGGER.debug("Informação não encontrada: {}", e.getMessage());
    String pathInfo = request.getPathInfo();
    return buildErrorResponse(pathInfo, TipoSituacao.NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
  }
  
  /** Captura especificamente tokens que passaram da validade.
   * @param e @link ExpiredJwtException}
   * @param request @code HttpServletRequest}
   * @return {@code ResponseEntity<ErrorResultDTO>}
   */
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResultDTO> handleExpiredJwt(ExpiredJwtException e, HttpServletRequest request) {
    LOGGER.error("Token JWT expirado: {}", e.getMessage());
    String msg = e.getMessage();
    return buildErrorResponse(msg, TipoSituacao.TOKEN_EXPIRADO, HttpStatus.UNAUTHORIZED);
  }

  /** Captura tokens adulterados, assinaturas erradas ou malformados.
   * @param e {@link Exception}
   * @param request {@link HttpServletRequest}
   * @return {@code ResponseEntity<ErrorResultDTO>}
   */
  @ExceptionHandler({MalformedJwtException.class, SignatureException.class, IllegalArgumentException.class})
  public ResponseEntity<ErrorResultDTO> handleInvalidJwt(Exception e, HttpServletRequest request) {
    LOGGER.error("Tentativa de acesso com Token JWT inválido: {}", e.getMessage());
    String msg = e.getMessage();
    return buildErrorResponse(msg, TipoSituacao.TOKEN_INVALIDO, HttpStatus.UNAUTHORIZED);
  }

  private ResponseEntity<ErrorResultDTO> buildErrorResponse(String descricao, TipoSituacao situacao, HttpStatus status) {
    ErrorResultDTO result = ErrorResultDTO.builder()
        .descricao(descricao)
        .consultaData(LocalDate.now())
        .situacaoCodigo(situacao.getCodigo())
        .situacaoDescricao(situacao.getDescricao())
        .build();

    return ResponseEntity.status(status).body(result);
  }

}
