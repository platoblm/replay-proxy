package com.example;

import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
interface SampleTop extends SampleMiddle {

    void doOnceTop();
}