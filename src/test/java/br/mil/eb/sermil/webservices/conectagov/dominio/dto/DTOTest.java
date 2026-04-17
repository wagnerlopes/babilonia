package br.mil.eb.sermil.webservices.conectagov.dominio.dto;

import java.io.Serializable;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import br.mil.eb.sermil.webservices.conectagov.dominio.GetterAndSetterTester;

class DTOTest {

  private Set<Class<? extends Serializable>> allClasses;

  private GetterAndSetterTester tester;

  @BeforeAll
  public void setUp() {
    this.tester = new GetterAndSetterTester();
    this.allClasses = new Reflections("br.mil.eb.sermil.webservices.conectagov.dominio.dto").getSubTypesOf(Serializable.class);
  }

  @Test
  void testAllClasses() {
    this.allClasses.forEach(a -> this.tester.testClass(a));
  }

}
