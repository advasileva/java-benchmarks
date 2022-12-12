package org.benchmark.polymorphism;

public class Cart2 {
    private Movie p;
    private int qty;
    public Cart2(Movie m) {
        this.p = m;
        this.qty = 10;
    }
    public int total() { return p.price() * qty; }
}