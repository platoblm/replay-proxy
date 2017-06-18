package io.deloop.tools.references.replay;

public interface Reference<T> {

    T get();

    void setTarget(T target);
    void clearTarget();
}
