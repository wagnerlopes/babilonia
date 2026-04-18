package br.com.wagnersoft.babilonia.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateHelper {

  private DateHelper() {
    super();
  }
  
  public static Date asDate(final LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date asDate(final LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate asLocalDate(final Date data) {
    return Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDateTime asLocalDateTime(final Date data) {
    return Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

	public static Date asDate(final String data, final String format) {
    return asDate(asLocalDate(data, format));
	}

	public static LocalDate asLocalDate(final String data, final String format) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern(format));
	}

  public static String asFormatDate(final Date data, final String format) {
    return data == null ? "" : asLocalDate(data).format(DateTimeFormatter.ofPattern(format));
  }

}
