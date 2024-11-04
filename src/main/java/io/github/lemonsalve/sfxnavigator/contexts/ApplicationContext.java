package io.github.lemonsalve.sfxnavigator.contexts;

@FunctionalInterface
public interface ApplicationContext {

  <T> T getBean(Class<T> beanClass);
}
