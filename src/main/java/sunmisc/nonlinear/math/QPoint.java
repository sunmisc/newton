package sunmisc.nonlinear.math;

public final class QPoint extends PointEnvelope {
    private final Number[] numbers;

    public QPoint(Number... numbers) {
        this.numbers = numbers;
    }
    @Override
    public Number param(int i) {
        return numbers[i];
    }

    @Override
    public int length() {
        return numbers.length;
    }

    @Override
    public Number[] toArray() {
        return numbers.clone();
    }
}

