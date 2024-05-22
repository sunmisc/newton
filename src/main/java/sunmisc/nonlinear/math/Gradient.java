package sunmisc.nonlinear.math;

import sunmisc.nonlinear.lazy.SimpleLazy;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class Gradient implements UnaryOperator<Point> {
    private static final double H = 1e-8;
    private final Function<Point, Double> function;

    public Gradient(Function<Point, Double> function) {
        this.function = function;
    }

    @Override
    public Point apply(Point x) {

        return new CachedPoint(
                new SimpleLazy<>(() -> {
                    int n = x.length();

                    final Number[] grad = new Number[n];
                    double fx = function.apply(x);

                    for (int i = 0; i < n; i++) {
                        final int slot = i;
                        grad[i] = new NumberEnvelope(
                                new SimpleLazy<>(() -> {
                                    Point xh = x.transform(slot, r -> r + H);

                                    return (function.apply(xh) - fx) / H;
                                })
                        );
                    }
                    return grad;
                })
        );
    }
}
