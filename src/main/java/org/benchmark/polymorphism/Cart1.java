package org.benchmark.polymorphism;

public class Cart1 {
    private Book p;
    private int qty;
    public Cart1(Book b) {
        this.p = b;
        this.qty = 10;
    }
    public int total(MockState state) { return p.price(state) * 2; }
    public Cart2 with(Movie m) {
        Cart2 c2 = new Cart2(m);
        return c2;
    }
}