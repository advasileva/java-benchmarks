package org.sample;

import org.openjdk.jmh.annotations.*;

public class Instantiation {
    @Benchmark
    public void measureWithInstantiation(org.openjdk.jmh.infra.Blackhole bh) {
        for (int i = 0; i < 100000; i++) {
            new Foo().hello();
        }
    }

    @Benchmark
    public void measureWithoutInstantiation(org.openjdk.jmh.infra.Blackhole bh) {
        Foo foo = new Foo();
        for (int i = 0; i < 100000; i++) {
            foo.hello();
        }
    }
}