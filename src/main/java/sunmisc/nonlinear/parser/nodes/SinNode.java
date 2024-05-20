package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class SinNode extends NodeEnvelope {
    public SinNode(Node val) {
        super(new Node() {
            @Override
            public double evaluate() {
                return Math.sin(val.evaluate());
            }

            @Override
            public String asString() {
                return String.format("sin(%s)", val.asString());
            }
        });
    }
}