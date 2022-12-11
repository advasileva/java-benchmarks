package org.benchmark.polymorphism;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public class MockState {
    public int price = 123;
}