package io.github.lemonsalve.sfxnavigator.routes;

import io.github.lemonsalve.sfxnavigator.guards.DefaultGuards;
import io.github.lemonsalve.sfxnavigator.guards.RouteGuard;
import java.util.ArrayList;
import java.util.List;

public record Route(
  String name,
  String path,
  String title,
  RouteGuard guard,
  RouteConfiguration configuration,
  List<Route> childRoutes
) {

  public Route(
    String name,
    String path,
    String title,
    RouteGuard guard,
    RouteConfiguration configuration
  ) {
    this(name, path, title, guard, configuration, new ArrayList<>());
  }

  public Route(String name, String path, String title, RouteConfiguration configuration) {
    this(name, path, title, DefaultGuards.unprotected(), configuration, new ArrayList<>());
  }

  public boolean hasChildren() {
    return !childRoutes.isEmpty();
  }
}
