package com.example;

import io.deloop.tools.references.replay.internal.BaseReplayReferenceImpl;
import io.deloop.tools.references.replay.internal.Invocation;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;

public final class Sample_ReplayReference extends BaseReplayReferenceImpl<Sample> implements Sample {

    @Override
    public void doAlways(final Boolean state) {
        Sample target = targetRef.get();
        if (target != null) {
            target.doAlways(state);
        }

        recordForAlways(new Invocation<Sample>() {
            @Override
            public void replayOn(Sample futureTarget) {
                futureTarget.doAlways(state);
            }
        });
    }

    @Override
    public void doOnce(final String arg) {
        Sample target = targetRef.get();
        if (target != null) {
            target.doOnce(arg);
            return;
        }

        final String methodId = "doOnce";
        recordForOnce(methodId, new Invocation<Sample>() {
            @Override
            public void replayOn(Sample futureTarget) {
                futureTarget.doOnce(arg);
            }
        });
    }

    @Override
    public void doOnceB() {
        Sample target = targetRef.get();
        if (target != null) {
            target.doOnceB();
            return;
        }

        final String methodId = "doOnceB";
        recordForOnce(methodId, new Invocation<Sample>() {
            @Override
            public void replayOn(Sample futureTarget) {
                futureTarget.doOnceB();
            }
        });
    }
}
