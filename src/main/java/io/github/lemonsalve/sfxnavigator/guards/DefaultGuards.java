package io.github.lemonsalve.sfxnavigator.guards;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class DefaultGuards {

  private DefaultGuards() {
  }

  @Contract(" -> new")
  public static @NotNull RouteGuard unprotected() {
    return new RouteGuard(
      (_, nextRoute) -> nextRoute
    );
  }
}
