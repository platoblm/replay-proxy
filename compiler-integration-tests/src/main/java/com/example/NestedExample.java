package com.example;

import io.deloop.tools.references.replay.ReplayReference;

public class NestedExample {

    @ReplayReference
    public interface NestedInterface {

        void doOnce();
    }
}
