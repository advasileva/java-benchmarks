package org.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class Instantiation extends Template {
    @Benchmark
    public void measureWith(Blackhole bh) {
        for (long i = 0; i < 100000000L; i++) {
            bh.consume(new Foo().bar());
        }
    }

    @Benchmark
    public void measureWithout(Blackhole bh) {
        Foo foo = new Foo();
        for (long i = 0; i < 100000000L; i++) {
            bh.consume(foo.bar());
        }
    }
}