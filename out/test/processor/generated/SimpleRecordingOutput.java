package com.example;

import com.deliveroo.common.reference.Invocation;
import com.deliveroo.common.reference.ReferenceImpl;
import java.lang.Override;
import java.lang.String;

public final class ScreenOtherType_ReplayingReference extends ReferenceImpl<ScreenOtherType> implements ScreenOtherType {
    @Override
    public void showMessage(final String message) {
        ScreenOtherType target = currentTarget();
        if (target != null) {
            target.showMessage(message);
            return;
        }

        final String methodKey = "showMessage";

        recordToReplayOnce(methodKey, new Invocation<ScreenOtherType>() {
            @Override
            public void replayOn(ScreenOtherType futureTarget) {
                futureTarget.showMessage(message);
            }
        });
    }

    @Override
    public void noArgsMethod() {
        ScreenOtherType target = currentTarget();
        if (target != null) {
            target.noArgsMethod();
            return;
        }

        final String methodKey = "noArgsMethod";

        recordToReplayOnce(methodKey, new Invocation<ScreenOtherType>() {
            @Override
            public void replayOn(ScreenOtherType futureTarget) {
                futureTarget.noArgsMethod();
            }
        });
    }
}