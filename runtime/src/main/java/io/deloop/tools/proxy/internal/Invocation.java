package io.deloop.tools.proxy.internal;

public interface Invocation<T> {
    void replayOn(T target);
}
