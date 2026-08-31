package br.com.wagnersoft.babilonia.data;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityReader<T> {

  // Constantes simbólicas para colunas da planilha
  int COL_TIPO = 1;
  int COL_BAIRRO = 2;
  int COL_SUBDISTRITO = 3;
  int COL_DISTRITO = 4;
  int COL_MUNICIPIO = 5;
  int COL_MICRORREGIAO = 6;
  int COL_MESORREGIAO = 7;
  int COL_UF = 8;
  int COL_NIVEL = 9;
  int COL_CATEGORIA_ID = 10;
  int COL_CATEGORIA_DESC = 11;
  int COL_LOCALIDADE = 12;
  int COL_LONGITUDE = 13;
  int COL_LATITUDE = 14;
  int COL_ALTITUDE = 15;

  T makeEntity(Row row);

  String extractUniqueKey(Row row);

  JpaRepository<T, ?> getRepository();

  String getEntityName();

  default String getCellValue(Row row, int cellIndex) {

    if (row == null) return "";

    Cell cell = row.getCell(cellIndex);
    if (cell == null) return "";

    return new DataFormatter().formatCellValue(cell).trim();
  }

}
