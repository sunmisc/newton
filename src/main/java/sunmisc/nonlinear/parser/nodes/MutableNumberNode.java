package sunmisc.nonlinear.parser.nodes;

import java.util.concurrent.atomic.AtomicReference;

public final class MutableNumberNode implements Node {

    private final AtomicReference<Double> value;

    public MutableNumberNode(AtomicReference<Double> value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return String.valueOf(value.get());
    }

    @Override
    public double evaluate() {
        return value.get();
    }
}


