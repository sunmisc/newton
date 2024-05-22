package sunmisc.nonlinear.iterate;

import sunmisc.nonlinear.Cursor;
import sunmisc.nonlinear.math.*;
import java.util.function.Function;

public final class NewtonIterate implements Cursor<Point> {
    private final Function<Point, Double> function;
    private final Function<Point, Point> derivative;
    private final Point x;

    public NewtonIterate(Function<Point, Double> function) {
        this(function, new QPoint(0, 0));
    }

    public NewtonIterate(Function<Point, Double> function,
                         Point initialGuess) {
        this(function, new Gradient(function), initialGuess);
    }

    public NewtonIterate(Function<Point, Double> function,
                         Function<Point, Point> derivative,
                         Point initialGuess) {
        this.function = function;
        this.derivative = derivative;
        this.x = initialGuess;
    }

    @Override public boolean exists() { return true; }

    @Override public Point element() { return x; }

    @Override
    public Cursor<Point> next() {
        var hessian = new Hessian(function);

        var delta = new LinearSolve(
                hessian.apply(x)
        ).apply(derivative.apply(x));

        return new NewtonIterate(
                function,
                derivative,
                new CachedPoint(
                        new SubPoint(x, delta)
                )
        );
    }
}
