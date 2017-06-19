package com.example;

import io.deloop.tools.proxy.HasReplayProxy;

@HasReplayProxy
interface SampleTop extends SampleMiddle {

    void doOnceTop();
}