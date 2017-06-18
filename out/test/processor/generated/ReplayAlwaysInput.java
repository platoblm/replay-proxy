package com.example;

import com.deliveroo.common.reference.annotations.ReplayingScreen;
import com.deliveroo.common.reference.annotations.ScreenState;

@ReplayingScreen
interface ScreenOtherType {

    @ScreenState
    void updateState(Object state);
}