# Benchmarks

The [JMH](https://github.com/openjdk/jmh) tool was used with settings for each benchmark:

+ Number of Forks = 5;
+ Benchmark Mode = `Throughput`;
+ Warmup Iterations = 5;
+ Measurement Iterations = 5;




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

Benchmark | Mode | Cnt | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Instantiation.measureWith` | thrpt | 5 | 35479,436 | ± 3843,144 | ops/s
`Instantiation.measureWithout` | thrpt | 5 | 36249,932 | ± 711,607 | ops/s




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

Benchmark | Mode | Cnt | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Collections.measureProcedural` | thrpt | 25 | 30488,728 | ± 1379,901 | ops/s
`Collections.measureFunctional` | thrpt | 25 | 18794,544 | ± 466,739 | ops/s
`Collections.measureDeclarative` | thrpt | 25 | 2412,086 | ± 180,319 | ops/s



## Polymorphism

**Task**: Call a polymorphic method many times.

**Question**: How does Java Dynamic Dispatch affect performance?

### Implementations

Late binding polymorphism using Dynamic Dispatch (there is probably a JIP optimization here):

```Java
public void measureWith(Blackhole bh) {
    Cart c = new Cart(new Book("1984"));
    c.p = new Movie("Godfather");
    for (int i = 0; i < 1000000000; i++) {
        bh.consume(c.total());
    }
}
```

Reducing polymorphism before compilation using object specialization:

```Java
public void measureWithout(Blackhole bh) {
    Cart1 c1 = new Cart1(new Book("1984"));
    Cart2 c2 = c1.with(new Movie("Godfather"));
    for (int i = 0; i < 1000000000; i++) {
        bh.consume(c2.total());
    }
}
```

### Results

Benchmark | Mode | Cnt | Score | Error | Units
------ | ------ | ------ | ------ | ------ | ------
`Polymorphism.measureWith` | thrpt | 25 | 38,970 | ± 0,366 | ops/s
`Polymorphism.measureWithout` | thrpt | 25 | 38,985 | ± 0,247 | ops/s
