package org.benchmark.polymorphism;

public class Cart {
    public Product p;
    private int qty;

    public Cart(Product p) {
        this.p = p;
        this.qty = 10;
    }

    public int total() {
        return p.price() * qty;
    }
}