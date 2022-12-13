# Benchmarks

The [JMH](https://github.com/openjdk/jmh) tool was used with settings for each benchmark:

+ `Mode = Throughput` - benchmark measurement mode - function performance in operations per second;
+ `Fork = 5` - number of forks of the process with the program;
+ `Warmup = 5` - number of warmup iterations that are not included in the measurement;
+ `Measurement = 5` - number of iterations to count the measurement;


The measurement results are presented in the format:

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`B` | `M` | `I` | `S` | `E` | `U`

**Total time:** `T`

Where: Benchmark `B` (`className.methodName`) was measured in the `M` mode the number of times `I`. We got the result `S` with the error `E` in units of measurement `U`. The measurement lasted time `T`

For example, in the first measurement, the throughput of the `Instantiation.measureWith` function is `35479,436 ± 3843,144` operations per second.
The declared accuracy of the tool is `99.9%`, so with a probability of `0.999`, the actual throughput is in the interval `(35479,436 - 3843,144; 35479,436 + 3843,144)` ops/s. Of course, it depends on a lot of parameters, and other conditions will lead to different results.



## Instantiation

**Task**: Call a method many times that does not modify the object.

**Question**: How much overhead will it take each time to create a new object for this?

### Implementations

Instantiating a new object for each call:

```Java
public void measureWith(Blackhole bh) {
    for (int i = 0; i < 1000000; i++) {
        bh.consume(new Foo().bar());
    }
}
```

Instantiating a single object for all calls:

```Java
public void measureWithout(Blackhole bh) {
    Foo foo = new Foo();
    for (int i = 0; i < 1000000; i++) {
        bh.consume(foo.bar());
    }
}
```

### Results

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Instantiation.measureWith` | `Throughput` | 5 | 35479,436 | ± 3843,144 | ops/s 
`Instantiation.measureWithout` | `Throughput` | 5 | 36249,932 | ± 711,607 | ops/s
 
**Total time:** `T`



## Collections

**Task**: Create a collection of objects and calculate the sum of the method values for each object.

**Question**: How does the style of writing code affect performance?

### Implementations

Procedural style using standard Java syntax:

```Java
public void measureProcedural(Blackhole bh) {
    final Foo[] foos = new Foo[10000];
    for (int i = 0; i < 10000; i++) {
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
            .limit(10000)
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
                            10000,
                            new Foo()
                    )
            )
    ).intValue();
    bh.consume(sum);
}
```

### Results

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Collections.measureProcedural` | `Throughput` | 25 | 30488,728 | ± 1379,901 | ops/s
`Collections.measureFunctional` | `Throughput` | 25 | 18794,544 | ± 466,739 | ops/s
`Collections.measureDeclarative` | `Throughput` | 25 | 2412,086 | ± 180,319 | ops/s

**Total time:** `T`



## Polymorphism

**Task**: Call a polymorphic method many times.

**Question**: How does Java Dynamic Dispatch affect performance?

### Implementations

Late binding polymorphism using Dynamic Dispatch (there is probably a JIP optimization here):

```Java
public void measureWith(Blackhole bh) {
    Cart c = new Cart(new Book("1984"));
    c.p = new Movie("Godfather");
    for (long i = 0; i < 10000000L; i++) {
        bh.consume(c.total());
    }
}
```

Reducing polymorphism before compilation using object specialization:

```Java
public void measureWithout(Blackhole bh) {
    Cart1 c1 = new Cart1(new Book("1984"));
    Cart2 c2 = c1.with(new Movie("Godfather"));
    for (long i = 0; i < 10000000L; i++) {
        bh.consume(c2.total());
    }
}
```

### Results

Benchmark | Mode | Iterations | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Polymorphism.measureWith` | `Throughput` | 25 | 10,291 | ± 0,053 | ops/s
`Polymorphism.measureWithout` | `Throughput` | 25 | 10,471 | ± 0,057 | ops/s

**Total time:** 16m 54s

### Discussion

One fork with 5 measurable iterations outputs the following results:

```bash
# Run progress: 20,00% complete, ETA 00:15:10
# Fork: 3 of 5
# Warmup Iteration   1: 0,075 ops/s
# Warmup Iteration   2: 0,075 ops/s
# Warmup Iteration   3: 2,579 ops/s
# Warmup Iteration   4: 2,506 ops/s
# Warmup Iteration   5: 2,579 ops/s
Iteration   1: 2,502 ops/s
Iteration   2: 2,556 ops/s
Iteration   3: 2,561 ops/s
Iteration   4: 2,479 ops/s
Iteration   5: 2,329 ops/s
```

The first two iterations are noticeably slower than the next ones. Therefore, we can assume that JIT optimization works
