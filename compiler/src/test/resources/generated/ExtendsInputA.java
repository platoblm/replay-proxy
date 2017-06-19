package com.example;

import io.deloop.tools.proxy.CreateReplayProxy;

@CreateReplayProxy
interface SampleTop extends SampleMiddle {

    void doOnceTop();
}