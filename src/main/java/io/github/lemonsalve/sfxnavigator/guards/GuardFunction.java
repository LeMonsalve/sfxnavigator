package io.github.lemonsalve.sfxnavigator.guards;

@FunctionalInterface
public interface GuardFunction<C, N, R> {

  R apply(C navigationContext, N nextRoute);
}
