package br.com.wagnersoft.babilonia.exception;

/** BabiloniaException gerando erro HTTP 400 Bad Request.
 * @author Wagner Lopes
 * @since 1.0.0
 * @version 1.0.0
 */
public class BabiloniaException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public BabiloniaException(String mensagem) {
    super(mensagem);
  }

  public BabiloniaException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

}
