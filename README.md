# Benchmarks

The [JMH](https://github.com/openjdk/jmh) tool was used with settings for each benchmark:

+ `Mode = AverageTime` - benchmark measurement mode - function execution time in seconds per operation;
+ `Fork = 10` - number of forks of the process with the program;
+ `Measurement = 1` - number of iterations to count the measurement.

There is a single operation in one fork, so that JIT optimization does not work


The measurement results are presented in the format:

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`B` | `M` | `I` | `S` | `E` | `U`

**Total time:** `T`

Where: Benchmark `B` (`className.methodName`) was measured in the `M` mode the number of times `I`. We got the result `S` with the error `E` in units of measurement `U`. The measurement lasted time `T`

For example, in the first measurement, the execution time of the `Instantiation.measureWith` function is `3,714 ± 0,246` seconds per operation.
The declared accuracy of the tool is `99.9%`, so with a probability of `0.999`, the actual execution time is in the interval `(3,714 - 0,246; 3,714 + 0,246)` s/ops. Of course, it depends on a lot of parameters, and other conditions will lead to different results.



## Instantiation

**Task**: Call a method many times that does not modify the object.

**Question**: How much overhead will it take each time to create a new object for this?

### Implementations

Instantiating a new object for each call:

```Java
public void measureWith(Blackhole bh) {
    for (long i = 0; i < 100000000L; i++) {
        bh.consume(new Foo().bar());
    }
}
```

Instantiating a single object for all calls (since `this.hashCode()` is called in `bar()`, the call is optimized):

```Java
public void measureWithout(Blackhole bh) {
    Foo foo = new Foo();
    for (long i = 0; i < 100000000L; i++) {
        bh.consume(foo.bar());
    }
}
```

### Results

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ |------| ------ | ------ | ------
`Instantiation.measureWith` | `AverageTime` | 10 | 3,714 | ± 0,246 | s/op 
`Instantiation.measureWithout` | `AverageTime` | 10 | 0,091 | ± 0,004 | s/op
 
**Total time:** 3m 49s



## Collections

**Task**: Create a collection of objects and calculate the sum of the method values for each object.

**Question**: How does the style of writing code affect performance?

### Implementations

Procedural style using standard Java syntax:

```Java
public void measureProcedural(Blackhole bh) {
    final Foo[] foos = new Foo[100000000];
    for (int i = 0; i < 100000000; i++) {
        foos[i] = new Foo();
    }
    int sum = 0;
    for (Foo foo : foos) {
        sum += foo.bar();
    }
    bh.consume(sum);
}
```

Relatively functional style using Java Stream API:

```Java
public void measureFunctional(Blackhole bh) {
    int sum = Stream.generate(Foo::new)
            .limit(100000000)
            .map(Foo::bar)
            .mapToInt(Integer::intValue)
            .sum();
    bh.consume(sum);
}
```

Object-Oriented Declarative style using Cactoos:

```Java
public void measureDeclarative(Blackhole bh) {
    int sum = new SumOf(
            new Mapped<Integer>(
                foo -> foo.bar(),
                new Repeated<Foo>(
                    100000000,
                    new Foo()
                )
            )
        ).intValue();
    bh.consume(sum);
}
```

### Results

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ |------| ------ | ------ | ------
`Collections.measureProcedural` | `AverageTime` | 10 | 7,631 | ± 0,524 | s/op
`Collections.measureFunctional` | `AverageTime` | 10 | 4,560 | ± 0,595 | s/op
`Collections.measureDeclarative` | `AverageTime` | 10 | 9,070 | ± 5,783 | s/op

**Total time:** 7m 54s



## Polymorphism

**Task**: Call a polymorphic method many times.

**Question**: How does Java Dynamic Dispatch affect performance?

### Implementations

Late binding polymorphism using Dynamic Dispatch:

```Java
public void measureWith(Blackhole bh) {
    Cart c = new Cart(new Book("1984"));
    c.p = new Movie("Godfather");
    for (long i = 0; i < 10000000000L; i++) {
        bh.consume(c.total());
    }
}
```

Reducing polymorphism before compilation using object specialization:

```Java
public void measureWithout(Blackhole bh) {
    Cart1 c1 = new Cart1(new Book("1984"));
    Cart2 c2 = c1.with(new Movie("Godfather"));
    for (long i = 0; i < 10000000000L; i++) {
        bh.consume(c2.total());
    }
}
```

### Results

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Polymorphism.measureWith` | `AverageTime` | 10 | 8,648 | ± 0,834 | s/op
`Polymorphism.measureWithout` | `AverageTime` | 10 | 6,360 | ± 0,346 | s/op

**Total time:** 5m 18s
