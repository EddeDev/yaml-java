# yaml-java
A YAML 1.2 parser and emitter for Java, currently very work in progress.

``` java
// Simple example

Emitter emitter = new Emitter();

emitter.write(Event.BEGIN_MAP);

emitter.write(Event.KEY).write("ThisIsAKey");
emitter.write(Event.VALUE).write("ThisIsAValue");

emitter.write(Event.END_MAP);

String result = emitter.string();

Node data = Parser.parse(result);

String value = data.get("ThisIsAKey");
System.out.println(value);

// Output: ThisIsAValue
```
