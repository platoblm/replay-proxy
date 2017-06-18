package com.example;

import io.deloop.tools.references.replay.internal.BaseReplayReferenceImpl;
import io.deloop.tools.references.replay.internal.Invocation;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;

public final class AccountScreen_ReplayReference extends BaseReplayReferenceImpl<AccountScreen> implements AccountScreen {
    @Override
    public void update(final Boolean isLoading) {
        AccountScreen target = currentTarget();
        if (target != null) {
            target.update(isLoading);
        }
        recordToReplayAlways(new Invocation<AccountScreen>() {
            @Override
            public void replayOn(AccountScreen futureTarget) {
                futureTarget.update(isLoading);
            }
        });
    }

    @Override
    public void showMessage(final String message) {
        AccountScreen target = currentTarget();
        if (target != null) {
            target.showMessage(message);
            return;
        }
        final String methodKey = "showMessage";
        recordToReplayOnce(methodKey, new Invocation<AccountScreen>() {
            @Override
            public void replayOn(AccountScreen futureTarget) {
                futureTarget.showMessage(message);
            }
        });
    }

    @Override
    public void exit() {
        AccountScreen target = currentTarget();
        if (target != null) {
            target.exit();
            return;
        }
        final String methodKey = "exit";
        recordToReplayOnce(methodKey, new Invocation<AccountScreen>() {
            @Override
            public void replayOn(AccountScreen futureTarget) {
                futureTarget.exit();
            }
        });
    }
}
