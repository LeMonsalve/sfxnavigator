package io.github.lemonsalve.sfxnavigator.configurations;

import io.github.lemonsalve.sfxnavigator.configurators.RoutesContextConfigurator;
import io.github.lemonsalve.sfxnavigator.routes.Route;
import java.util.List;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultRoutesConfiguration implements RoutesContextConfigurator {

  @Override
  public List<Route> setupRoutes() {
    return List.of();
  }
}
