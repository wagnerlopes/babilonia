package br.com.wagnersoft.babilonia.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "x-api-key";
  
  private static final String EXPECTED_API_KEY = "3f6988fd-feff-477a-9772-c2ceefda3b39";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String apiKey = request.getHeader(API_KEY_HEADER);

    if (EXPECTED_API_KEY.equals(apiKey)) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("API Key inválida ou ausente");
    }
    
  }

}
