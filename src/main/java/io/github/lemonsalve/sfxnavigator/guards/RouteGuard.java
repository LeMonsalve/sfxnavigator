package io.github.lemonsalve.sfxnavigator.guards;

import io.github.lemonsalve.sfxnavigator.contexts.NavigationContext;
import io.github.lemonsalve.sfxnavigator.routes.Route;

public record RouteGuard(
  GuardFunction<
    NavigationContext,
    Route,
    Route
    > protect
) {

}
