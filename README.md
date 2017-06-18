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
     This is free and unencumbered software released into the public domain.
     
     Anyone is free to copy, modify, publish, use, compile, sell, or
     distribute this software, either in source code form or as a compiled
     binary, for any purpose, commercial or non-commercial, and by any
     means.
     
     In jurisdictions that recognize copyright laws, the author or authors
     of this software dedicate any and all copyright interest in the
     software to the public domain. We make this dedication for the benefit
     of the public at large and to the detriment of our heirs and
     successors. We intend this dedication to be an overt act of
     relinquishment in perpetuity of all present and future rights to this
     software under copyright law.
     
     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
     EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
     MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
     IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
     OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
     ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
     OTHER DEALINGS IN THE SOFTWARE.
     
     For more information, please refer to <http://unlicense.org/>

