package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class CosNode extends NodeEnvelope {
    public CosNode(Node val) {
        super(new Node() {
            @Override
            public double evaluate() {
                return Math.cos(val.evaluate());
            }

            @Override
            public String asString() {
                return String.format("cos(%s)", val.asString());
            }
        });
    }
}

