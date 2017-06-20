Replay Proxy
------------

[![circleci](https://circleci.com/gh/platoblm/replay-proxy.svg?style=shield)](https://circleci.com/gh/platoblm/replay-proxy)
[![codecov.io](http://codecov.io/github/platoblm/replay-proxy/coverage.svg)](https://codecov.io/gh/platoblm/replay-proxy)

Uses an annotation processor, to create proxy instances of an interface, that can record and replay calls 
on a targets that implement that interface, as shown in
```
@CreateReplayProxy
interface MyView {
  
    @ReplayAlways
    void update(String state);
  
    void exit();
}
  
@Mock MyView first;
@Mock MyView second;
  
ReplayProxy<MyView> proxy = ReplayProxyFactory.createFor(MyView.class)
  
@Test public void shouldReplayCalls() {
      // target missing
      proxy.get().exit();
  
      proxy.setTarget(first);
      verify(first).exit();
  
      proxy.get().update("loading");
      verify(first).update("loading");
  
      proxy.setTarget(second);
      verify(second).update("loading");
      verify(second, never()).exit();
}
```

[Check this](compiler-integration-tests/src/test/java/com/example/BasicTest.kt) for more details: 

The created proxy:
- Holds a weak reference to the target.
- Forwards method invocations to its target when present.
- Records invocations if the target is missing, and replays them later, when a target is set.
- Exposes a proxy and never a reference to the target itself.
- Records and replays only the last invocation of each method.
- Always replays a method that is annotated with ReplayAlways, when a target is set.

Use case
--------
Our use case comes from developing on Android. We use a replay proxy to hold a reference to the 
View (Activity, Fragment) in our MVP architecture. This helps address the lifecycle issues of the View, which at times, 
is destroyed and recreated by the system. 
 
We first used reflection and Java's Proxy and InvocationHandler classes, and then this annotation processor to 
generate the needed classes and avoid reflection. 


Android Proguard
----------------
Add this to your proguard-rules file

    -keep class **ReplayProxy { *; }


License
-------
The [license](LICENSE.md)
