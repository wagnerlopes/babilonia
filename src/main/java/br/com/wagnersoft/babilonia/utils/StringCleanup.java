package br.com.wagnersoft.babilonia.utils;

import java.text.Normalizer;

public class StringCleanup {

  private StringCleanup() {
  }
  
  public static String cleanAccent(final String str) {
    return str == null ? null : str.trim().toUpperCase()
        .replaceAll("[ãâàáä]", "a")
        .replaceAll("[êèéë]" , "e")
        .replaceAll("[îìíï]" , "i")
        .replaceAll("[õôòóö]", "o")
        .replaceAll("[ûúùü]" , "u")
        .replaceAll("[ÃÂÀÁÄ]", "A")
        .replaceAll("[ÊÈÉË]" , "E")
        .replaceAll("[ÎÌÍÏ]" , "I")
        .replaceAll("[ÕÔÒÓÖ]", "O")
        .replaceAll("[ÛÙÚÜ]" , "U")
        .replace('ç', 'c')
        .replace('Ç', 'C')
        .replace('ñ', 'n')
        .replace('Ñ', 'N');
  }

  public static String cleanAllAccents(String str) {
    if (str == null) {
      return null;
    }
    String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
    return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
  }

}
