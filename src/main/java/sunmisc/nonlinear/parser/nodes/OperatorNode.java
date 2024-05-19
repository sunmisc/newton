package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

public final class OperatorNode extends NodeEnvelope {
    public OperatorNode(String operator, Node left, Node right) {
        super(new Node() {
            @Override
            public double evaluate() {
                return (switch (operator) {
                    case "+" -> new SumNode(left, right);
                    case "-" -> new SubNode(left, right);
                    case "*" -> new MultipleNode(left, right);
                    case "/" -> new DivNode(left, right);
                    case "^" -> new PowNode(left, right);
                    default -> throw new IllegalStateException(
                            "Unexpected value: " + operator
                    );
                }).evaluate();
            }

            @Override
            public String asString() {
                return left.asString() + operator + right.asString();
            }
        });
    }
}

