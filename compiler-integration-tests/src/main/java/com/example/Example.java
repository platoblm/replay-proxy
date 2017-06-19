package com.example;

import io.deloop.tools.references.replay.ReplayAlways;
import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
public interface Example {
    void doOnceA();

    void doOnceB(Object arg);

    void doOnceB(Object argOne, Integer argTwo);

    @ReplayAlways
    void doAlways(Object state);
}
