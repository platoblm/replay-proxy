import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.HasReplayProxy;

@HasReplayProxy
interface TooManyReplayAlways {

    @ReplayAlways
    void methodOne(Object arg);

    @ReplayAlways
    void methodTwo(Object arg1, Object arg2);
}