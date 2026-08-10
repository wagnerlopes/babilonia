package br.com.wagnersoft.babilonia.util;

public class Coordenada {

  private double latitude;   // graus, -90 a +90

  private double longitude;  // graus, -180 a +180

  private double altitude;   // metros

  public Coordenada(double latitude, double longitude, double altitude) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getAltitude() {
    return altitude;
  }

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
