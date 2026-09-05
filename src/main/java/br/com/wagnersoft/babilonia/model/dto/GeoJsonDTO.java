package br.com.wagnersoft.babilonia.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoJsonDTO {

  public record Coordinate(Double latitude, Double longitude) { };
  
  public record Properties(String tipo, String nivel, String categoria, String localidade, String bairro, String subdistrito, String distrito, String municipio, String microrregiao, String mesorregiao, String uf) { };

  public record Geometry(String type, List<Double> coordinates) { };

  private String type;

  private Properties properties;

  private Geometry geometry;

}
