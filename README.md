Replay Proxy
------------

[![circleci](https://circleci.com/gh/platoblm/replay-proxy.svg?style=shield)](https://circleci.com/gh/platoblm/replay-proxy)
[![codecov.io](http://codecov.io/github/platoblm/replay-proxy/coverage.svg)](https://codecov.io/gh/platoblm/replay-proxy)

An annotation processor, that given an interface creates a proxy that makes the following succeed:

```
@HasReplayProxy
interface Example {

    @ReplayAlways
    void doAlways(String state);

    void doOnce();
}

@Mock Example first;
@Mock Example second;

ReplayProxy<Example> proxy = ReplayProxyFactory.createFor(Example.class)

@Test public void shouldReplayCalls() {
      proxy.get().doOnce();

      proxy.setTarget(first);
      verify(first).doOnce();

      proxy.get().doAlways("loading");
      verify(first).doAlways("loading");

      proxy.setTarget(second);
      verify(second).doAlways("loading");
      verify(second, never()).doOnce();
}
```

[Read this](compiler-integration-tests/src/test/java/com/example/BasicTest.kt) for more details: 

The created proxy:
- Forwards method invocations to its target if one is present.
- Records invocations if the target is missing, and replays them later, when a target is set.
- Exposes a proxy and never the target itself.
- Holds a weak reference to the target.
- Records and replays only the last invocation of each method.
- Always replays the last invocation of the method that is annotated with ReplayAlways (optional).

Android Proguard
----------------
Add this to your proguard-rules file

    -keep class **ReplayProxy { *; }


License
-------
The [license](LICENSE.md)
