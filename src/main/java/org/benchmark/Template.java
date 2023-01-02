package org.benchmark;

import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
@Fork(10)
@Warmup(iterations = 0)
@Measurement(iterations = 1)
public abstract class Template {
}
