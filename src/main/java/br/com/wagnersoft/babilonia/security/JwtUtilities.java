package br.com.wagnersoft.babilonia.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

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
@Component
public class JwtUtilities {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtilities.class);

  private final PrivateKey parsedPrivateKey;
  
  private final PublicKey parsedPublicKey;

  private final String token;

  public JwtUtilities(JwtProperties props) {
    this.token = props.getToken();
    this.parsedPublicKey = loadPublicKey();
    this.parsedPrivateKey = loadPrivateKey();
    LOGGER.info("{}", token);
    LOGGER.info("Private = {} {} {}", parsedPrivateKey.getAlgorithm(), parsedPrivateKey.getFormat(), parsedPrivateKey.getEncoded());
    LOGGER.info("Public = {}", parsedPublicKey);
  }

  public boolean isValid(String token) {
    return token != null && token.equals(this.token);
  }

  public Claims parseToken(String token) {

      Jws<Claims> jws = Jwts.parser()
          .verifyWith(loadPublicKey())
          .build()
          .parseSignedClaims(token);

      return jws.getPayload();

  }

  public String newToken() {
    String jwt = "N/D";
    try {
      PrivateKey privateKey = loadPrivateKey();
      jwt = Jwts.builder()
          .subject("babilonia")
          .issuer("WagnerSoft")
          .issuedAt(new Date())
          .expiration(new Date(System.currentTimeMillis() + 315_360_000))
          .claim("roles", List.of("ADMIN", "USER"))
          .signWith(privateKey)
          .compact();
    } catch (Exception e) {
      LOGGER.error("Erro gerando token JWT", e);
    }
    return jwt;
  }

  private PrivateKey loadPrivateKey() {
    ClassPathResource resource = new ClassPathResource("wagnersoft.p12");
    try (InputStream is = resource.getInputStream()) {
      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(is, "wl1234".toCharArray());
      String alias = "wagnersoft";
      return (PrivateKey) ks.getKey(alias, "wl1234".toCharArray());
    } catch (Exception e) {
      LOGGER.error("Erro ao carregar chave privada do keystore", e);
      throw new RuntimeException("Falha ao carregar chave privada", e);
    }
  }

  private PublicKey loadPublicKey() {
    ClassPathResource resource = new ClassPathResource("wagnersoft.p12");
    try (InputStream is = resource.getInputStream()) {
      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(is, "wl1234".toCharArray());
      String alias = "wagnersoft";
      Certificate cert = ks.getCertificate(alias);
      if (cert == null) {
        throw new RuntimeException("Certificado não encontrado para alias: " + alias);
      }
      return cert.getPublicKey();
    } catch (Exception e) {
      LOGGER.error("Erro ao carregar chave pública do keystore", e);
      throw new RuntimeException("Falha ao carregar chave pública", e);
    }
  }


  /** Exemplo de carregamento de chave pública no formato PEM
   * @param publicKeyPem formato string
   * @return PublicKey
   */
  @SuppressWarnings("unused")
  private static PublicKey parsePublicKey(String publicKeyPem) {
    try {
      String sanitized = publicKeyPem
          .replace("-----BEGIN PUBLIC KEY-----", "")
          .replace("-----END PUBLIC KEY-----", "")
          .replaceAll("\\s+", "");
      byte[] decode = Base64.getDecoder().decode(sanitized);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (Exception e) {
      LOGGER.error("Erro crítico: Não foi possível carregar a chave pública RSA configurada.", e);
      throw new IllegalStateException("Falha catastrófica ao inicializar chaves de segurança da API.", e);
    }
  }  

}
