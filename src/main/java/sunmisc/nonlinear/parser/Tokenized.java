package sunmisc.nonlinear.parser;

import sunmisc.nonlinear.Text;
import java.util.Iterator;

public final class Tokenized implements Iterable<String> {
    private final Text input;
    public Tokenized(Text input) { this.input = input; }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private final String text = input.asString();
            private int pos = 0;
            @Override
            public boolean hasNext() {
                while (pos < text.length()) {
                    char current = text.charAt(pos);
                    switch (current) {
                        case '+', '-', '/', '*', '^', '(', ')' -> {
                            return true;
                        }
                        default -> {
                            if (Character.isDigit(current) ||
                                    Character.isLetter(current))
                                return true;
                            else
                                pos++; // skip
                        }
                    }
                }
                return false;
            }

            @Override
            public String next() {
                char current = text.charAt(pos);
                if (Character.isDigit(current)) {
                    StringBuilder sb = new StringBuilder();
                    while (pos < text.length() &&
                            (Character.isDigit(text.charAt(pos)) ||
                                    text.charAt(pos) == '.'))
                        sb.append(text.charAt(pos++));

                    return sb.toString();
                } else
                    pos++;
                return String.valueOf(current);
            }
        };
    }
}
