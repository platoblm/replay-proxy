package com.example;

import com.deliveroo.common.reference.Invocation;
import com.deliveroo.common.reference.ReferenceImpl;
import java.lang.Object;
import java.lang.Override;

public final class ScreenOtherType_ReplayingReference extends ReferenceImpl<ScreenOtherType> implements ScreenOtherType {
    @Override
    public void updateState(final Object state) {
        ScreenOtherType target = currentTarget();
        if (target != null) {
            target.updateState(state);
        }

        recordToReplayAlways(new Invocation<ScreenOtherType>() {
            @Override
            public void replayOn(ScreenOtherType futureTarget) {
                futureTarget.updateState(state);
            }
        });
    }
}