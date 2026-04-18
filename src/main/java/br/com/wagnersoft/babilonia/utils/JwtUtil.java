package br.com.wagnersoft.babilonia.utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.wagnersoft.babilonia.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
  
  private static final String TESTE_JWT = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxMTM0MTMifQ.N0rtJSqT18QLTL-c77tSfYEBwGXAxln_uCsVphWUB1l9p3cZYG60SREiliKVHLlb5TfFaYFeoXPWLMLu9lZrUXBAlD5jjBfuZDlTUjRokB-sdGkojeaXc-PFGbSUlJQ3uAJoZyFOtQpf00AwaaQYvijVHm6hs52CsKBvIDylAIqq4nrr3quc6C6N5KL15SNQJqRafjrLTA5DDVLHADOmWPArVT6-MNO4IXp-Ry-b_pz5tfFDqOW2OKAtOA873cwTvjkhrngSIwy7r7sO8ho6gee0RMiQy35LlD55OnllbdENFj8DGvajMH0H4AJYZXcB05h3FogMDrgx8zAWATfTQw";
  
  private static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtJjGu4aeWy0u8Ek3jAklXhSyFb8SzkvUxTg3kB4WoGeORZGMv4bFraqRRMiPkg0EQIhZNmg6M1h1ZQ7t4l0bUqeYfGFw+Ujv9i+DU5L3roGJkWui6uI1edsJoRENqR3/Y45AE2Gwd/WroQVzUw1nrXAKczZv8/rrn/owSN8oTjxnFvdRNS2/rIckEcgPHlqvJeuh1oxSUuXQrJZUiljkiUJ8GDB/hSYVrnB11ioDx4NYMFSvhIXf7NC9nE8mghTVOUYGqM4CUBRaYs+VdBsYiRlIZamK0jChEXgPxgz0MYAlFQZ5Z8hxv3gIC2eXn0GH19R7f9MPOhjuz8EkyN4FFwIDAKAC";
  
  JwtUtil() {
    // Construtor protegido
  }

  public static boolean tokenValid(String token, boolean teste) {
    return teste ? TESTE_JWT.equals(token) : TESTE_JWT.equals(token);
  }
  
  public static String parseToken(final String token) throws InvalidTokenException {
    try {
      final Claims claims = Jwts.parser().verifyWith(getParsedPublicKey()).build().parseSignedClaims(token).getPayload();
      return claims.getSubject();
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
      throw new InvalidTokenException("Token JWT inválido.", e);
    }
  }

  private static RSAPublicKey getParsedPublicKey() throws InvalidTokenException {
    try {
      byte[] decode = Base64.getDecoder().decode(PUBLIC_KEY);
      final X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(decode);
      final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      final RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpecX509);
      LOGGER.debug("{}", pubKey);
      return pubKey;
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new InvalidTokenException("A chave pública informada não é válida.", e);
    }
  }

}
