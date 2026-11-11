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

  public static void keyParGenerator() throws IOException {

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

    writePemFile("private.pem", "PRIVATE KEY", privateKey.getEncoded());
    writePemFile("public.pem", "PUBLIC KEY", publicKey.getEncoded());

    // Converter para Base64
    String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

    System.out.println("Public Key: " + publicKeyBase64);
    System.out.println("Private Key: " + privateKeyBase64);
  }

  private static void writePemFile(String filename, String type, byte[] encoded) throws IOException {

    String base64 = Base64.getEncoder().encodeToString(encoded);

    String pem = "-----BEGIN " + type + "-----\n"
        + base64.replaceAll("(.{64})", "$1\n") // quebra em linhas de 64 chars
        + "\n-----END " + type + "-----\n";

    try (FileOutputStream fos = new FileOutputStream(filename)) {
      fos.write(pem.getBytes());
    }

  }

  public static void p12Generator(String privatekey, String certFile, String alias) throws Exception {

    // Carrega chave privada
    String pathFile = Path.of(privatekey).getParent().toString();
    String privatePem = Files.readString(Path.of(privatekey))
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s+", "");

    byte[] privateBytes = Base64.getDecoder().decode(privatePem);

    PrivateKey privateKey = KeyFactory.getInstance("RSA")
        .generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

    // Carrega certificado
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate certificate;

    try (var in = Files.newInputStream(Path.of(certFile))) {
      certificate = cf.generateCertificate(in);
    }

    // Cria PKCS12
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(null, null);

    ks.setKeyEntry(
        alias,
        privateKey,
        "wl1234".toCharArray(),
        new Certificate[]{certificate}
        );

    try (FileOutputStream fos = new FileOutputStream(pathFile + alias + ".p12")) {
      ks.store(fos, "wl1234".toCharArray());
    }

    System.out.println("Arquivo PKCS12 criado com sucesso.");
  }

  public static void main(String[] args) {
    try {
      KeyGeneratorUtility.p12Generator("C:/Users/USUARIO/.keys/patagonia.local-key.pem","C:/Users/USUARIO/.keys/patagonia.local.pem", "patagonia");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
