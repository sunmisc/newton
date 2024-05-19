package sunmisc.nonlinear.parser;

import sunmisc.nonlinear.parser.nodes.Node;

public class NodeEnvelope implements Node {

    private final Node origin;

    public NodeEnvelope(Node origin) {
        this.origin = origin;
    }

    @Override
    public double evaluate() {
        return origin.evaluate();
    }

    @Override
    public String asString() {
        return origin.asString();
    }

    @Override
    public String toString() {
        return origin.toString();
    }
}
