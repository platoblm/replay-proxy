import io.deloop.tools.references.replay.ReplayAlways;
import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
interface ValidInterface {

    void methodA();

    @ReplayAlways
    void methodB(Object a, Integer b);
}