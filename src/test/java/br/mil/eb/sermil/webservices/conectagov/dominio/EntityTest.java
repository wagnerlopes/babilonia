package br.mil.eb.sermil.webservices.conectagov.dominio;

import java.io.Serializable;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.reflections.Reflections;

class EntityTest {

  private Set<Class<? extends Serializable>> allClasses;

  private GetterAndSetterTester tester;

  @BeforeEach
  public void setUp() {
    this.tester = new GetterAndSetterTester();
    this.allClasses = new Reflections("br.mil.eb.sermil.webservices.conectagov.dominio").getSubTypesOf(Serializable.class);
  }

  @Test
  void testAllEntities() {
    this.allClasses.forEach(a -> this.tester.testClass(a));
  }

}
