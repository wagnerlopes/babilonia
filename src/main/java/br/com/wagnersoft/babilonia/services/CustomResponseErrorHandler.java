package br.com.wagnersoft.babilonia.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import br.com.wagnersoft.babilonia.exceptions.WSException;
import io.micrometer.core.instrument.util.IOUtils;

/** Manipulador de erro na resposta do servidor WS.
 * @author WagnerSoft
 * @since 0.1
 * @version 0.1
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

  private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return this.errorHandler.hasError(response);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    final String body = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
    final WSException exception = new WSException();
    final Map<String, Object> properties = new HashMap<>();
    properties.put("code", response.getStatusCode().toString());
    properties.put("body", body);
    properties.put("header", response.getHeaders());
    exception.setProperties(properties);
    throw exception;
  }

}
