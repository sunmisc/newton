package sunmisc.nonlinear.math;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public final class SubPoint implements Point {

    private final Point a, b;

    public SubPoint(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Number[] point() {
        Number[] as = a.point(), bs = b.point();
        int n = as.length;
        Number[] next = new Number[n];
        for (int i = 0; i < n; i++)
            next[i] = as[i].doubleValue() - bs[i].doubleValue();
        return next;
    }

    @Override
    public Point transform(int i, UnaryOperator<Double> f) {
        Number[] ns = point().clone();
        ns[i] = f.apply(ns[i].doubleValue());
        return new QPoint(ns);
    }

    @Override
    public String toString() {
        return Arrays.toString(point());
    }
}
