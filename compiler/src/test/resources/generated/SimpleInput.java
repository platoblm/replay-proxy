package com.example;

import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.CreateReplayProxy;

@CreateReplayProxy
interface Sample {

    @ReplayAlways
    void doAlways(Boolean state);

    void doOnce(String arg);
    void doOnceB();
}