package br.com.wagnersoft.babilonia.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoordenadaTest {

  Coordenada c = new Coordenada();

  @BeforeEach
  void setUp() {
    c.setAltitude(20.3453);
    c.setLatitude(-12.3124);
    c.setLongitude(43.8923);
  }

  @Test
  @DisplayName("Deve gerar getter e setter corretamente")
  void testGettersAndSetters() {
    assertEquals(20.3453, c.getAltitude());
    assertEquals(-12.3124, c.getLatitude());
    assertEquals(43.8923, c.getLongitude());
  }

  @Test
  @DisplayName("Deve respeitar Equals e Hashcode no codigo")
  void testEqualsAndHashCode() {

    // Arrange
    Coordenada igual = new Coordenada();
    igual.setAltitude(20.3453);
    igual.setLatitude(-12.3124);
    igual.setLongitude(43.8923);

    Coordenada diferente = new Coordenada();
    diferente.setAltitude(1.3);
    diferente.setLatitude(1.3);
    diferente.setLongitude(1.3);

    // Teste de igualdade (mesmo codigo)
    assertTrue(c.equals(c));
    assertTrue(c.equals(igual));
    assertEquals(c.hashCode(), igual.hashCode());

    // Teste de diferença (codigos diferentes)
    assertFalse(c.equals(diferente));
    assertNotEquals(c.hashCode(), diferente.hashCode());

    // Limite: nulo e classes diferentes
    assertFalse(c.equals(null));
    assertNotEquals(c, "Uma String qualquer");

  }

  @Test
  @DisplayName("Deve calcular distancia corretamente")
  void testDistancia() {
    Coordenada igual = new Coordenada();
    igual.setAltitude(20.3453);
    igual.setLatitude(-12.3124);
    igual.setLongitude(43.8923);
    
    assertEquals(0, c.distancia(igual));
  }

  @Test
  @DisplayName("Deve gerar string corretamente")
  void testToString() {
    assertNotEquals("", c.toString());
  }
  
}
