package br.mil.eb.wagnersoft.babilonia.dominio.excpetions;

/** BabiloniaException.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
public class BabiloniaException extends Exception {

  /** serialVersionUID. */
  private static final long serialVersionUID = 1L;

  public BabiloniaException() {
  }
  
  public BabiloniaException(String mensagem) {
    super(mensagem);
  }

  public BabiloniaException(Throwable e) {
    super(e);
  }

  public BabiloniaException(String msg, Throwable e) {
    super(msg, e);
  }
  
}
