package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class OperatorNode extends NodeEnvelope {
    public OperatorNode(String operator, Node left, Node right) {
        super(new Node() {
            @Override
            public double evaluate() {
                return switch (operator) {
                    case "+" -> left.evaluate() + right.evaluate();
                    case "-" -> left.evaluate() - right.evaluate();
                    case "*" -> left.evaluate() * right.evaluate();
                    case "/" -> left.evaluate() / right.evaluate();
                    case "^" -> Math.pow(left.evaluate(), right.evaluate());
                    default -> throw new IllegalStateException(
                            "Unexpected value: " + operator
                    );
                };
            }

            @Override
            public String asString() {
                return left.asString() + operator + right.asString();
            }
        });
    }
}


