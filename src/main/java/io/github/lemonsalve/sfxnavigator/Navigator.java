package io.github.lemonsalve.sfxnavigator;

import io.github.lemonsalve.sfxnavigator.analyzers.NavigationNodeAnalyzer;
import io.github.lemonsalve.sfxnavigator.contexts.ApplicationContext;
import io.github.lemonsalve.sfxnavigator.contexts.NavigationContext;
import io.github.lemonsalve.sfxnavigator.contexts.RoutesContext;
import io.github.lemonsalve.sfxnavigator.exceptions.RouteRendererNotFoundException;
import io.github.lemonsalve.sfxnavigator.exceptions.SceneCreationFailedException;
import io.github.lemonsalve.sfxnavigator.routes.Route;
import io.github.lemonsalve.sfxnavigator.routes.RouteConfiguration;
import io.github.lemonsalve.sfxnavigator.routes.RouteHistory;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Navigator {

  private final ApplicationContext appContext;
  private final RouteHistory routeHistory;
  private final RoutesContext routesContext;
  private final NavigationNodeAnalyzer navigationNodeAnalyzer;
  private final Class<? extends Application> fxAppClass;
  private String currentParentRoute = null;
  private Stage primaryStage;

  public Navigator(
    final ApplicationContext appContext,
    final RouteHistory routeHistory,
    final RoutesContext routesContext,
    final NavigationNodeAnalyzer navigationNodeAnalyzer,
    final Class<? extends Application> fxAppClass
  ) {
    this.appContext = appContext;
    this.routeHistory = routeHistory;
    this.routesContext = routesContext;
    this.navigationNodeAnalyzer = navigationNodeAnalyzer;
    this.fxAppClass = fxAppClass;
  }

  public void init(final Stage stage) {
    this.primaryStage = stage;
  }

  public void navigateTo(final @NotNull String routeName) {
    if (!routeHistory.getHistory().isEmpty() && routeHistory.getCurrent().equals(routeName)) {
      return;
    }

    Route route = routesContext.findRoute(routeName);
    Route nextRoute = executeGuard(route);

    while (!nextRoute.equals(route)) {
      route = nextRoute;
      nextRoute = executeGuard(route);
    }

    final Route parentRoute = routesContext.findParentRoute(nextRoute.name());
    if (parentRoute == null) {
      currentParentRoute = null;
      loadBaseRoute(route);
      return;
    }

    final Route parentNextRoute = executeGuard(parentRoute);

    if (!parentNextRoute.equals(parentRoute)
      && !routeHistory.getHistory().contains(parentNextRoute.name())) {
      navigateTo(parentNextRoute.name());
      return;
    }

    if (!parentRoute.name().equals(currentParentRoute)) {
      loadParentRoute(parentRoute);
      currentParentRoute = parentRoute.name();
    }

    loadChildRoute(nextRoute);
  }

  private void loadBaseRoute(final @NotNull Route route) {
    final FXMLLoader loader = getFXMLLoader(route);
    final RouteConfiguration routeConfiguration = route.configuration();

    Scene scene;
    try {
      scene = new Scene(loader.load(), routeConfiguration.width(), routeConfiguration.height());
    } catch (IOException e) {
      throw new SceneCreationFailedException("Failed to create scene", e);
    }

    onNavigateAction(route, scene, true);
  }

  private void loadParentRoute(final @NotNull Route parentRoute) {
    final FXMLLoader loader = getFXMLLoader(parentRoute);
    final RouteConfiguration parentConfiguration = parentRoute.configuration();

    Scene scene;

    try {
      scene = new Scene(loader.load(), parentConfiguration.width(), parentConfiguration.height());
    } catch (IOException e) {
      throw new SceneCreationFailedException("Failed to create parent scene", e);
    }

    onNavigateAction(parentRoute, scene, false);
  }

  private void loadChildRoute(final @NotNull Route childRoute) {
    final FXMLLoader loader = getFXMLLoader(childRoute);

    try {
      final BorderPane parentRoot = (BorderPane) primaryStage.getScene().lookup("#routeRenderer");
      if (parentRoot == null) {
        throw new RouteRendererNotFoundException(
          "No BorderPane with id '" + "routeRenderer" + "' found in the parent scene."
        );
      }
      final BorderPane childPane = loader.load();
      parentRoot.setCenter(childPane);
    } catch (IOException e) {
      throw new SceneCreationFailedException("Failed to create child scene", e);
    }

    routeHistory.add(childRoute.name());
    navigationNodeAnalyzer.analyzeNodeAndCreateHandlers(primaryStage.getScene(), this::navigateTo);
    primaryStage.setResizable(false);
    setWindowTitle(childRoute.title());
    primaryStage.show();
  }

  public Route executeGuard(final @NotNull Route route) {
    return route
      .guard()
      .protect()
      .apply(getNavigationContext(), route);
  }

  private void onNavigateAction(@NotNull Route route, Scene scene, boolean addToHistory) {
    if (addToHistory) {
      routeHistory.add(route.name());
    }

    navigationNodeAnalyzer.analyzeNodeAndCreateHandlers(scene, this::navigateTo);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    setWindowTitle(route.title());
    primaryStage.show();
  }

  @Contract("_ -> new")
  private @NotNull FXMLLoader getFXMLLoader(final @NotNull Route route) {
    final FXMLLoader loader = new FXMLLoader(fxAppClass.getResource(route.path()));
    loader.setControllerFactory(appContext::getBean);
    return loader;
  }

  public NavigationContext getNavigationContext() {
    return new NavigationContext(appContext, routesContext, routeHistory);
  }

  private void setWindowTitle(final String title) {
    primaryStage.setTitle("");
    primaryStage.setTitle(title);
  }
}