package com.example;

import io.deloop.tools.proxy.HasReplayProxy;

class SampleOuter {
    public static class SampleNested {

        @HasReplayProxy
        interface SampleNestedInterface {

            void doOnce();
        }
    }
}