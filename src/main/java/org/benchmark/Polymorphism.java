package org.benchmark;

import org.benchmark.polymorphism.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class Polymorphism extends Template {
    @Benchmark
    public void measureWith(Blackhole bh) {
        Cart c = new Cart(new Book("1984"));
        c.p = new Movie("Godfather");
        for (int i = 0; i < 1000000000; i++) {
            bh.consume(c.total());
        }
    }

    @Benchmark
    public void measureWithout(Blackhole bh) {
        Cart1 c1 = new Cart1(new Book("1984"));
        Cart2 c2 = c1.with(new Movie("Godfather"));
        for (int i = 0; i < 1000000000; i++) {
            bh.consume(c2.total());
        }
    }
}
