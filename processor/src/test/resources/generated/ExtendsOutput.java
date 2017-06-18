package com.example;

import io.deloop.tools.references.replay.internal.BaseReplayReferenceImpl;
import io.deloop.tools.references.replay.internal.Invocation;
import java.lang.Override;

public final class SampleTop_ReplayReference extends BaseReplayReferenceImpl<SampleTop> implements SampleTop {
    @Override
    public void doOnceTop() {
        // If target is present, forward and return
        SampleTop target = targetRef.get();
        if (target != null) {
            target.doOnceTop();
            return;
        }

        // Record invocation to be replayed later, and override previous calls of the same method
        final String methodId = "doOnceTop";
        recordForOnce(methodId, new Invocation<SampleTop>() {
            @Override
            public void replayOn(SampleTop futureTarget) {
                futureTarget.doOnceTop();
            }
        });
    }

    @Override
    public void doAlwaysMiddle() {
        // If target is present, forward
        SampleTop target = targetRef.get();
        if (target != null) {
            target.doAlwaysMiddle();
        }

        // Record invocation - it will always be replayed when a target is set.
        recordForAlways(new Invocation<SampleTop>() {
            @Override
            public void replayOn(SampleTop futureTarget) {
                futureTarget.doAlwaysMiddle();
            }
        });
    }

    @Override
    public void doOnceBottom() {
        // If target is present, forward and return
        SampleTop target = targetRef.get();
        if (target != null) {
            target.doOnceBottom();
            return;
        }

        // Record invocation to be replayed later, and override previous calls of the same method
        final String methodId = "doOnceBottom";
        recordForOnce(methodId, new Invocation<SampleTop>() {
            @Override
            public void replayOn(SampleTop futureTarget) {
                futureTarget.doOnceBottom();
            }
        });
    }
}
