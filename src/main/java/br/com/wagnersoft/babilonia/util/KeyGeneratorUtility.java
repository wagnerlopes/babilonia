package br.com.wagnersoft.babilonia.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Gera o par de chaves, privada e pública, de criptografia RSA.
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public class KeyGeneratorUtility {

  private static final Logger LOGGER = LoggerFactory.getLogger(KeyGeneratorUtility.class);

  public static void keyParGenerator(String alias) throws IOException {

    KeyPairGenerator keyGen = null;

    try {
      keyGen = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("{}", e.getMessage());
    }

    keyGen.initialize(2048);
    KeyPair pair = keyGen.generateKeyPair();

    RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
    RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();

    writePemFile(alias + "-private.pem", "PRIVATE KEY", privateKey.getEncoded());
    writePemFile(alias + "-public.pem", "PUBLIC KEY", publicKey.getEncoded());

    // Converter para Base64
    String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

    LOGGER.info("Public Key: {}", publicKeyBase64);
    LOGGER.info("Private Key: {}", privateKeyBase64);
  }

  private static void writePemFile(String filename, String type, byte[] encoded) throws IOException {

    // String formato Base64 com linhas de 64 chars
    String stringBase64 = Base64.getEncoder().encodeToString(encoded).replaceAll("(.{64})", "$1\n");

    String pem = "-----BEGIN " + type + "-----\n" + stringBase64 + "\n-----END " + type + "-----\n";

    try (FileOutputStream fos = new FileOutputStream(filename)) {
      fos.write(pem.getBytes());
    }

  }

  public static void p12Generator(String privatekeyFile, String certificateFile, String alias, String password) throws Exception {

    // Captura path do arquivo de entrada
    Path privatePath = Path.of(privatekeyFile);
    Path parentDir = privatePath.getParent();

    LOGGER.info("Local do arquivo de saída: {}", parentDir);

    if (parentDir == null) {
      parentDir = Path.of(".");
    }

    // Carrega chave privada
    String privatePem = Files.readString(Path.of(privatekeyFile))
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s+", "");

    byte[] privateBytes = Base64.getDecoder().decode(privatePem);

    PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

    // Carrega certificado
    CertificateFactory cf = CertificateFactory.getInstance("X.509");

    Certificate certificate;
    try (var in = Files.newInputStream(Path.of(certificateFile))) {
      certificate = cf.generateCertificate(in);
    }

    // Cria PKCS12
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(null, null);

    ks.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[]{certificate});

    // Gera o caminho de saída na mesma pasta do arquivo de entrada de forma segura
    Path outputP12Path = parentDir.resolve(alias + ".p12");

    try (FileOutputStream fos = new FileOutputStream(outputP12Path.toFile())) {
      ks.store(fos, password.toCharArray());
    }

    LOGGER.info("Arquivo {} criado com sucesso.", outputP12Path.getFileName());
  }

  public static void main(String[] args) {
    try {
      KeyGeneratorUtility.keyParGenerator("wagnersoft");
      //KeyGeneratorUtility.p12Generator("C:/Users/USUARIO/.keys/macedonia.local-key.pem","C:/Users/USUARIO/.keys/macedonia.local.pem", "macedonia", "wl1234");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
