package io.github.lemonsalve.sfxnavigator.utils;

import io.github.lemonsalve.sfxnavigator.routes.Route;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RouteFinder {

  private RouteFinder() {
  }

  public static @Nullable Route findParent(
    final Route route,
    final @NotNull List<Route> allRoutes
  ) {
    for (final Route parentRoute : allRoutes) {
      for (final Route childRoute : parentRoute.childRoutes()) {
        if (childRoute.equals(route)) {
          return parentRoute;
        }
      }
    }

    return null;
  }
}
