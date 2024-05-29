package bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.iterate.NewtonIterate;
import sunmisc.nonlinear.math.Point;
import sunmisc.nonlinear.math.QPoint;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.pow;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode({Mode.Throughput})
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 50, time = 1)
@Fork(1)
@Threads(1)
public class NewtonBench {

    private Cursor<? extends Point> start;
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(NewtonBench.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
    @Setup
    public void next() {
        start = new NewtonIterate(
                point -> {
                    return pow(point.param(0).doubleValue(), 2)
                            + point.param(0).doubleValue() * 33;
                },
                new QPoint(1)
        );

    }
    @Benchmark
    public Point newton() {
        var c = start.next();
        start = c;
        return c.element();
    }
}
