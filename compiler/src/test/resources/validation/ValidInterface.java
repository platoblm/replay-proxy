import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.CreateReplayProxy;

@CreateReplayProxy
interface ValidInterface {

    void methodA();

    @ReplayAlways
    void methodB(Object a, Integer b);
}