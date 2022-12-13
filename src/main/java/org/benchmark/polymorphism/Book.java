package org.benchmark.polymorphism;

import java.util.Random;

public final class Book extends Product {
    private Random rand = new Random();
    public Book(String title) {
        super(title);
    }

    @Override
    public int price() {
        return rand.nextInt();
    }
}
