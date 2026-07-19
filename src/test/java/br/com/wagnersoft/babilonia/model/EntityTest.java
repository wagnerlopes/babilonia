package br.com.wagnersoft.babilonia.model;

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
    this.allClasses = new Reflections("br.com.wagnersoft.babilonia.model").getSubTypesOf(Serializable.class);
  }

  @Test
  void testAllEntities() {
    this.allClasses.forEach(a -> this.tester.testClass(a));
  }

}
