package org.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class Instantiation extends Template {
    @Benchmark
    public void measureWith(Blackhole bh) {
        for (int i = 0; i < 1000000; i++) {
            bh.consume(new Foo().bar());
        }
    }

    @Benchmark
    public void measureWithout(Blackhole bh) {
        Foo foo = new Foo();
        for (int i = 0; i < 1000000; i++) {
            bh.consume(foo.bar());
        }
    }
}