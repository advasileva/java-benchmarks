package org.benchmark.polymorphism;

public final class Movie extends Product {
    private int mock;
    public Movie(String title) {
        super(title);
    }

    @Override
    public int price(MockState state) {
        mock += state.price;
        return mock;
    }
}
