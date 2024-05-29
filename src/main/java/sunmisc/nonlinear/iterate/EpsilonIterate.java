package sunmisc.nonlinear.iterate;

import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.math.Point;

import java.util.function.Function;

import static java.lang.Math.abs;

public final class EpsilonIterate implements Cursor<Point> {
    private final Cursor<Point> itr;
    private final Function<Point, Double> origin;
    private final double epsilon;

    public EpsilonIterate(Cursor<Point> itr,
                          Function<Point, Double> origin,
                          double epsilon) {
        this.itr = itr;
        this.origin = origin;
        this.epsilon = epsilon;
    }
    @Override
    public boolean exists() {
        return itr.exists() &&
                abs(
                        origin.apply(itr.element())
                ) > epsilon;
    }
    @Override
    public Point element() {
        return itr.element();
    }
    @Override
    public Cursor<Point> next() {
        return new EpsilonIterate(itr.next(), origin, epsilon);
    }
}
