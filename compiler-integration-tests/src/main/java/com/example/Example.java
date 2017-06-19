package com.example;

import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.CreateReplayProxy;

@CreateReplayProxy
public interface Example {
    void doOnceA();

    void doOnceB(Object arg);

    void doOnceB(Object argOne, Integer argTwo);

    @ReplayAlways
    void doAlways(Object state);
}
