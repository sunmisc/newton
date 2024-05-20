package sunmisc.nonlinear.parser;

import sunmisc.nonlinear.Text;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Tokenized implements Iterable<String> {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\d+(\\.\\d*)?|[A-Za-z]+|[+\\-*/^()]|\\s+|."
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
                String token = matcher.group();
                if (token.matches("\\d+(\\.\\d*)?") ||
                        token.matches("[A-Za-z]+") ||
                        token.matches("[+\\-*/^()]")) {
                    return token;
                } else {
                    throw new RuntimeException("Unexpected character: " + token);
                }
            }
        };
    }

}
