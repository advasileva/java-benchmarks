package org.benchmark.polymorphism;

public final class Book extends Product {
    public Book(String title) {
        super(title);
    }

    @Override
    public int price() {
        return 40;
    }
}
