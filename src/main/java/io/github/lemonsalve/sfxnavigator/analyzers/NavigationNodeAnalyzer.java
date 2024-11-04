package io.github.lemonsalve.sfxnavigator.analyzers;

import io.github.lemonsalve.sfxnavigator.contexts.RoutesContext;
import io.github.lemonsalve.sfxnavigator.routes.Route;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

public class NavigationNodeAnalyzer {

  private static final String NAVIGATE_TO_PREFIX = "navigateTo";
  private final Map<Node, Route> navigationNodes = new HashMap<>();
  private final RoutesContext routesContext;

  public NavigationNodeAnalyzer(RoutesContext routesContext) {
    this.routesContext = routesContext;
  }

  public void analyzeNodeAndCreateHandlers(
    final @NotNull Scene scene,
    final Consumer<String> navigationConsumer
  ) {
    analyzeNode(scene.getRoot());
    createNavigationHandlers(navigationConsumer);
  }

  private void analyzeNode(
    final Node node
  ) {
    if (node instanceof Parent parent) {
      for (Node child : parent.getChildrenUnmodifiable()) {
        analyzeNode(child);
      }
    }

    if (!isNavigationNode(node)) {
      return;
    }

    final String routeName = getRouteName(node);
    final Route route = routesContext.findRoute(routeName);
    navigationNodes.put(node, route);
  }

  private void createNavigationHandlers(
    final Consumer<String> navigationConsumer
  ) {
    navigationNodes.forEach(
      (node, route) -> createNavigationHandler(node, route, navigationConsumer)
    );
    navigationNodes.clear();
  }

  private void createNavigationHandler(
    final @NotNull Node node,
    final @NotNull Route route,
    final @NotNull Consumer<String> navigationConsumer
  ) {
    node.addEventHandler(
      MouseEvent.MOUSE_CLICKED,
      event -> navigationConsumer.accept(route.name())
    );
  }

  private boolean isNavigationNode(final @NotNull Node node) {
    return node.getId() != null && node.getId().startsWith(NAVIGATE_TO_PREFIX);
  }

  private @NotNull String getRouteName(final @NotNull Node node) {
    return node.getId().substring(NAVIGATE_TO_PREFIX.length());
  }
}
