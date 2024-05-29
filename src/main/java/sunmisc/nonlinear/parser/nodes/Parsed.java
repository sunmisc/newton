package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

import java.util.List;
import java.util.SequencedCollection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class Parsed extends NodeEnvelope {

    public Parsed(Iterable<String> tokens) {
        this(e -> { throw new IllegalStateException(); }, tokens);
    }

    public Parsed(Function<String, Node> param, Iterable<String> tokens) {
        super(next(
                StreamSupport
                        .stream(tokens.spliterator(), false)
                        .collect(Collectors.toList()
                ), param)
        );
    }


    private static Node next(SequencedCollection<String> tokens,
                             Function<String, Node> param) {
        Node node = multiplyDivide(tokens, param);

        while (!tokens.isEmpty()) {
            String first = tokens.getFirst();
            switch (first) {
                case "+", "-" -> {
                    String operator = tokens.removeFirst();
                    Node right = multiplyDivide(tokens, param);
                    node = new OperatorNode(operator, node, right);
                }
                default -> {
                    return node;
                }
            }
        }
        return node;
    }

    private static
    Node multiplyDivide(SequencedCollection<String> tokens,
                        Function<String, Node> param) {
        Node node = power(tokens, param);

        while (!tokens.isEmpty()) {
            String first = tokens.getFirst();
            switch (first) {
                case "*", "/" -> {
                    String operator = tokens.removeFirst();
                    Node right = power(tokens, param);
                    node = new OperatorNode(operator, node, right);
                }
                default -> {
                    return node;
                }
            }
        }
        return node;
    }

    private static Node power(SequencedCollection<String> tokens,
                              Function<String, Node> param) {
        Node node = unary(tokens, param);

        while (!tokens.isEmpty()) {
            String first = tokens.getFirst();
            if ("^".equals(first)) {
                String operator = tokens.removeFirst();
                Node right = unary(tokens, param);
                node = new OperatorNode(operator, node, right);
            } else {
                break;
            }
        }
        return node;
    }

    private static Node unary(SequencedCollection<String> tokens,
                              Function<String, Node> param) {
        String token = tokens.removeFirst();

        switch (token) {
            case "(" -> {
                try {
                    return next(tokens, param);
                } finally {
                    tokens.removeFirst(); // )
                }
            }
            case "sin" -> {
                return new SinNode(unary(tokens, param));
            }
            case "cos" -> {
                return new CosNode(unary(tokens, param));
            }
            case "log" -> {
                return new LogNode(unary(tokens, param));
            }
            default -> {
                try {
                    return new NumberNode(Double.parseDouble(token));
                } catch (NumberFormatException e) {
                    return new Node() {
                        @Override
                        public double evaluate() {
                            return param.apply(token).evaluate();
                        }

                        @Override
                        public String asString() {
                            return param.apply(token).toString();
                        }
                    };
                }
            }
        }
    }
}