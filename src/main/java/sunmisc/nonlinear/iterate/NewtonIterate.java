package sunmisc.nonlinear.iterate;

import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.math.*;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class NewtonIterate implements Cursor<Point> {
    private final Function<Point, Double> function;
    private final Function<Point, Point> derivative;
    private final Function<Point, double[][]> hessian;
    private final Point x;

    @Override public boolean exists() { return true; }

    @Override public Point element() { return x; }

    @Override
    public Cursor<Point> next() {
        final Point start = x, dx = derivative.apply(start);
        final UnaryOperator<Point> delta = new LinearSolve(
                hessian.apply(start)
        );
        final Point next = new QPoint(
                new SubPoint(
                        start,
                        delta.apply(dx)
                ).toArray()
        );
        return new NewtonIterate(function, derivative, hessian, next);
    }


    public NewtonIterate(Function<Point, Double> function,
                         Point initialGuess) {
        this(function, new Hessian(function), initialGuess);
    }

    public NewtonIterate(Function<Point, Double> function,
                         Function<Point, double[][]> hessian,
                         Point initialGuess) {
        this(function, new Gradient(function), hessian, initialGuess);
    }

    public NewtonIterate(Function<Point, Double> function,
                         Function<Point, Point> derivative,
                         Function<Point, double[][]> hessian,
                         Point initialGuess) {
        this.function = function;
        this.derivative = derivative;
        this.hessian = hessian;
        this.x = initialGuess;
    }
}

