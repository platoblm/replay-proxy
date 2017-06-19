package io.deloop.tools.proxy;

public interface ReplayProxy<T> {

    T get();

    void setTarget(T target);
    void clearTarget();
}
