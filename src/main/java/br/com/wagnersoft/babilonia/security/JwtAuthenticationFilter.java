package br.com.wagnersoft.babilonia.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  
  private final JwtUtilities jwtUtilities;

  public JwtAuthenticationFilter(JwtUtilities jwtUtilities) {
    this.jwtUtilities = jwtUtilities;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      // Aqui o parseToken pode lançar ExpiredJwtException, MalformedJwtException, SignatureException
      Claims claims = jwtUtilities.parseToken(token);

      LOGGER.info("Token Subject: {}", claims.getSubject());
      
      List<String> roles = claims.get("roles", List.class);

      LOGGER.info("Token Roles: {}", roles);
      
      // Se chegou até aqui, o token é válido
      // Você pode popular o contexto de segurança com o usuário
      //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, List.of());
      //SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
