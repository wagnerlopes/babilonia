package br.com.wagnersoft.babilonia.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 
 * Coordenada geográfica.
 * 
 * <p>Coordenadas geográficas (latitude, longitude e altitude) de uma {@link Localidade localidade}.
 * <p>Permite calcular a distância de uma outra localidade com a fórmula de Haversine.</p>
 * 
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Coordenada {

  @DecimalMin(value = "-90.0", message = "A latitude deve ser maior ou igual a -90.0")
  @DecimalMax(value = "90.0", message = "A latitude deve ser menor ou igual a 90.0")
  private Double latitude;

  @DecimalMin(value = "-180.0", message = "A longitude deve ser maior ou igual a -180.0")
  @DecimalMax(value = "180.0", message = "A longitude deve ser menor ou igual a 180.0")
  private Double longitude;

  private Double altitude;   // metros

  /**
   * Calcula a distância entre esta coordenada e outra usando a fórmula de Haversine.
   * @param outra Coordenada destino
   * @return distância em metros
   */
  public double distancia(Coordenada outra) {

    final int RAIO_TERRA = 6371000; // em metros

    double lat1Rad = Math.toRadians(this.latitude);
    double lat2Rad = Math.toRadians(outra.latitude);
    double deltaLat = Math.toRadians(outra.latitude - this.latitude);
    double deltaLon = Math.toRadians(outra.longitude - this.longitude);

    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
        Math.cos(lat1Rad) * Math.cos(lat2Rad) *
        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    double distanciaSuperficie = RAIO_TERRA * c;

    // Considera diferença de altitude
    double deltaAlt = outra.altitude - this.altitude;

    return Math.sqrt(distanciaSuperficie * distanciaSuperficie + deltaAlt * deltaAlt);
  }

  @Override
  public String toString() {
    return String.format("Lat: %.6f, Lon: %.6f, Alt: %.2f m", latitude, longitude, altitude);
  }

}
