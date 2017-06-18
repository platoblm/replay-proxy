import io.deloop.tools.references.replay.ReplayAlways;
import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
interface TooManyReplayAlways {

    @ReplayAlways
    void methodOne(Object arg);

    @ReplayAlways
    void methodTwo(Object arg1, Object arg2);
}