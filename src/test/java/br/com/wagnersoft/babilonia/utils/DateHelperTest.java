package br.com.wagnersoft.babilonia.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class DateHelperTest {

  private Date date;
  
  private LocalDate localDate;

  private LocalDateTime localDateTime;
  
  @BeforeAll
  public void setUp() throws ParseException {
    this.date = DateFormat.getDateInstance(DateFormat.SHORT).parse("21/03/70");
    this.localDate = LocalDate.of(1970, 3, 21);
    this.localDateTime = LocalDateTime.of(1970, 3, 21, 0, 0, 0);
  }
  
  @Test
  void localDateAsDate() {
    final Date in = DateHelper.asDate(localDate);
    assertEquals(date, in);
  }

  @Test
  void localDateTimeAsDate() {
    final Date in = DateHelper.asDate(localDateTime);
    assertEquals(date, in);
  }

  @Test
  void stringAsDate() {
    final Date in = DateHelper.asDate("21/03/1970","dd/MM/yyyy");
    assertEquals(date, in);
  }

  @Test
  void dateAsFormatDate() {
    final String in = DateHelper.asFormatDate(date,"dd/MM/yyyy");
    assertEquals("21/03/1970", in);
  }

  @Test
  void dateAsLocalDate() {
    final LocalDate in = DateHelper.asLocalDate(date);
    assertEquals(localDate, in);
  }

  @Test
  void stringAsLocalDate() {
    final LocalDate in = DateHelper.asLocalDate("21/03/1970","dd/MM/yyyy");
    assertEquals(localDate, in);
  }
  
  @Test
  void dateAsLocalDateTime() {
    final LocalDateTime in = DateHelper.asLocalDateTime(date);
    assertEquals(localDateTime, in);
  }
  
}