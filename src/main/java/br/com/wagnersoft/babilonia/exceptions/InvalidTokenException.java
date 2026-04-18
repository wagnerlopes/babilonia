package br.com.wagnersoft.babilonia.exceptions;

import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;

public class InvalidTokenException extends BabiloniaException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 1L;

  public InvalidTokenException() {
  }
  
  public InvalidTokenException(String msg) {
    super(msg);
  }

  public InvalidTokenException(String msg, Throwable e) {
    super(msg, e);
  }

  public InvalidTokenException(Throwable e) {
    super(e);
  }
  
}
