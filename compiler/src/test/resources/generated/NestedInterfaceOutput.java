package com.example;

import io.deloop.tools.proxy.internal.BaseImpl;
import io.deloop.tools.proxy.internal.Invocation;
import java.lang.Override;

public final class SampleNestedInterface_ReplayProxy extends BaseImpl<SampleOuter.SampleNested.SampleNestedInterface> implements SampleOuter.SampleNested.SampleNestedInterface {

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
