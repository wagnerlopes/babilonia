package br.com.wagnersoft.babilonia.util;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

/** 
 * JWT Utilities para manipulação de tokens.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public class JwtUtilities {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtilities.class);

  private static final String TOKEN_JWT = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxMTM0MTMifQ.N0rtJSqT18QLTL-c77tSfYEBwGXAxln_uCsVphWUB1l9p3cZYG60SREiliKVHLlb5TfFaYFeoXPWLMLu9lZrUXBAlD5jjBfuZDlTUjRokB-sdGkojeaXc-PFGbSUlJQ3uAJoZyFOtQpf00AwaaQYvijVHm6hs52CsKBvIDylAIqq4nrr3quc6C6N5KL15SNQJqRafjrLTA5DDVLHADOmWPArVT6-MNO4IXp-Ry-b_pz5tfFDqOW2OKAtOA873cwTvjkhrngSIwy7r7sO8ho6gee0RMiQy35LlD55OnllbdENFj8DGvajMH0H4AJYZXcB05h3FogMDrgx8zAWATfTQw";

  private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtJjGu4aeWy0u8Ek3jAklXhSyFb8SzkvUxTg3kB4WoGeORZGMv4bFraqRRMiPkg0EQIhZNmg6M1h1ZQ7t4l0bUqeYfGFw+Ujv9i+DU5L3roGJkWui6uI1edsJoRENqR3/Y45AE2Gwd/WroQVzUw1nrXAKczZv8/rrn/owSN8oTjxnFvdRNS2/rIckEcgPHlqvJeuh1oxSUuXQrJZUiljkiUJ8GDB/hSYVrnB11ioDx4NYMFSvhIXf7NC9nE8mghTVOUYGqM4CUBRaYs+VdBsYiRlIZamK0jChEXgPxgz0MYAlFQZ5Z8hxv3gIC2eXn0GH19R7f9MPOhjuz8EkyN4FFwIDAKAC";

  private static final RSAPublicKey PARSED_PUBLIC_KEY = parsePublicKey();

  private JwtUtilities() { /* Utility class */ }

  public static boolean isValid(String token) {
    return token != null && token.equals(TOKEN_JWT);
  }

  public static String parseToken(String token) {
    Jws<Claims> jws = Jwts.parser()
        .verifyWith(PARSED_PUBLIC_KEY)
        .build()
        .parseSignedClaims(token);

    return jws.getPayload().getSubject();
  }

  private static RSAPublicKey parsePublicKey() {
    try {
      byte[] decode = Base64.getDecoder().decode(PUBLIC_KEY);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (Exception e) {
      LOGGER.error("Erro crítico: Não foi possível carregar a chave pública RSA configurada.", e);
      throw new IllegalStateException("Falha catastrófica ao inicializar chaves de segurança da API.", e);
    }
  }  

}
