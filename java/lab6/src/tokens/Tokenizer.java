package tokens;

import java.util.ArrayList;
import java.util.List;

public final class Tokenizer {
    private int pos;

    public List<Token> tokenize(String str) {
        pos = 0;
        List<Token> res = new ArrayList<>();
        while (pos < str.length()) {
            char c = str.charAt(pos);
            pos++;
            if (
                    switch (c) {
                        case ' ' -> true;
                        case '(' -> res.add(new Left());
                        case ')' -> res.add(new Right());
                        case '*' -> res.add(new Mul());
                        case '+' -> res.add(new Plus());
                        case '/' -> res.add(new Div());
                        default -> false;
                    }
            ) {
            } else if (c == '-') {
                if (res.size() > 0 && !(res.get(res.size() - 1) instanceof Operation))
                    res.add(new Minus());
                else res.add(new NumberToken(-parseNumber(str)));
            } else if (Character.isDigit(c)) {
                pos--;
                res.add(new NumberToken(parseNumber(str)));
            } else throw new IllegalArgumentException("Incorrect symbol at " + pos);
        }
        return res;
    }

    private int parseNumber(String str) {
        int num = 0;
        if (pos >= str.length() || !Character.isDigit(str.charAt(pos)))
            throw new IllegalArgumentException("Unable to parse expression");
        while (pos < str.length() && Character.isDigit(str.charAt(pos))) {
            num *= 10;
            num += str.charAt(pos) - '0';
            pos++;
        }
        return num;
    }
}
