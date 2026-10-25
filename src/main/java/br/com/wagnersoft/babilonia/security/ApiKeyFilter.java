package br.com.wagnersoft.babilonia.security;

import java.io.IOException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
 * Filtro de verificação da API Key de segurança.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "security.apikey")
public class ApiKeyFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "x-api-key";

  private String xApiKey;
  
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }
	  
    String userKey = request.getHeader(API_KEY_HEADER);

    if (xApiKey.equals(userKey)) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("API Key inválida ou ausente");
    }

  }

  public String getxApiKey() {
    return xApiKey;
  }

  public void setxApiKey(String xApiKey) {
    this.xApiKey = xApiKey;
  }
  
}
