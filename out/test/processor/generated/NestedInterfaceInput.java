package com.example;

import com.deliveroo.common.reference.annotations.ReplayingScreen;

class SomeClass {

    public static class OtherClass {

        @ReplayingScreen
        interface ScreenType {

            void method();
        }
    }
}