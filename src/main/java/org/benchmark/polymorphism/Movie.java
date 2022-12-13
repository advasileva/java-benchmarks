package org.benchmark.polymorphism;

import java.util.Random;

public final class Movie extends Product {
    private Random rand = new Random();
    public Movie(String title) {
        super(title);
    }

    @Override
    public int price() {
        return rand.nextInt();
    }
}
