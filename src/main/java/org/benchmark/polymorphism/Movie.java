package org.benchmark.polymorphism;

public final class Movie extends Product {
    public Movie(String title) {
        super(title);
    }

    @Override
    public int price() {
        return 15;
    }
}
