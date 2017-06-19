import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.CreateReplayProxy;

@CreateReplayProxy
interface TooManyReplayAlways {

    @ReplayAlways
    void methodOne(Object arg);

    @ReplayAlways
    void methodTwo(Object arg1, Object arg2);
}