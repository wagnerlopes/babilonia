package br.com.wagnersoft.babilonia.dominio.dto;

import java.io.Serializable;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import br.com.wagnersoft.babilonia.dominio.GetterAndSetterTester;

@Disabled("Feature not yet implemented")
class DTOTest {

  private Set<Class<? extends Serializable>> allClasses;

  private GetterAndSetterTester tester;

  @BeforeAll
  public void setUp() {
    this.tester = new GetterAndSetterTester();
    this.allClasses = new Reflections("br.com.wagnersoft.babilonia.dominio.dto").getSubTypesOf(Serializable.class);
  }

  @Test
  void testAllClasses() {
    this.allClasses.forEach(a -> this.tester.testClass(a));
  }

}
