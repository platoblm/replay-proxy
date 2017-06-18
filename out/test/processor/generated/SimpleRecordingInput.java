package com.example;

import com.deliveroo.common.reference.annotations.ReplayingScreen;

@ReplayingScreen
interface ScreenOtherType {

    void showMessage(String message);

    void noArgsMethod();
}