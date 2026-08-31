package br.com.wagnersoft.babilonia.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistritoTest {

  Distrito x = new Distrito();

  @BeforeEach
  void setUp() {
    x.setId(1);
    x.setDescricao("XXX");
  }

  @Test
  @DisplayName("Deve gerar getter e setter corretamente")
  void testGettersAndSetters() {
    assertEquals(1, x.getId());
    assertEquals("XXX", x.getDescricao());
  }

  @Test
  @DisplayName("Deve respeitar Equals e Hashcode no codigo")
  void testEqualsAndHashCode() {

    // Arrange
    Distrito igual = new Distrito();
    igual.setId(1);

    Distrito diferente = new Distrito();
    diferente.setId(2);

    // Teste de igualdade (mesmo codigo)
    assertTrue(x.equals(x));
    assertTrue(x.equals(igual));
    assertEquals(x.hashCode(), igual.hashCode());

    // Teste de diferença (codigos diferentes)
    assertFalse(x.equals(diferente));
    assertNotEquals(x.hashCode(), diferente.hashCode());

    // Limite: nulo e classes diferentes
    assertFalse(x.equals(null));
    assertNotEquals(x, "Uma String qualquer");

  }

}
