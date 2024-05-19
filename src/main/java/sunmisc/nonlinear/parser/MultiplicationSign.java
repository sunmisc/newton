package sunmisc.nonlinear.parser;

import sunmisc.nonlinear.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MultiplicationSign implements Text {
    private static final Pattern COMPILE =
            Pattern.compile("(\\d+)([a-zA-Z])");
    private final Text origin;

    public MultiplicationSign(Text origin) {
        this.origin = origin;
    }

    @Override
    public String asString() {
        Matcher matcher = COMPILE.matcher(origin.asString());

        StringBuilder sb = new StringBuilder();

        while (matcher.find())
            matcher.appendReplacement(sb, "$1 * $2");
        matcher.appendTail(sb);

        return sb.toString();
    }
}
