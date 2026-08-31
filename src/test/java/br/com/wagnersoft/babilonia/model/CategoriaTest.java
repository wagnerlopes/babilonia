package br.com.wagnersoft.babilonia.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoriaTest {

  Categoria categoria = new Categoria();

  @BeforeEach
  void setUp() {
    categoria.setId("1234");
    categoria.setDescricao(TipoCategoria.ALDEIA_INDIGENA);
  }

  @Test
  @DisplayName("Deve gerar getter e setter corretamente")
  void testGettersAndSetters() {
    assertEquals("1234", categoria.getId());
    assertEquals(TipoCategoria.ALDEIA_INDIGENA, categoria.getDescricao());
  }

  @Test
  @DisplayName("Deve respeitar Equals e Hashcode no codigo")
  void testEqualsAndHashCode() {

    // Arrange
    Categoria igual = new Categoria();
    igual.setId("1234");

    Categoria diferente = new Categoria();
    diferente.setId("1");

    // Teste de igualdade (mesmo codigo)
    assertTrue(categoria.equals(categoria));
    assertTrue(categoria.equals(igual));
    assertEquals(categoria.hashCode(), igual.hashCode());

    // Teste de diferença (codigos diferentes)
    assertFalse(categoria.equals(diferente));
    assertNotEquals(categoria.hashCode(), diferente.hashCode());

    // Limite: nulo e classes diferentes
    assertFalse(categoria.equals(null));
    assertNotEquals(categoria, "Uma String qualquer");

  }

}
