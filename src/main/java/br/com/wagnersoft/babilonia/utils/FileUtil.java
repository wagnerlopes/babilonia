package br.com.wagnersoft.babilonia.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

import jakarta.xml.bind.DatatypeConverter;

public class FileUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

  private FileUtil() {}
  
  public static String getImgString(final byte[] byteArray) throws IOException{
    File file = new File("tempFile");
    FileUtils.writeByteArrayToFile(file, byteArray);
    PDDocument docPdf = Loader.loadPDF(file);
    final PDFRenderer pdfRenderer = new PDFRenderer(docPdf);
    final BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
    try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      ImageIO.write(bim, "png", bos);
      String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(bos.toByteArray());
      LOGGER.debug("{}", imagem);
      return imagem;
    }
  }

}
