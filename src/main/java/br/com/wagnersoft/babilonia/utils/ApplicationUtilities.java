package br.com.wagnersoft.babilonia.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Locale;

/** App Utilities.
 * @since 1.0
 * @version 1.0
 * @author Wagner Lopes
 */
public class ApplicationUtilities {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUtilities.class);

  private ApplicationUtilities() { /* utility class */ }

  /** PDF first page image.
   * @param byteArray
   * @return
   * @throws IOException
   */
  public static String pdfFirstPageToPngDataUrl(byte[] byteArray) throws IOException {
    try (PDDocument doc = Loader.loadPDF(byteArray)) {
      final PDFRenderer pdfRenderer = new PDFRenderer(doc);
      final BufferedImage img = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        ImageIO.write(img, "png", bos);
        String base64 = Base64.getEncoder().encodeToString(bos.toByteArray());
        String imagem = "data:image/png;base64," + base64;
        LOGGER.debug("{}", imagem);
        return imagem;
      }
    }
  }

  /** Clean string from diacritical marks.
   * @param dirty
   * @return clean string
   */
  public static String cleanAccent(String dirty) {
    if (dirty == null) return null;
    String clean = Normalizer.normalize(dirty, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    LOGGER.debug("{} => {}", dirty, clean);
    return clean.trim().toUpperCase(Locale.getDefault());
  }
  
}
