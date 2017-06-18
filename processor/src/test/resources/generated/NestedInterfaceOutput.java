package com.example;

import io.deloop.tools.references.replay.internal.BaseReplayReferenceImpl;
import io.deloop.tools.references.replay.internal.Invocation;
import java.lang.Override;

public final class SampleNestedInterface_ReplayReference extends BaseReplayReferenceImpl<SampleOuter.SampleNested.SampleNestedInterface> implements SampleOuter.SampleNested.SampleNestedInterface {

    @Override
    public void doOnce() {
        SampleOuter.SampleNested.SampleNestedInterface target = targetRef.get();
        if (target != null) {
            target.doOnce();
            return;
        }

        final String methodId = "doOnce";
        recordForOnce(methodId, new Invocation<SampleOuter.SampleNested.SampleNestedInterface>() {
            @Override
            public void replayOn(SampleOuter.SampleNested.SampleNestedInterface futureTarget) {
                futureTarget.doOnce();
            }
        });
    }
}
