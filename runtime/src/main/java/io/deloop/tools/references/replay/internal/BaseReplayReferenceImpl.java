package io.deloop.tools.references.replay.internal;

import io.deloop.tools.references.replay.Reference;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseReplayReferenceImpl<T> implements Reference<T> {

    private static final WeakReference EMPTY = new WeakReference(null);

    private final Map<String, Invocation<T>> replayOnce = new LinkedHashMap<>();
    private Invocation replayAlways = null;

    protected WeakReference<T> targetRef = EMPTY;

    @Override public T get() {
        return (T) this;
    }

    @Override public void setTarget(T target) {
        checkTarget(target);

        targetRef = new WeakReference<>(target);
        replayRecorded(target);
    }

    @Override public void clearTarget() {
        targetRef = EMPTY;
    }

    protected synchronized void recordForOnce(String key, Invocation<T> invocation) {
        replayOnce.remove(key);
        replayOnce.put(key, invocation);
    }

    protected synchronized void recordForAlways(Invocation<T> invocation) {
        replayAlways = invocation;
    }

    private synchronized void replayRecorded(T target) {
        if (replayAlways != null) {
            replayAlways.replayOn(target);
        }

        for (Invocation invocation : replayOnce.values()) {
            invocation.replayOn(target);
        }
        replayOnce.clear();
    }

    private void checkTarget(T target) {
        if (target == null) {
            throw new NullPointerException("Target cannot be null. Call clearTarget() to clear the target.");
        }
    }
}
