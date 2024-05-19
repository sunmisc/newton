package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class DivNode extends NodeEnvelope {
    public DivNode(Node left, Node right) {
        super(new Node() {
            @Override
            public double evaluate() {
                double leftValue = left.evaluate();
                double rightValue = right.evaluate();

                return leftValue / rightValue;
            }

            @Override
            public String asString() {
                return left.asString() + "/" + right.asString();
            }
        });
    }
}
