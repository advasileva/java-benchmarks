package org.benchmark.polymorphism;

public final class Book extends Product {
    private int mock;

    public Book(String title) {
        super(title);
    }

    @Override
    public int price(MockState state) {
        mock = state.price;
        return state.price;
    }
}
