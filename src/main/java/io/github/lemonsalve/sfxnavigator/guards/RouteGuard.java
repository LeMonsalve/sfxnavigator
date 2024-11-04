package io.github.lemonsalve.sfxnavigator.guards;

import io.github.lemonsalve.sfxnavigator.NavigationContext;
import io.github.lemonsalve.sfxnavigator.routes.Route;

public record RouteGuard(
  GuardFunction<
    NavigationContext,
    Route,
    Route
    > protect
) {

}
