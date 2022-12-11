package org.benchmark;

import org.benchmark.polymorphism.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.infra.Blackhole;

public class Polymorphism extends Template {
    @Benchmark
    @Fork(1)
    public void measureWith(Blackhole bh, MockState state) {
        Cart c = new Cart(new Book("1984"));
        c.p = new Movie("Godfather");
        for (long i = 0; i < 10000000000L; i++) {
            bh.consume(c.total(state));
        }
    }

    @Benchmark
    @Fork(1)
    public void measureWithout(Blackhole bh, MockState state) {
        Cart1 c1 = new Cart1(new Book("1984"));
        Cart2 c2 = c1.with(new Movie("Godfather"));
        for (long i = 0; i < 10000000000L; i++) {
            bh.consume(c2.total(state));
        }
    }
}
