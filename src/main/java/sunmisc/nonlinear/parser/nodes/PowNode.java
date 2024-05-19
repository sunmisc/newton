package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class PowNode extends NodeEnvelope {
    public PowNode(Node left, Node right) {
        super(new Node() {
            @Override
            public double evaluate() {
                double leftValue = left.evaluate();
                double rightValue = right.evaluate();

                return Math.pow(leftValue, rightValue);
            }

            @Override
            public String asString() {
                return left.asString() + "^" + right.asString();
            }
        });
    }
}
