package org.benchmark;

import org.cactoos.func.FuncOf;
import org.cactoos.iterable.Repeated;
import org.cactoos.iterable.Mapped;
import org.cactoos.number.SumOf;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.infra.Blackhole;

import java.util.stream.Stream;

public class Collections extends Template {
    @Benchmark
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

    @Benchmark
    public void measureFunctional(Blackhole bh) {
        int sum = Stream.generate(Foo::new)
                .limit(10000)
                .map(Foo::bar)
                .mapToInt(Integer::intValue)
                .sum();
        bh.consume(sum);
    }

    @Benchmark
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
}
