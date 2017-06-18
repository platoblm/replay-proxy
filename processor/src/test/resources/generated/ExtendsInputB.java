package com.example;

import io.deloop.tools.references.replay.ReplayAlways;

interface SampleMiddle extends SampleBottom {

    @ReplayAlways
    void doAlwaysMiddle();
}