package com.example;

import com.deliveroo.common.reference.annotations.ScreenState;

interface MiddleScreen extends BottomScreen {

    @ScreenState
    void middleStateMethod();
}