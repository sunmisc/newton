package sunmisc.nonlinear.math;

import sunmisc.nonlinear.lazy.SimpleLazy;

import java.util.function.Function;

public final class Hessian implements Function<Point, double[][]> {
    private final Function<Point, Double> f;

    public Hessian(Function<Point, Double> f) {
        this.f = f;
    }

    @Override
    public double[][] apply(Point r) {
        final int n = r.length();
        double[][] hessian = new double[n][n];
        double h = 1e-5;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                final int i = x, j = y;

                hessian[x][y] = new NumberEnvelope(
                        new SimpleLazy<>(() -> {
                            Point xh1 = r.transform(i, d -> d + h);
                            xh1 = xh1.transform(j, d -> d + h);

                            Point xh2 = r.transform(i, d -> d + h);
                            xh2 = xh2.transform(j, d -> d - h);

                            Point xh3 = r.transform(i, d -> d - h);
                            xh3 = xh3.transform(j, d -> d + h);

                            Point xh4 = r.transform(i, d -> d - h);
                            xh4 = xh4.transform(j, d -> d - h);

                            return (f.apply(xh1) -
                                    f.apply(xh2) -
                                    f.apply(xh3) +
                                    f.apply(xh4)
                            ) / (4 * h * h);
                        })).doubleValue();
            }
        }
        return hessian;
    }
}
