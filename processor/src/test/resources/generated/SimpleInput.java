package com.example;

import io.deloop.tools.references.replay.ReplayAlways;
import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
interface Sample {

    @ReplayAlways
    void doAlways(Boolean state);

    void doOnce(String arg);
    void doOnceB();
}