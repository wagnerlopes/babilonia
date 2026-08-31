package br.com.wagnersoft.babilonia.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.Serializable;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.reflections.Reflections;

import br.com.wagnersoft.babilonia.model.dto.ErrorResultDTO;
import br.com.wagnersoft.babilonia.model.dto.LocalidadeDTO;

@TestInstance(Lifecycle.PER_CLASS)
class DtoTest {

  private Set<Class<? extends Serializable>> allClasses;

  private GetterAndSetterTester tester;

  @BeforeAll
  public void setUp() {
    this.tester = new GetterAndSetterTester();
    this.allClasses = new Reflections("br.com.wagnersoft.babilonia.model.dto").getSubTypesOf(Serializable.class);
  }

  @Test
  void testAllClasses() {
    this.allClasses.forEach(a -> this.tester.testClass(a));
  }

  @Test
  void testDtoEmpty() {
    assertNotNull(ErrorResultDTO.empty());
    assertNotNull(LocalidadeDTO.empty());
  }
  
}
