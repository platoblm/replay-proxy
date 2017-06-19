package com.example;

import io.deloop.tools.references.replay.ReplayReference;

class SampleOuter {
    public static class SampleNested {

        @ReplayReference
        interface SampleNestedInterface {

            void doOnce();
        }
    }
}