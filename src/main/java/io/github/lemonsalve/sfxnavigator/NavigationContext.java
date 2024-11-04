package io.github.lemonsalve.sfxnavigator;

import io.github.lemonsalve.sfxnavigator.routes.RouteHistory;
import org.springframework.context.ConfigurableApplicationContext;

public record NavigationContext(
  ConfigurableApplicationContext springContext,
  RoutesContext routesContext,
  RouteHistory routeHistory
) {

}
