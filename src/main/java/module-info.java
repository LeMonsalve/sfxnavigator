module sfxnavigator {
  requires javafx.fxml;
  requires javafx.graphics;
  requires org.jetbrains.annotations;

  exports io.github.lemonsalve.sfxnavigator;
  exports io.github.lemonsalve.sfxnavigator.routes;
  exports io.github.lemonsalve.sfxnavigator.contexts;
  exports io.github.lemonsalve.sfxnavigator.exceptions;
  exports io.github.lemonsalve.sfxnavigator.utils;
  exports io.github.lemonsalve.sfxnavigator.guards;
  exports io.github.lemonsalve.sfxnavigator.analyzers;
}