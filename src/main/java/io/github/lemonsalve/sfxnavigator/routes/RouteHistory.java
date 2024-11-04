package io.github.lemonsalve.sfxnavigator.routes;

import java.util.Stack;
import org.springframework.stereotype.Component;

@Component
public class RouteHistory {

  private final Stack<String> routeHistory = new Stack<>();

  public String getPrevious() {
    return routeHistory.size() > 1 ? routeHistory.get(routeHistory.size() - 2) : null;
  }

  public void add(String route) {
    if (routeHistory.isEmpty() || !routeHistory.peek().equals(route)) {
      routeHistory.push(route);
    }
  }

  public String getCurrent() {
    return routeHistory.isEmpty() ? null : routeHistory.peek();
  }

  public Stack<String> getHistory() {
    return routeHistory;
  }

  public void goBack() {
    if (!routeHistory.isEmpty()) {
      routeHistory.pop();
    }
  }
}