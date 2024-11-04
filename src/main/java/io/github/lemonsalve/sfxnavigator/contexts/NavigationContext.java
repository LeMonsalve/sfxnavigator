package io.github.lemonsalve.sfxnavigator.contexts;

import io.github.lemonsalve.sfxnavigator.routes.RouteHistory;

public record NavigationContext(
  ApplicationContext appContext,
  RoutesContext routesContext,
  RouteHistory routeHistory
) {

}
