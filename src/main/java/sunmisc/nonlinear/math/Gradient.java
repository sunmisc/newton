package sunmisc.nonlinear.math;

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
                new PointEnvelope() {
                    final double fx = function.apply(x);
                    @Override
                    public Number param(int i) {
                        Point xh = x.transform(i, r -> r + H);

                        return (function.apply(xh) - fx) / H;
                    }
                    @Override
                    public int length() {
                        return x.length();
                    }
                }
        );
    }
}
