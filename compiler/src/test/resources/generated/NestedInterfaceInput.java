package com.example;

import io.deloop.tools.proxy.CreateReplayProxy;

class SampleOuter {
    public static class SampleNested {

        @CreateReplayProxy
        interface SampleNestedInterface {

            void doOnce();
        }
    }
}