package com.example;

import io.deloop.tools.references.replay.ReplayAlways;
import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
interface AccountScreen {

    @ReplayAlways
    void update(Boolean isLoading);

    void showMessage(String message);
    void exit();
}