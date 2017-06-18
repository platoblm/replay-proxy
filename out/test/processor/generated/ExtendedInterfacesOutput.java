package com.example;

import com.deliveroo.common.reference.Invocation;
import com.deliveroo.common.reference.ReferenceImpl;
import java.lang.Override;

public final class TopScreen_ReplayingReference extends ReferenceImpl<TopScreen> implements TopScreen {
    @Override
    public void topMethod() {
        TopScreen target = currentTarget();
        if (target != null) {
            target.topMethod();
            return;
        }

        final String methodKey = "topMethod";

        recordToReplayOnce(methodKey, new Invocation<TopScreen>() {
            @Override
            public void replayOn(TopScreen futureTarget) {
                futureTarget.topMethod();
            }
        });
    }

    @Override
    public void middleStateMethod() {
        TopScreen target = currentTarget();
        if (target != null) {
            target.middleStateMethod();
        }

        recordToReplayAlways(new Invocation<TopScreen>() {
            @Override
            public void replayOn(TopScreen futureTarget) {
                futureTarget.middleStateMethod();
            }
        });
    }

    @Override
    public void bottomMethod() {
        TopScreen target = currentTarget();
        if (target != null) {
            target.bottomMethod();
            return;
        }

        final String methodKey = "bottomMethod";

        recordToReplayOnce(methodKey, new Invocation<TopScreen>() {
            @Override
            public void replayOn(TopScreen futureTarget) {
                futureTarget.bottomMethod();
            }
        });
    }
}