package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class LogNode extends NodeEnvelope {
    public LogNode(Node val) {
        super(new Node() {
            @Override
            public double evaluate() {
                return Math.log(val.evaluate());
            }

            @Override
            public String asString() {
                return String.format("log(%s)", val.asString());
            }
        });
    }
}