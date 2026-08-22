package br.com.wagnersoft.babilonia.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerHealthIndicator implements HealthIndicator {

  private boolean initialized = false;

  public void markInitialized() {
    this.initialized = true;
  }

  @Override
  public Health health() {
    if (initialized) {
      return Health.up()
          .withDetail("dataInitializer", "Dados carregados com sucesso")
          .build();
    } else {
      return Health.down()
          .withDetail("dataInitializer", "Carregamento ainda não concluído")
          .build();
    }
  }
}
