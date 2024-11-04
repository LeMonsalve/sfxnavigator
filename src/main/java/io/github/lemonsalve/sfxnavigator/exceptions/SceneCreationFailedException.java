package io.github.lemonsalve.sfxnavigator.exceptions;

public class SceneCreationFailedException extends RuntimeException {

  public SceneCreationFailedException(String message) {
    super(message);
  }

  public SceneCreationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
