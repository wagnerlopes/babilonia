package br.com.wagnersoft.babilonia.exception;

/** Pesquisa sem informacoes.
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
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
