package com.example;

import io.deloop.tools.proxy.CreateReplayProxy;

public class NestedExample {

    @CreateReplayProxy
    public interface NestedInterface {

        void doOnce();
    }
}
