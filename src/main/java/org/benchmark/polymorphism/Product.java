package org.benchmark.polymorphism;

public abstract class Product {
    private String title;
    public Product(String title) {
        this.title = title;
    }
    public abstract int price();
}
