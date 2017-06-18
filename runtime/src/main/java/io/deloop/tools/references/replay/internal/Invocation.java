package io.deloop.tools.references.replay.internal;

public interface Invocation<T> {
    void replayOn(T target);
}
