package sunmisc.nonlinear.math;

import java.util.function.Function;

public final class Hessian implements Function<Point, double[][]> {
    private final Function<Point, Double> origin;
    private static final double H = 1e-5;
    public Hessian(Function<Point, Double> f) {
        this.origin = f;
    }
    @Override
    public double[][] apply(Point r) {
        final int n = r.length();
        double[][] hessian = new double[n][n];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                Point xh1 = r.transform(x, d -> d + H);
                xh1 = xh1.transform(y, d -> d + H);
                Point xh2 = r.transform(x, d -> d + H);
                xh2 = xh2.transform(y, d -> d - H);
                Point xh3 = r.transform(x, d -> d - H);
                xh3 = xh3.transform(y, d -> d + H);
                Point xh4 = r.transform(x, d -> d - H);
                xh4 = xh4.transform(y, d -> d - H);
                hessian[x][y] = (origin.apply(xh1) -
                        origin.apply(xh2) -
                        origin.apply(xh3) +
                        origin.apply(xh4)
                ) / (4 * H * H);
            }
        }
        return hessian;
    }
}
