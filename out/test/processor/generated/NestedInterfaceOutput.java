package com.example;

import com.deliveroo.common.reference.Invocation;
import com.deliveroo.common.reference.ReferenceImpl;
import java.lang.Override;

public final class ScreenType_ReplayingReference extends ReferenceImpl<SomeClass.OtherClass.ScreenType> implements SomeClass.OtherClass.ScreenType {
    @Override
    public void method() {
        SomeClass.OtherClass.ScreenType target = currentTarget();
        if (target != null) {
            target.method();
            return;
        }

        final String methodKey = "method";

        recordToReplayOnce(methodKey, new Invocation<SomeClass.OtherClass.ScreenType>() {
            @Override
            public void replayOn(SomeClass.OtherClass.ScreenType futureTarget) {
                futureTarget.method();
            }
        });
    }
}