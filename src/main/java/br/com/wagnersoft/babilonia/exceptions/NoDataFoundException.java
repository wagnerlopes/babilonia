package br.com.sermil.webservices.babilonia.exceptions;

public class NoDataFoundException extends ConectagovException {

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
