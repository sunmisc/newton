package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.MultiplicationSign;
import sunmisc.nonlinear.parser.NodeEnvelope;
import sunmisc.nonlinear.parser.Tokenized;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Math.pow;

public final class Parsed extends NodeEnvelope {
    public Parsed(String... tokens) {
        this(Map.of(), List.of(tokens));
    }

    public Parsed(Map<String, Node> param, String... tokens) {
        this(param, List.of(tokens));
    }

    public Parsed(Iterable<String> tokens) {
        this(Map.of(), tokens);
    }

    public Parsed(Map<String, Node> param, Iterable<String> tokens) {
        super(next(
                StreamSupport
                        .stream(tokens.spliterator(), false)
                        .collect(Collectors.toList()
                ), param)
        );
    }


    private static Node next(SequencedCollection<String> tokens,
                             Map<String, Node> param) {
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
                        Map<String, Node> param) {
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
                              Map<String, Node> param) {
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
                              Map<String, Node> param) {
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
                return new Node() {
                    @Override
                    public double evaluate() {
                        Node val = param.get(token);
                        return (
                                val != null ? val : new NumberNode(token)
                        ).evaluate();
                    }

                    @Override
                    public String asString() {
                        Node val = param.get(token);
                        return (
                                val != null ? val : new NumberNode(token)
                        ).toString();
                    }
                };
            }
        }
    }

    public static void main(String[] args) {
        double x = 1222, y = x + 333;
        String expression = "50*(X2-X1)^2+(X2+X1-1)^2+X3^2+X4^2";
        Parsed parsed1 = new Parsed(
                Map.of(
                        "X1", new NumberNode(x),
                        "X2", new NumberNode(y)
                ),
                new Tokenized(new MultiplicationSign(
                        () -> expression)
                )
        );


        double a = parsed1.evaluate();
        double b = 100 * pow((y-pow(x, 2)), 2) +pow((x-1), 2);
        System.out.println(a + " " + b);
    }
}