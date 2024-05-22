package sunmisc.nonlinear.math;

import sunmisc.nonlinear.lazy.SimpleLazy;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class CachedPoint implements Point {
    private final Supplier<Number[]> number;

    public CachedPoint(Supplier<Number[]> point) {
        this.number = point;
    }

    public CachedPoint(Point point) {
        this.number = new SimpleLazy<>(point::point);
    }

    @Override
    public Number[] point() {
        return number.get();
    }

    @Override
    public Point transform(int i, UnaryOperator<Double> f) {
        Number[] copy = point().clone();
        Number p = copy[i];
        copy[i] = f.apply(p.doubleValue());
        return new QPoint(copy);
    }

    @Override
    public String toString() {
        return Arrays.toString(point());
    }
}
