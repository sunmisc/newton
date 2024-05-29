package sunmisc.nonlinear.math;

import java.util.function.UnaryOperator;

public final class LinearSolve implements UnaryOperator<Point> {

    private final double[][] matrixA;

    public LinearSolve(double[][] matrixA) {
        this.matrixA = matrixA;
    }

    @Override
    public Point apply(Point b) {
        int n = b.length();
        Double[] x = new Double[n];
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double q = matrixA[i][j];
                if (i <= j) {
                    U[i][j] = q;
                    for (int k = 0; k < i; k++)
                        U[i][j] -= L[i][k] * U[k][j];
                    if (i == j)
                        L[i][i] = 1;
                } else {
                    L[i][j] = q;
                    for (int k = 0; k < j; k++)
                        L[i][j] -= L[i][k] * U[k][j];
                    L[i][j] /= U[j][j];
                }
            }
        }
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            y[i] = b.param(i).doubleValue();
            for (int j = 0; j < i; j++)
                y[i] -= L[i][j] * y[j];
            y[i] /= L[i][i];
        }
        for (int i = n - 1; i >= 0; i--) {
            x[i] = y[i];
            for (int j = i + 1; j < n; j++)
                x[i] -= U[i][j] * x[j];
            x[i] /= U[i][i];
        }
        return new QPoint(x);
    }
}
