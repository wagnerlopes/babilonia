package br.com.wagnersoft.babilonia.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyGeneratorUtility {

  private static final Logger LOGGER = LoggerFactory.getLogger(KeyGeneratorUtility.class);

  private static void keyParGenerator() throws IOException {

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

  public static void main(String[] args) {
    try {
      KeyGeneratorUtility.keyParGenerator();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
