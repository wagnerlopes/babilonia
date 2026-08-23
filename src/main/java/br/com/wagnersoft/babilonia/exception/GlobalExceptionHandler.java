package br.com.wagnersoft.babilonia.exception;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    String errorMessage = this.buildErrorMessage("Erro inesperado no servidor (HTTP 500)", e);

    return buildErrorResponse(errorMessage, TipoSituacao.ERRO_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BabiloniaException.class)
  public ResponseEntity<ErrorResultDTO> handleBabilonia(BabiloniaException e, HttpServletRequest request) {

    String errorMessage = this.buildErrorMessage("Requisição inválida (HTTP 400)", e);

    return buildErrorResponse(errorMessage, TipoSituacao.REQUISICAO_INVALIDA, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoDataFoundException.class)
  public ResponseEntity<ErrorResultDTO> handleNoDataFound(NoDataFoundException e, HttpServletRequest request) {

    String errorMessage = this.buildErrorMessage("Informação não encontrada (HTTP 404)", e);

    return buildErrorResponse(errorMessage, TipoSituacao.NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResultDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {

    String errorMessage = this.buildErrorMessage("Erro de validação de parâmetro (HTTP 406)", e);

    return buildErrorResponse(errorMessage , TipoSituacao.ERRO_VALIDACAO, HttpStatus.NOT_ACCEPTABLE);
  }

  /** Captura especificamente tokens que passaram da validade.
   * @param e @link ExpiredJwtException}
   * @param request @code HttpServletRequest}
   * @return {@code ResponseEntity<ErrorResultDTO>}
   */
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResultDTO> handleExpiredJwt(ExpiredJwtException e, HttpServletRequest request) {

    String errorMessage = this.buildErrorMessage("Token JWT expirado (HTTP 401)", e);

    return buildErrorResponse(errorMessage, TipoSituacao.TOKEN_EXPIRADO, HttpStatus.UNAUTHORIZED);
  }

  /** Captura tokens adulterados, assinaturas erradas ou malformados.
   * @param e {@link Exception}
   * @param request {@link HttpServletRequest}
   * @return {@code ResponseEntity<ErrorResultDTO>}
   */
  @ExceptionHandler({MalformedJwtException.class, SignatureException.class, IllegalArgumentException.class})
  public ResponseEntity<ErrorResultDTO> handleInvalidJwt(Exception e, HttpServletRequest request) {

    String errorMessage = this.buildErrorMessage("Tentativa de acesso com Token JWT inválido (HTTP 401): {}", e);

    return buildErrorResponse(errorMessage, TipoSituacao.TOKEN_INVALIDO, HttpStatus.UNAUTHORIZED);
  }

  private String buildErrorMessage(String mensagem, Exception e) {

    String mensagemErro = (e.getCause() != null && e.getCause().getMessage() != null) ? e.getCause().getMessage() : e.getMessage();

    if (mensagemErro == null) {
      mensagemErro = mensagem;
    }

    LOGGER.debug("{}", e);

    return mensagemErro;
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
