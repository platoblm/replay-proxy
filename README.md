Replay Reference 
----------------

An annotation processor, that given an interface creates a reference that makes the following succeed: 

```
@ReplayReference
interface Example {

    @ReplayAlways
    void doAlways(String state);

    void doOnce();
}
  
@Mock Example first;
@Mock Example second;
   
Reference<Example> reference = ReplayReferenceFactory.createFor(Example.class)
   
@Test public void shouldReplayCalls() {
      reference.get().doOnce();
      
      reference.setTarget(first);
      verify(first).doOnce();
      
      reference.get().doAlways("loading");
      verify(first).doAlways("loading");
      
      reference.setTarget(second);
      verify(second).doAlways("loading");
      verify(second, never()).doOnce();
}
```

[Read this](processor-integration-tests/src/test/java/com/example/BasicTest.kt) for more details: 

The created reference:
- Forwards method invocations to its target if one is present.
- Records invocations if the target is missing, and replays them later, when a target is set.
- Exposes a proxy and never the target itself. 
- Holds a weak reference to the target.
- Records and replays only the last invocation of each method.
- Always replays the last invocation of the method that is annotated with ReplayAlways (optional).


License
-------
The [license](LICENSE.md)

