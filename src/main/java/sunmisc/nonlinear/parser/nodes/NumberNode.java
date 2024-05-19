package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

import java.math.BigDecimal;

public final class NumberNode extends NodeEnvelope {
    public NumberNode(String value) {
        this(new BigDecimal(value));
    }

    public NumberNode(Number value) {
        this(value.doubleValue());
    }
    public NumberNode(double value) {
        super(new Node() {
            @Override
            public double evaluate() {
                return value;
            }

            @Override
            public String asString() {
                return String.valueOf(value);
            }
        });
    }
}
