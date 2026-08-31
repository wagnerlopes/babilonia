package br.com.wagnersoft.babilonia.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TipoCategoriaTest {

  @Test
  @DisplayName("Deve gerar TipoCategoria corretamente")
  void testGetById() {
    assertEquals(TipoCategoria.CIDADE, TipoCategoria.getById(1));
    assertNull(TipoCategoria.getById(null));
  }


  @Test
  @DisplayName("Deve gerar erro")
  void testGetByIdError() {
    assertThrows(IllegalArgumentException.class, () -> TipoCategoria.getById(10));
  }

}
