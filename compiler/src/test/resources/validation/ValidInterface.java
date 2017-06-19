import io.deloop.tools.proxy.ReplayAlways;
import io.deloop.tools.proxy.HasReplayProxy;

@HasReplayProxy
interface ValidInterface {

    void methodA();

    @ReplayAlways
    void methodB(Object a, Integer b);
}