package br.com.wagnersoft.babilonia.exception;

/**
 * Resultado de pesquisa sem informações.
 * 
 * <p>Esta exceção está associada ao status http 404 na API.</p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public class NoDataFoundException extends BabiloniaException {

  private static final long serialVersionUID = 1L;

  public NoDataFoundException(String msg) {
    super(msg);
  }

  public NoDataFoundException(String msg, Throwable e) {
    super(msg, e);
  }

}
