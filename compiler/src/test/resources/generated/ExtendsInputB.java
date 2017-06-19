package com.example;

import io.deloop.tools.proxy.ReplayAlways;

interface SampleMiddle extends SampleBottom {

    @ReplayAlways
    void doAlwaysMiddle();
}