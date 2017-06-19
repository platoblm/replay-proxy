package com.example;

import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.HasReplayProxy;

@HasReplayProxy
interface Sample {

    @ReplayAlways
    void doAlways(Boolean state);

    void doOnce(String arg);
    void doOnceB();
}