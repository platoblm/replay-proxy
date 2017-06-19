package com.example;

import io.deloop.tools.proxy.HasReplayProxy;

public class NestedExample {

    @HasReplayProxy
    public interface NestedInterface {

        void doOnce();
    }
}
