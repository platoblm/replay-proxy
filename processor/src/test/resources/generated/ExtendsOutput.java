package com.example;

import io.deloop.tools.references.replay.internal.BaseReplayReferenceImpl;
import io.deloop.tools.references.replay.internal.Invocation;
import java.lang.Override;

public final class SampleTop_ReplayReference extends BaseReplayReferenceImpl<SampleTop> implements SampleTop {

    @Override
    public void doOnceTop() {
        SampleTop target = targetRef.get();
        if (target != null) {
            target.doOnceTop();
            return;
        }

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
        SampleTop target = targetRef.get();
        if (target != null) {
            target.doAlwaysMiddle();
        }

        recordForAlways(new Invocation<SampleTop>() {
            @Override
            public void replayOn(SampleTop futureTarget) {
                futureTarget.doAlwaysMiddle();
            }
        });
    }

    @Override
    public void doOnceBottom() {
        SampleTop target = targetRef.get();
        if (target != null) {
            target.doOnceBottom();
            return;
        }

        final String methodId = "doOnceBottom";
        recordForOnce(methodId, new Invocation<SampleTop>() {
            @Override
            public void replayOn(SampleTop futureTarget) {
                futureTarget.doOnceBottom();
            }
        });
    }
}
