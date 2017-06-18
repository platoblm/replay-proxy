package com.example;

import com.deliveroo.common.reference.annotations.ReplayingScreen;

@ReplayingScreen
interface TopScreen extends MiddleScreen {

    void topMethod();
}