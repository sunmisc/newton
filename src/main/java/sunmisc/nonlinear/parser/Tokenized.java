package sunmisc.nonlinear.parser;

import sunmisc.nonlinear.Text;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Tokenized implements Iterable<String> {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\d+(\\.\\d*)?|[A-Za-z][A-Za-z0-9]*|[+\\-*/^()]|\\s+|."
    );
    private final Text input;

    public Tokenized(Text input) {
        this.input = input;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private final Matcher matcher = TOKEN_PATTERN.matcher(input.asString());

            @Override
            public boolean hasNext() {
                while (matcher.find()) {
                    String token = matcher.group();
                    if (!token.matches("\\s+"))
                        return true;
                }
                return false;
            }

            @Override
            public String next() {
                return matcher.group();
            }
        };
    }
}
