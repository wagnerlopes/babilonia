package br.com.wagnersoft.babilonia.exceptions;

import br.com.wagnersoft.babilonia.exceptions.BabiloniaException;

public class NoDataFoundException extends BabiloniaException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 1L;

  public NoDataFoundException() {
  }
  
  public NoDataFoundException(String msg) {
    super(msg);
  }

  public NoDataFoundException(String msg, Throwable e) {
    super(msg, e);
  }

  public NoDataFoundException(Throwable e) {
    super(e);
  }
  
}
