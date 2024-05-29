package sunmisc.nonlinear.parser.nodes;

import sunmisc.nonlinear.parser.NodeEnvelope;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomNode extends NodeEnvelope {
    public RandomNode(double origin, double bound) {
        super(new Node() {
            @Override
            public double evaluate() {
                return ThreadLocalRandom.current().nextDouble(origin, bound);
            }

            @Override
            public String asString() {
                return "random";
            }
        });
    }
}
