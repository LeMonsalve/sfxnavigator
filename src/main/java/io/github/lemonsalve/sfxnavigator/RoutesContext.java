package io.github.lemonsalve.sfxnavigator;

import io.github.lemonsalve.sfxnavigator.configurators.RoutesContextConfigurator;
import io.github.lemonsalve.sfxnavigator.exceptions.RouteNotFoundException;
import io.github.lemonsalve.sfxnavigator.routes.Route;
import io.github.lemonsalve.sfxnavigator.utils.RouteFinder;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RoutesContext {

  private final List<Route> routes;

  public RoutesContext(final RoutesContextConfigurator configurator) {
    this.routes = configurator.setupRoutes();
  }

  public Route findRoute(final @NotNull String routeName) {
    return routes.stream()
      .map(route -> findRouteRecursive(route, routeName))
      .filter(Objects::nonNull)
      .findFirst()
      .orElseThrow(() -> new RouteNotFoundException("Route not found: " + routeName));
  }

  private @Nullable Route findRouteRecursive(final @NotNull Route route, final String routeName) {
    if (route.name().equals(routeName)) {
      return route;
    }

    for (final Route childRoute : route.childRoutes()) {
      final Route found = findRouteRecursive(childRoute, routeName);

      if (found != null) {
        return found;
      }
    }

    return null;
  }

  public Route findParentRoute(final String routeName) {
    final Route route = findRoute(routeName);
    return RouteFinder.findParent(route, routes);
  }

  public boolean hasParentRoute(final String routeName) {
    return findParentRoute(routeName) != null;
  }
}
