package org.benchmark;

import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.Throughput)
@Fork(5)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public abstract class Template {
}
