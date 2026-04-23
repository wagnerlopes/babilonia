package br.com.wagnersoft.babilonia.exceptions;

public class InvalidTokenException extends BabiloniaException {

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
