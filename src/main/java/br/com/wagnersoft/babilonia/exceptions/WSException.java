package br.mil.eb.sermil.webservices.babilonia.exceptions;

import java.io.IOException;
import java.util.Map;

public class WSException extends IOException {

  private static final long serialVersionUID = 1L;

  private transient Map<String, Object> properties;
  
  public WSException() {
  }

  public WSException(String msg) {
    super(msg);
  }

  public WSException(String msg, Throwable e) {
    super(msg, e);
  }
  
  public Map<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }
  
}
